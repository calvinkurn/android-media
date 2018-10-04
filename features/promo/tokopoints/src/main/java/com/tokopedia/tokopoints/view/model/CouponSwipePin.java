package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

public class CouponSwipePin {

    @SerializedName("need_pin")
    private boolean isPinRequire;

    @SerializedName("text")
    private boolean text;

    public boolean isPinRequire() {
        return isPinRequire;
    }

    public void setPinRequire(boolean pinRequire) {
        isPinRequire = pinRequire;
    }

    public boolean isText() {
        return text;
    }

    public void setText(boolean text) {
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
