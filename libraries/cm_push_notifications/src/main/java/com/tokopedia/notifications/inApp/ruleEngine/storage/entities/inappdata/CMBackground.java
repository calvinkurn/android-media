package com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata;

import android.arch.persistence.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author lalit.singh
 */
public class CMBackground {
    @ColumnInfo(name = "img")
    @SerializedName("img")
    @Expose
    public String img;
    @ColumnInfo(name = "clr")
    @SerializedName("clr")
    @Expose
    public String color;
    @ColumnInfo(name = "sc")
    @SerializedName("sc")
    @Expose
    public String strokeColor;
    @ColumnInfo(name = "sw")
    @SerializedName("sw")
    @Expose
    public int strokeWidth;
    @ColumnInfo(name = "rd")
    @SerializedName("rd")
    @Expose
    public float cornerRadius;

    public CMBackground(){

    }

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