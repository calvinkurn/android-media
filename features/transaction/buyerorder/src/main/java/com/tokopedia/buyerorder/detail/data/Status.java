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
    @SerializedName("textColor")
    @Expose
    private String textColor;
    @SerializedName("backgroundColor")
    @Expose
    private String backgroundColor;

    public String statusText() {
        return statusText;
    }

    public String status() {
        return status;
    }

    public String statusLabel() {
        return statusLabel;
    }

    public String textColor() {
        return textColor;
    }

    public String backgroundColor() {
        return backgroundColor;
    }

    @Override
    public String toString() {
        return "[Status:{" +" "+
                "statusText=" + statusText +" "+
                "status=" + status + " "+
                "statusLabel=" + statusLabel + " "+
                "textColor=" + textColor + " "+
                "backgroundColor=" + backgroundColor
                + "}]";
    }
}
