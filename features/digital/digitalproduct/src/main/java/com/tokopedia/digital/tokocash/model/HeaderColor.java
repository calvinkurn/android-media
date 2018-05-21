package com.tokopedia.digital.tokocash.model;

/**
 * Created by nabillasabbaha on 8/30/17.
 */

public class HeaderColor {

    private int color;

    private int background;

    public HeaderColor(int color, int background) {
        this.color = color;
        this.background = background;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }
}
