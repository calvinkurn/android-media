package com.tokopedia.core.network.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 7/6/2017.
 */

public class TextErrorObject {
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("type")
    @Expose
    private int type;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
