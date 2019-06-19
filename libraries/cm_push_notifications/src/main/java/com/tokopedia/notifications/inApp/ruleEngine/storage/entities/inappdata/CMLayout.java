package com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.notifications.inApp.ruleEngine.storage.ButtonListConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lalit.singh
 */
public class CMLayout {

    @Embedded(prefix = "bg_")
    @SerializedName("bg")
    @Expose
    public CMBackground foreground;

    @Embedded(prefix = "ttl_")
    @SerializedName("ttl")
    @Expose
    public CMText titleText;
    @Embedded(prefix = "msg_")
    @SerializedName("msg")
    @Expose
    public CMText messageText;
    @ColumnInfo(name = "img")
    @SerializedName("img")
    @Expose
    public String img;

    @SerializedName("appLink")
    @ColumnInfo(name = "appLink")
    @Expose
    public String appLink;

    @ColumnInfo(name = "btnOri")
    @SerializedName("btnOri")
    @Expose
    public String btnOrientation;

    @SerializedName("inAppButtons")
    @Expose
    public ArrayList<CMButton> button;// = new ArrayList<>();

    public CMLayout() {

    }


    public CMBackground getForeground() {
        return foreground;
    }

    public void setForeground(CMBackground foreground) {
        this.foreground = foreground;
    }

    public CMText getTitleText() {
        return titleText;
    }

    public void setTitleText(CMText titleText) {
        this.titleText = titleText;
    }

    public CMText getMessageText() {
        return messageText;
    }

    public void setMessageText(CMText messageText) {
        this.messageText = messageText;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<CMButton> getButton() {
        return button;
    }

    public void setButton(ArrayList<CMButton> button) {
        this.button = button;
    }

    public String getBtnOrientation() {
        return btnOrientation;
    }

    public void setBtnOrientation(String btnOrientation) {
        this.btnOrientation = btnOrientation;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }
}