package com.tokopedia.gamification.cracktoken.presentation.model;

/**
 * Created by Rizky on 28/03/18.
 */

public class RewardTextStyle {
    private String color;
    private String text;
    private int size;

    public RewardTextStyle(String color, String text, int size) {
        this.color = color;
        this.text = text;
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public String getText() {
        return text;
    }

    public int getSize() {
        return size;
    }
}
