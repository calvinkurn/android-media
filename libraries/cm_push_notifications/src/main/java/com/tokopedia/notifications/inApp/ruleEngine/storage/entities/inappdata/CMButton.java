package com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata;

import android.arch.persistence.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author lalit.singh
 */
public class CMButton {
    @ColumnInfo(name = "text")
    @SerializedName("text")
    @Expose
    public String txt;
    @ColumnInfo(name = "clr")
    @SerializedName("clr")
    @Expose
    public String color;
    @ColumnInfo(name = "bgc")
    @SerializedName("bgc")
    @Expose
    public String bgColor;
    @ColumnInfo(name = "sz")
    @SerializedName("sz")
    @Expose
    public String size;
    @SerializedName("appLink")
    @ColumnInfo(name = "appLink")
    @Expose
    public String appLink;
    @ColumnInfo(name = "pd")
    @SerializedName("pd")
    @Expose
    public int padding;
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

    public CMButton() {

    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
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
        return cornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }
}