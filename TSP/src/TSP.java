import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
//INPUT FORMAT SHOULD LOOK LIKE THIS:
//1,38 Parsons Hall Maynooth,4,53.37521,-6.6103
//2,34 Silken Vale Maynooth ,6,53.37626,-6.59308
//3,156 Glendale Leixlip ,18,53.37077,-6.48279
//4,33 The Paddocks Oldtown Mill Celbridge ,8,53.3473,-6.55057
//5,902 Lady Castle K Club Straffan ,11,53.31159,-6.60538
//6,9 The Park Louisa Valley Leixlip ,3,53.36115,-6.48907
//7,509 Riverforest Leixlip ,10,53.37402,-6.49363
//8,16 Priory Chase St.Raphaels Manor Celbridge ,7,53.33886,-6.55468
//9,13 Abbey Park Court Clane,13,53.2908,-6.67746
//10,117 Royal Meadows Kilcock ,12,53.39459,-6.66995

public class TSP extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new TSP();
    }

    int ROWS;
    //Add the map picture so it can link to Application
    ImageIcon icon = new ImageIcon("map.jpeg");
    JLabel background = new JLabel(icon);
    JLabel container;
    JLabel Data = new JLabel("Data"), Output = new JLabel("Output");
    JTextArea inputFiled, outputFiled;
    JButton calculate = new JButton("Calculate");


    public TSP(){

        Data.setBounds(240,30,30,30);

        inputFiled = new JTextArea();
        inputFiled.setBackground(new Color(224,224,224));
        inputFiled.setLineWrap(true);
        JScrollPane inputBar=new JScrollPane(inputFiled,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        inputBar.setBounds(40,70,420,150);

        calculate.setBounds(210,230,100,20);
        calculate.setBackground(new Color(255,250,250));
        calculate.setFocusable(false);
        calculate.addActionListener(this::actionPerformed);

        Output.setBounds(240,250,50,30);

        outputFiled = new JTextArea();
        outputFiled.setBackground(new Color(244,244,244));
        outputFiled.setLineWrap(true);
        JScrollPane outputBar = new JScrollPane(outputFiled,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        outputBar.setBounds(40,285,420,90);

        container = new JLabel();
        container.setBackground(Color.WHITE);
        container.setOpaque(true);
        container.setBounds(170,100,500,400);
        container.setVisible(true);
        container.add(Data);
        container.add(inputBar);
        container.add(calculate);
        container.add(Output);
        container.add(outputBar);

        background.setLayout(null);
        background.add(container);

        add(background);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        setSize(icon.getIconWidth()+10,icon.getIconHeight()+10);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource()==calculate){
            String data = inputFiled.getText();
            Scanner scanner = new Scanner(data);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                ROWS++;
            }
            scanner.close();
            String outputData = trackFinder(data,ROWS);
            outputFiled.setText(outputData);
        }

    }

    public double distance(double lat1, double lon1, double lat2, double lon2)
    {
        // distance between latitudes and longitudes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }

    public String[] removeTheElement(String[] arr, int index)
    {
        if (arr == null
                || index < 0
                || index >= arr.length) {

            return arr;
        }

        String[] anotherArray = new String[arr.length - 1];

        for (int i = 0, k = 0; i < arr.length; i++) {
            if (i == index) {
                continue;
            }
            anotherArray[k++] = arr[i];
        }

        return anotherArray;
    }

    public double[][] arrayTrimmer(double[][] arr, int index){

        if (arr == null){
            return arr;
        }else {
            int x = 0;
            double[][] arrTemp = new double[ROWS-1][2];
            for (int i = 0; i < ROWS; i++) {
                if (i!=index) {
                    for (int j = 0; j < 2; j++) {
                        arrTemp[x][j] = arr[i][j];
                    }
                    x++;
                }
            }
            ROWS--;
            return arrTemp;
        }
    }

    public int nearestPoint(double[] coordinate, double[][] points) {
        final int X = 0;
        final int Y = 1;
        int indexFound = 0;
        double[] closestPoint = points[0];
        double closestDist = distance(coordinate[X], coordinate[Y],
                closestPoint[X], closestPoint[Y]);

        // Traverse the array
        for(int i = 0; i < points.length; i++) {
            double dist = distance(coordinate[X], coordinate[Y],
                    points[i][X], points[i][Y]);
            if (dist < closestDist && dist != 0.0) {
                closestDist = dist;
                closestPoint = points[i];
                indexFound = i;
            }
        }

        //return closestPoint;
        return indexFound;
    }

    public String timeCalculator(String data, double[] pizzaLocation, String[] track){

        Map<String, java.util.List<Double>> map = new HashMap<>();
        java.util.List<Double> pizzaLoc = new ArrayList<>();
        pizzaLoc.add(pizzaLocation[0]);
        pizzaLoc.add(pizzaLocation[1]);
        pizzaLoc.add(0.0);
        map.put("0",pizzaLoc);
        Scanner scanner = new Scanner(data);
        int i = 0, j = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            double lat = Double.parseDouble(line.split(",")[3]);
            double lon = Double.parseDouble(line.split(",")[4]);
            double time = Double.parseDouble(line.split(",")[2]);
            String key = line.split(",")[0];
            java.util.List<Double> arr = new ArrayList<>();
            arr.add(lat);
            arr.add(lon);
            arr.add(time);
            map.put(key, arr);
            i++;
        }
        scanner.close();

        double angryseconds=0;
        double kmph = 60;
        double elapsedtime=0;
        for( i=0;i<track.length-1;i++){
            String house1=track[i];
            String house2=track[i+1];
            double lat1 = 0, lat2 = 0, lon1 = 0, lon2 = 0, time = 0;
            for (Map.Entry<String, List<Double>> listMap : map.entrySet()) {
                if (listMap.getKey().equals(house1)) {
                    lat1 = listMap.getValue().get(0);
                    lon1 = listMap.getValue().get(1);
                } else if (listMap.getKey().equals(house2)) {
                    lat2 = listMap.getValue().get(0);
                    lon2 = listMap.getValue().get(1);
                    time = listMap.getValue().get(2);
                }
            }
            double distance=distance(lat1,lon1,lat2,lon2);
            System.out.println(distance);
            elapsedtime+=distance/kmph;
            angryseconds=angryseconds+Math.max(0.0,(time)*60+elapsedtime-1800);
        }

        String output;

        output = "The total time elapsed for delivery in minutes is "+elapsedtime + "\n";
        output += "The total number of angry minutes is "+angryseconds/60.0;
        return output;
    }

    public String trackFinder(String data, int ROWS1){
        int ROWS = ROWS1;
        String outputData = "";

        double[][] coordinates = new double[ROWS][2];
        String[] houseIds = new String[ROWS];
        double[] pizzaLocation = {53.38197,-6.59274};
        double[] startingPoints = pizzaLocation;

        Scanner scanner = new Scanner(data);
        int i = 0, j = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            coordinates[i][j] = Double.parseDouble(line.split(",")[3]);
            coordinates[i][j+1] = Double.parseDouble(line.split(",")[4]);
            houseIds[i] = line.split(",")[0];
            i++;
        }
        scanner.close();
        String[] track = new String[ROWS];
        for (int k = 0; k < track.length; k++) {
            int a = nearestPoint(startingPoints,coordinates);
            track[k] = houseIds[a];
            houseIds = removeTheElement(houseIds,a);
            startingPoints[0] = coordinates[a][0];
            startingPoints[1] = coordinates[a][1];
            coordinates = arrayTrimmer(coordinates,a);
        }
        for (int k = 0; k < track.length; k++) {
            outputData += track[k];
            if (k==track.length-1){
                continue;
            }
            outputData += "," ;
        }
        outputData += "\n";

        outputData += timeCalculator(data,pizzaLocation,track);

        return outputData;
    }

}
