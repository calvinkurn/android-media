package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

public class CouponSwipeDetail {

    @SerializedName("need_swipe")
    private boolean needSwipe;

    @SerializedName("text")
    private String text;

    @SerializedName("note")
    private String note;

    @SerializedName("partner_code")
    private String partnerCode;

    @SerializedName("pin")
    private CouponSwipePin pin;

    public boolean isNeedSwipe() {
        return needSwipe;
    }

    public void setNeedSwipe(boolean needSwipe) {
        this.needSwipe = needSwipe;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public CouponSwipePin getPin() {
        return pin;
    }

    public void setPin(CouponSwipePin pin) {
        this.pin = pin;
    }

    @Override
    public String toString() {
        return "CouponSwipeDetail{" +
                "needSwipe=" + needSwipe +
                ", text='" + text + '\'' +
                ", note='" + note + '\'' +
                ", partnerCode='" + partnerCode + '\'' +
                ", pin=" + pin +
                '}';
    }
}
