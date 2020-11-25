package com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author lalit.singh
 */
public class CMButton {

    @Expose
    @ColumnInfo(name = "text")
    @SerializedName("text")
    public String txt;

    @Expose
    @ColumnInfo(name = "clr")
    @SerializedName("clr")
    public String color;

    @Expose
    @ColumnInfo(name = "bgc")
    @SerializedName("bgc")
    public String bgColor;

    @Expose
    @ColumnInfo(name = "sz")
    @SerializedName("sz")
    public String size;

    @Expose
    @SerializedName("appLink")
    @ColumnInfo(name = "appLink")
    public String appLink;

    @Expose
    @ColumnInfo(name = "pd")
    @SerializedName("pd")
    public int padding;

    @Expose
    @ColumnInfo(name = "sc")
    @SerializedName("sc")
    public String strokeColor;

    @Expose
    @ColumnInfo(name = "sw")
    @SerializedName("sw")
    public int strokeWidth;

    @Expose
    @ColumnInfo(name = "rd")
    @SerializedName("rd")
    public float cornerRadius;

    @Expose
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private String id;

    @Expose
    @ColumnInfo(name = "unifyType")
    @SerializedName("unifyType")
    private String unifyType = "main";

    @Expose
    @ColumnInfo(name = "unifyVariant")
    @SerializedName("unifyVariant")
    private String unifyVariant = "filled";

    @Expose
    @ColumnInfo(name = "unifySize")
    @SerializedName("unifySize")
    private String unifySize = "medium";

    public CMButton() {}

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnifyType() {
        return unifyType;
    }

    public void setUnifyType(String unifyType) {
        this.unifyType = unifyType;
    }

    public String getUnifyVariant() {
        return unifyVariant;
    }

    public void setUnifyVariant(String unifyVariant) {
        this.unifyVariant = unifyVariant;
    }

    public String getUnifySize() {
        return unifySize;
    }

    public void setUnifySize(String unifySize) {
        this.unifySize = unifySize;
    }

}