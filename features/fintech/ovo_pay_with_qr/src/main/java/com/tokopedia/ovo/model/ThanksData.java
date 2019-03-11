package com.tokopedia.ovo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ThanksData {
    @SerializedName("goalQRThanks")
    @Expose
    private GoalQRThanks goalQRThanks;

    public GoalQRThanks getGoalQRThanks() {
        return goalQRThanks;
    }
}
