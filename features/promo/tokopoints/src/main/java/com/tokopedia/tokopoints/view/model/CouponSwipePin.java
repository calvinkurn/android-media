package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

public class CouponSwipePin {

    @SerializedName("need_pin")
    private boolean isPinRequire;

    @SerializedName("text")
    private String text;

    public boolean isPinRequire() {
        return isPinRequire;
    }

    public void setPinRequire(boolean pinRequire) {
        isPinRequire = pinRequire;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "CouponSwipePin{" +
                "isPinRequire=" + isPinRequire +
                ", text=" + text +
                '}';
    }
}
