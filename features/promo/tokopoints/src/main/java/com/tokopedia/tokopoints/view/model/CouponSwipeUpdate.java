package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

public class CouponSwipeUpdate {

    @SerializedName("partnerCode")
    private String partnerCode;

    @SerializedName("note")
    private String note;

    @SerializedName("resultStatus")
    private ResultStatusEntity resultStatus;

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ResultStatusEntity getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatusEntity resultStatus) {
        this.resultStatus = resultStatus;
    }

    @Override
    public String toString() {
        return "CouponSwipeUpdate{" +
                "partnerCode='" + partnerCode + '\'' +
                ", note='" + note + '\'' +
                ", resultStatus=" + resultStatus +
                '}';
    }
}
