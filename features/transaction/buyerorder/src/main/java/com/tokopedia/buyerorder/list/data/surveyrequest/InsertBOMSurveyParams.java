package com.tokopedia.buyerorder.list.data.surveyrequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsertBOMSurveyParams {

    @SerializedName("rate")
    @Expose
    private int rating;

    @SerializedName("comments")
    @Expose
    private String comments;

    @SerializedName("device")
    @Expose
    private String deviceType;


    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
