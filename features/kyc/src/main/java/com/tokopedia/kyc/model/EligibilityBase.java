package com.tokopedia.kyc.model;

import com.google.gson.annotations.SerializedName;

public class EligibilityBase {
    @SerializedName("goalKYCRequest")
    EligibilityCheckResponse goalKYCRequest;

    public EligibilityCheckResponse getGoalKYCRequest() {
        return goalKYCRequest;
    }

    public void setGoalKYCRequest(EligibilityCheckResponse goalKYCRequest) {
        this.goalKYCRequest = goalKYCRequest;
    }
}
