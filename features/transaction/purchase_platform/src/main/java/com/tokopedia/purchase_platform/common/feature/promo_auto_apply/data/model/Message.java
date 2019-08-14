
package com.tokopedia.purchase_platform.common.feature.promo_auto_apply.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("state")
    @Expose
    private String state = "";
    @SerializedName("color")
    @Expose
    private String color = "";
    @SerializedName("text")
    @Expose
    private String text = "";

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
