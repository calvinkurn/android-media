package com.tokopedia.buyerorder.detail.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Color {
    @SerializedName("border")
    @Expose
    private String border;
    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("textColor")
    @Expose
    private String textColor;

    public String border(){
        return border;
    }

    public String background(){
        return background;
    }

    public String textColor() {
        return textColor;
    }

    @Override
    public String toString() {
        return "Color{"
                + "border=" + border + ", "
                + "background=" + background + ", "
                + "textColor="+textColor

                + "}";
    }
}