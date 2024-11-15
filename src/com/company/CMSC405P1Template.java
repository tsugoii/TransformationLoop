package com.company;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class CMSC405P1Template extends JPanel {

    // A counter that increases by one in each frame.
    private int frameNumber;
    // The time, in milliseconds, since the animation started.
    private long elapsedTimeMillis;

    static int translateX = 0;
    static int translateY = 0;
    //in terms of Rads
    static double rotationOne = 0.0;
    static double rotationTwo = 0.0;
    static double scaleX = 1.0;
    static double scaleY = 1.0;
    static ImageTemplate myImages = new ImageTemplate();
    static BufferedImage tImage;

    public static void main(String[] args) {
        JFrame window = new JFrame("Java Animation");
        final CMSC405P1Template panel = new CMSC405P1Template(); // The drawing area.
        JTextField selection = new JTextField("Select Desired Shape: ");
        selection.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        selection.setBackground(Color.WHITE);
        selection.setEditable(false);
        panel.add(selection);
        JButton squareButton = new JButton("Square");
        panel.add(squareButton);
        JButton circleButton = new JButton("Circle");
        panel.add(circleButton);
        JButton triangleButton = new JButton("Triangle");
        panel.add(triangleButton);
        window.setContentPane(panel); // Show the panel in the window.
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // End program when window closes.
        window.pack();  // Set window size based on the preferred sizes of its contents.
        window.setResizable(false); // Don't let user resize window.
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation( // Center window on screen.
                (screen.width - window.getWidth()) / 2,
                (screen.height - window.getHeight()) / 2);
        Timer animationTimer;  // A Timer that will emit events to drive the animation.
        final long startTime = System.currentTimeMillis();
        // Modified to change timing and allow for recycling
        animationTimer = new Timer(1600, arg0 -> {
            if (panel.frameNumber > 4) {
                panel.frameNumber = 0;
            } else {
                panel.frameNumber++;
            }
            panel.elapsedTimeMillis = System.currentTimeMillis() - startTime;
            panel.repaint();
        });
        window.setVisible(true);
        triangleButton.addActionListener(e -> {
            tImage = myImages.getImage(ImageTemplate.triangleShape);
            animationTimer.start();
        });
        circleButton.addActionListener(e -> {
            tImage = myImages.getImage(ImageTemplate.circleShape);
            animationTimer.start();
        });
        squareButton.addActionListener(e -> {
            tImage = myImages.getImage(ImageTemplate.squareShape);
            animationTimer.start();
        });
    }

    public CMSC405P1Template() {
        // Size of Frame
        setPreferredSize(new Dimension(600, 600));
    }

    protected void paintComponent(Graphics g) {

        /* First, create a Graphics2D drawing context for drawing on the panel.
         * (g.create() makes a copy of g, which will draw to the same place as g,
         * but changes to the returned copy will not affect the original.)
         */
        Graphics2D g2 = (Graphics2D) g.create();

        // Turn on antialiasing in this graphics context, for better drawing.
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill in the entire drawing area with white.
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight()); // From the old graphics API!

        /* Here, I set up a new coordinate system on the drawing area, by calling
         * the applyLimits() method that is defined below.  Without this call, I
         * would be using regular pixel coordinates.  This function sets the value
         * of the global variable pixelSize, which I need for stroke widths in the
         * transformed coordinate system.
         */
        // Controls your zoom and area you are looking at
        applyWindowToViewportTransformation(g2, -75, 75, -75, 75, true);
        AffineTransform savedTransform = g2.getTransform();
        String operationHappening = "Nothing";
        switch (frameNumber) {
            case 1 -> { // First frame is unmodified.
                translateX = 0;
                translateY = 0;
                scaleX = 1.0;
                scaleY = 1.0;
                rotationOne = 0;
                rotationTwo = 0;
            }
            case 2 -> { // Second frame translates each image by (-5, 7).
                operationHappening = "Translated -5 X, 7 Y";
                translateX = -5;
                translateY = 7;
            }
            case 3 -> { // Third frame rotates each image by 45 degrees Counter
                operationHappening = "Rotating 320 Degrees";
                translateX = -5;
                translateY = 7;
                rotationOne = 45 * Math.PI / 180.0;
            }
            case 4 -> { // Fourth frame rotates each image by 90 degrees clockwise
                operationHappening = "Rotating 90 Degrees";
                translateX = -5;
                translateY = 7;
                rotationTwo = 275 * Math.PI / 180.0;
            }
            case 5 -> { // scale x by 2, y by .5
                operationHappening = "Scaling X by 2, Y by .5";
                translateX = -5;
                translateY = 7;
                rotationTwo = 275 * Math.PI / 180.0;
                scaleX = 2.0;
                scaleY = 0.5;
            }
            default -> {
            }
        } // End switch
        System.out.println("Frame is: " + frameNumber + " Operation is: " + operationHappening);
        g2.translate(translateX, translateY); // Move image.
        // To offset translate again
        g2.translate(-10,10);
        g2.rotate(rotationOne); // Rotate image.
        g2.rotate(rotationTwo);
        g2.scale(scaleX, scaleY); // Scale image.
        g2.drawImage(tImage, 0, 0, this); // Draw image.
        g2.setTransform(savedTransform);
    }
    private void applyWindowToViewportTransformation(Graphics2D g2,
            double left, double right, double bottom, double top,
            boolean preserveAspect) {
        int width = getWidth();   // The width of this drawing area, in pixels.
        int height = getHeight(); // The height of this drawing area, in pixels.
        if (preserveAspect) {
            // Adjust the limits to match the aspect ratio of the drawing area.
            double displayAspect = Math.abs((double) height / width);
            double requestedAspect = Math.abs((bottom - top) / (right - left));
            if (displayAspect > requestedAspect) {
                // Expand the viewport vertically.
                double excess = (bottom - top) * (displayAspect / requestedAspect - 1);
                bottom += excess / 2;
                top -= excess / 2;
            } else if (displayAspect < requestedAspect) {
                // Expand the viewport vertically.
                double excess = (right - left) * (requestedAspect / displayAspect - 1);
                right += excess / 2;
                left -= excess / 2;
            }
        }
        g2.scale(width / (right - left), height / (bottom - top));
        g2.translate(-left, -top);
        double pixelWidth = Math.abs((right - left) / width);
        double pixelHeight = Math.abs((bottom - top) / height);
        // This is the measure of a pixel in the coordinate system
        // set up by calling the applyLimits method.  It can be used
        // for setting line widths, for example.
        float pixelSize = (float) Math.max(pixelWidth, pixelHeight);
    }
}
