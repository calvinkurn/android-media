package com.tokopedia.buyerorder.detail.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by baghira on 10/05/18.
 */

public class Status {
    @SerializedName("statusText")
    @Expose
    private String statusText;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusLabel")
    @Expose
    private String statusLabel;
    @SerializedName("iconUrl")
    @Expose
    private String iconUrl;
    @SerializedName("textColor")
    @Expose
    private String textColor;
    @SerializedName("backgroundColor")
    @Expose
    private String backgroundColor;
    @SerializedName("fontSize")
    @Expose
    private String fontSize;

    public Status(String statusText, String status, String statusLabel, String iconUrl, String textColor, String backgroundColor, String fontSize) {
        this.statusText = statusText;
        this.status = status;
        this.statusLabel = statusLabel;
        this.iconUrl = iconUrl;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.fontSize = fontSize;
    }

    public String statusText() {
        return statusText;
    }

    public String status() {
        return status;
    }

    public String statusLabel() {
        return statusLabel;
    }

    public String iconUrl() {
        return iconUrl;
    }

    public String textColor() {
        return textColor;
    }

    public String backgroundColor() {
        return backgroundColor;
    }

    public String fontSize() {
        return fontSize;
    }

    @Override
    public String toString() {
        return "[Status:{" +" "+
                "statusText=" + statusText +" "+
                "status=" + status + " "+
                "statusLabel=" + statusLabel + " "+
                "iconUrl=" + iconUrl + " "+
                "textColor=" + textColor + " "+
                "backgroundColor=" + backgroundColor + " "+
                "fontSize=" + fontSize
                + "}]";
    }
}
