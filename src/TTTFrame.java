import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class TTTFrame extends JFrame implements MouseListener, KeyListener
{
    // Display message
    private String text = "";
    // the letter you are playing as
    private char player;
    // store the letter of the active player
    private char turn = 'X';
    // stores all the game data
    private GameData gameData = null;
    // output stream to the server


    private int xOfLeft = 50;
    private int yOfLeft = 50;
    ObjectOutputStream os;


    public TTTFrame(GameData gameData, ObjectOutputStream os, char player)
    {
        super("TTT Game");
        // sets the attributes
        this.gameData = gameData;
        this.os = os;
        this.player = player;


        // adds a MouseListener to the Frame
        addMouseListener(this);

        // adds a KeyListener to the Frame
        addKeyListener(this);

        // makes closing the frame close the program
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        if (player == 'X')
        {
            text = "Waiting for O to Connect";
        }
        setSize(540,540);
        setAlwaysOnTop(true);
        setVisible(true);
    }


    public void paint(Graphics g)
    {
        // draws the background
        g.setColor(new Color(175, 238, 238));
        g.fillRect(0,0,getWidth(),getHeight());


        // draws the display text to the screen
        g.setColor(Color.BLACK);
        g.setFont(new Font("Times New Roman",Font.BOLD,30));
        g.drawString(text,20,65);


        int x = 30;
        int y = 100;
        for(int r=0; r<6; r++)
        {
            for (int c = 0; c <6; c++)
            {
                if (gameData.getGrid()[r][c] == 'X')
                {
                    if (isHorizontal(x, y)) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setColor(Color.red);
                        g2.setStroke(new BasicStroke(10));
                        g2.drawLine(x+35, y+20, x+85, y+20);
                    }
                    if (isVertical())
                    {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setColor(Color.red);
                        g2.setStroke(new BasicStroke(20));


                        g2.drawLine(x+45, y+20, x+100, y+20);
                    }
                }
                else if (gameData.getGrid()[r][c] == 'O')
                {
                    if (isHorizontal(x, y)) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setColor(Color.blue);
                        g2.setStroke(new BasicStroke(10));
                        g2.drawLine(x+35, y+20, x+85, y+20);
                    }
                    if (isVertical())
                    {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setColor(Color.pink);
                        g2.setStroke(new BasicStroke(20));


                        g2.drawLine(x+45, y+20, x+100, y+20);
                    }
                }

                g.setColor(Color.black);
                g.fillOval(x, y, 40, 40);
                x += 85;
            }
            x = 30;
            y += 70;
        }

    }


    public void setText(String text) {
        this.text = text;
        repaint();
    }


    public char getPlayer() {
        return player;
    }


    public void setPlayer(char player) {
        this.player = player;
    }




    public char getTurn() {
        return turn;
    }

    public void setTurn(char turn) {
        this.turn = turn;
        if (turn == player)
        {
            text = "Your turn";
        }
        else
        {
            if (turn == 'X')
            {
                text = "Red's turn.";
            }
            else
            {
                text = "Black's turn.";
            }
        }
        repaint();
    }


    public void makeMove(int c, int r, char letter)
    {
        gameData.getGrid()[r][c] = letter;
        repaint();
    }


    public void reset()
    {
        gameData.reset();
    }


    public char other()
    {
        if(player=='x')
            return 'o';
        else
            return 'x';
    }


    public GameData getGameData()
    {
        return gameData;
    }



    @Override
    public void mouseClicked(MouseEvent e)
    {

    }


    @Override
    public void keyTyped(KeyEvent event) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        char key = e.getKeyChar();

        if (key == 'R')
        {
            try {
                os.writeObject(new CommandFromClient(CommandFromClient.RESTART, gameData + ""));
            }
            catch(Exception o)
            {
                o.printStackTrace();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        int x = e.getX();
        int y = e.getY();
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            xOfLeft = x;
            yOfLeft = y;
        }

        int row = 0;
        int col = 0;
        x = 30;
        y = 100;
        for (int r = 0; r < 11; r++) {
            for (int c = 0; c < 11; c++) {
                //add isHorizontal and if true
                if (isHorizontal(x, y)) {
                    row = r;
                    col = c;
                    break;
                }
                if (isVertical())
                {
                    row = r;
                    col = c;
                    break;
                }
                x += 85;
            }
            x = 30;
            y += 70;
        }

        try
        {
            os.writeObject(new CommandFromClient(CommandFromClient.MOVE, "" + col + row + player));
        }
        catch(Exception l)
        {
            l.printStackTrace();
        }
    }




    @Override
    public void mouseReleased(MouseEvent e)
    {


    }


    @Override
    public void mouseEntered(MouseEvent e) {


    }


    @Override
    public void mouseExited(MouseEvent e) {


    }

    public boolean isHorizontal(int x, int y)
    {
        if (xOfLeft >= x && xOfLeft <= x + 100 && yOfLeft >= y && yOfLeft <= y + 100)
        {
            return true;
        }
        return false;
    }

    public boolean isVertical()
    {
        //fix if statement
        for (int r = 30; r < 70; r+=85)
        {
            for (int c = 100; c< 140; c+=70)
            {
                if (xOfLeft == r && yOfLeft == c)
                {
                    return true;
                }
            }
        }
        return false;
    }
}
