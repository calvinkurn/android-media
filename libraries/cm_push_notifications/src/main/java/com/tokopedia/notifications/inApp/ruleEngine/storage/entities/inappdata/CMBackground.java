package com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author lalit.singh
 */
public class CMBackground {

    @Expose
    @ColumnInfo(name = "img")
    @SerializedName("img")
    public String img = "";

    @Expose
    @ColumnInfo(name = "clr")
    @SerializedName("clr")
    public String color = "";

    @Expose
    @ColumnInfo(name = "sc")
    @SerializedName("sc")
    public String strokeColor = "";

    @Expose
    @ColumnInfo(name = "sw")
    @SerializedName("sw")
    public int strokeWidth = 0;

    @Expose
    @ColumnInfo(name = "rd")
    @SerializedName("rd")
    public float cornerRadius = 0;

    public CMBackground(){}

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public float getCornerRadius() {
        if (cornerRadius == 0f)
            return 8f;
        return cornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }
}