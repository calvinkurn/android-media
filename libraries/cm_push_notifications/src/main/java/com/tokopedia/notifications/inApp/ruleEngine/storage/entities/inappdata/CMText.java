package com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata;

import android.arch.persistence.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author lalit.singh
 */
public class CMText {
    @ColumnInfo(name = "txt")
    @SerializedName("txt")
    @Expose
    public String txt;
    @ColumnInfo(name = "clr")
    @SerializedName("clr")
    @Expose
    public String color;
    @ColumnInfo(name = "sz")
    @SerializedName("sz")
    @Expose
    public String size;

    public CMText(){

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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
