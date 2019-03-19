package com.tokopedia.ovo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BarcodeResponseData {

    @SerializedName("goalQRInquiry")
    @Expose
    private GoalQRInquiry goalQRInquiry;

    public BarcodeResponseData(GoalQRInquiry goalQRInquiry) {
        this.goalQRInquiry = goalQRInquiry;
    }

    public GoalQRInquiry getGoalQRInquiry() {
        return goalQRInquiry;
    }

    public void setGoalQRInquiry(GoalQRInquiry goalQRInquiry) {
        this.goalQRInquiry = goalQRInquiry;
    }

}
