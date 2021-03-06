package Baseball;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.poi.EmptyFileException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class SelectTeamsMenu extends JFrame implements ActionListener, ListSelectionListener {
	
    JList list;
    DefaultListModel listModel;
    JLabel team1;
    JLabel team2;
    JButton select;
    JButton select2;
    JButton createNewTeam;
    JButton playerSelectionButton;
    Team t1;
    Team t2;
    Game game;
    //static XSSFWorkbook workbook;
    static Commands commands = new Commands();
    
	public SelectTeamsMenu(Game g, String title) {
		super(title);
		game = g;
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
//		workbook = new XSSFWorkbook();
//        try {
//			workbook = locateFile();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
        listModel = new DefaultListModel();
        ArrayList<Team> teams = game.teamList;
        int numTeams = teams.size();
        for (int i = 0; i < numTeams; i++) {
        	listModel.addElement(teams.get(i).getTeamName());
        }

        //Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);
        JLabel team = new JLabel("Available Teams: ");
        select = new JButton("Select Team 1: ");
        select2 = new JButton("Select Team 2: ");
        select.addActionListener(this);
        select2.addActionListener(this);
        list.addListSelectionListener(this);
        team1 = new JLabel("Team 1");
        team2 = new JLabel("Team 2");
        
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = gc.SOUTHWEST;
		gc.gridx = 1;
		gc.gridy = 0;
		gc.weighty = 0.25;
		add(team, gc);
		
		gc.gridx = 1;
		gc.gridy = 1;
		gc.ipadx = 100;
		//gc.weighty = 0.25;
		gc.anchor = gc.CENTER;
		gc.ipady = 50;
		gc.fill = GridBagConstraints.NONE;
		add(listScrollPane, gc);
		
		gc.ipadx = 0;
		gc.ipady = 0;
		gc.insets = new Insets(0, 10, 0, 0);
		gc.gridx = 1;
		gc.gridy = 2;
		gc.anchor = gc.WEST;
		add(team1, gc);
		
		gc.gridx = 0;
		gc.gridy = 3;
		gc.ipadx = 0;
		gc.weighty = 0.25;
		gc.insets = new Insets(0, 10, 0, 0);
		add(select2, gc);
		
		gc.gridx = 1;
		gc.gridy = 3;
		gc.anchor = gc.WEST;
		add(team2, gc);
		
		gc.gridx = 0;
		gc.gridy = 2;
		add(select, gc);
		
		gc.anchor = gc.EAST;
        createNewTeam = new JButton("Create new team");
        createNewTeam.addActionListener(this);
		gc.gridx = 2;
		gc.gridy = 2;
		gc.weighty = 0.25;
		gc.insets = new Insets(0, 0, 0, 10);
		add(createNewTeam, gc);
		
		playerSelectionButton = new JButton("Choose players");
		playerSelectionButton.addActionListener(this);
		gc.gridx = 2;
		gc.gridy = 3;
		gc.weighty = 0.25;
		add(playerSelectionButton, gc);
		
		setSize(400, 300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
    
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == select) {
			String team = (String) list.getSelectedValue();
			System.out.println(list.getSelectedIndex());
			team1.setText(team);
			t1 = game.teamList.get(list.getSelectedIndex());
			game.setTeamA(t1);
		}
		else if (arg0.getSource() == select2) {
			String team = (String) list.getSelectedValue();
			team2.setText(team);
			t2 = game.teamList.get(list.getSelectedIndex());
			game.setTeamB(t2);
		}
		else if (arg0.getSource() == createNewTeam) {
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new createNewTeamGUI("Create New Team").createAndShowGUI();
				}
			});
		}
		else if (arg0.getSource() == playerSelectionButton) {
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					//System.out.println("run");
					game.switchToSelectPlayerState();
					dispose();
				}
			});
		}
		
	}
	
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public XSSFWorkbook locateFile() throws FileNotFoundException, IOException {
		File fs = new File("teams.xlsx");
        XSSFWorkbook workbook = null;
        if (!fs.exists()) //if the file does not exist, then create the file instead
        {
            workbook = new XSSFWorkbook();
            FileOutputStream fOS = new FileOutputStream("teams.xlsx");
            workbook.write(fOS);
            fOS.close();
        }
        try {
            workbook = new XSSFWorkbook("teams.xlsx");
        }
        catch (EmptyFileException e) {
        	try {
				Commands.rewriteFile();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        }
        return workbook;
        
	}
	
	   /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public void createAndShowGUI() {
        //Create and set up the window.
 
        //Create and set up the content pane.
        JFrame frame = new SelectTeamsMenu(game, "Select Teams");
 
        //Display the window.
        frame.setVisible(true);
    }
 
//    public static void main(String[] args) {
//        //Schedule a job for the event-dispatching thread:
//        //creating and showing this application's GUI.
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                createAndShowGUI();
//            }
//        });
//    }

    public class createNewTeamGUI extends JFrame implements ActionListener {
    	
    	JButton create;
    	JTextField field;
    	public createNewTeamGUI(String title) {
    		super(title);
    		
    		setLayout(new GridBagLayout());
    		GridBagConstraints gc = new GridBagConstraints();
    		gc.anchor = gc.CENTER;
    		gc.gridx=0;
    		gc.gridy=0;
    		gc.fill = GridBagConstraints.NONE;
    		gc.insets = new Insets(0, 0, 0, 5);
    		field = new JTextField("New Team");
    		create = new JButton("Create Team");
    		gc.ipady = 7;
    		gc.ipadx = 50;
    		add(field, gc);
    		gc.gridx=1;
    		gc.ipady = 0;
    		gc.ipadx = 0;
    		add(create, gc);
    		create.addActionListener(this);
    		
    		
    		setSize(400, 400);
    		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    	}
    	public void createAndShowGUI() {
    		JFrame frame = new createNewTeamGUI("Create New Team");
    		
    		frame.setVisible(true);
    	}
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == create) {
				listModel.addElement(field.getText());
				Team t = new Team(field.getText());
				game.addTeam(t);
				dispose();
			}
			
		}
    }
}
