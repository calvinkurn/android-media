package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by meta on 31/07/18.
 */
public class ChatModel {

    @SerializedName("unreads")
    @Expose
    private String unreads = "";

    public String getUnreads() {
        return unreads;
    }

    public void setUnreads(String unreads) {
        this.unreads = unreads;
    }
}
