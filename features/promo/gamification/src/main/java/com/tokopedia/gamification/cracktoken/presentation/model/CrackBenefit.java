package com.tokopedia.gamification.cracktoken.presentation.model;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class CrackBenefit {

    private String text;
    private String color;
    private int size;

    public CrackBenefit(String text, String color, int size) {
        this.text = text;
        this.color = color;
        this.size = size;
    }

    public String getText() {
        return text;
    }

    public String getColor() {
        return color;
    }

    public int getSize() {
        return size;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
