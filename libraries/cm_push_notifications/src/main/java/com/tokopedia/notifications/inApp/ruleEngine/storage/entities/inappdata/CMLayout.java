package com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.notifications.inApp.ruleEngine.storage.ButtonListConverter;
import com.tokopedia.notifications.inApp.viewEngine.CmInAppConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lalit.singh
 */
public class CMLayout {

    @Expose
    @Embedded(prefix = "bg_")
    @SerializedName("bg")
    public CMBackground foreground = new CMBackground();

    @Expose
    @Embedded(prefix = "ttl_")
    @SerializedName("ttl")
    public CMText titleText = new CMText();

    @Expose
    @Embedded(prefix = "msg_")
    @SerializedName("msg")
    public CMText messageText = new CMText();

    @Expose
    @ColumnInfo(name = "img")
    @SerializedName("img")
    public String img = "";

    @Expose
    @SerializedName("appLink")
    @ColumnInfo(name = "appLink")
    public String appLink = "";

    @Expose
    @ColumnInfo(name = "btnOri")
    @SerializedName("btnOri")
    public String btnOrientation = CmInAppConstant.ORIENTATION_VERTICAL;

    @Expose
    @ColumnInfo(name = "inAppButtons")
    @SerializedName("inAppButtons")
    public ArrayList<CMButton> button = new ArrayList<>();

    public CMLayout() {}

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