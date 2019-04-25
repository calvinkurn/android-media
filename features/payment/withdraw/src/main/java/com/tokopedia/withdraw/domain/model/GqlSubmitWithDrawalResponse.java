package com.tokopedia.withdraw.domain.model;

import com.google.gson.annotations.SerializedName;

public class GqlSubmitWithDrawalResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("submitWithdrawal")
    private SubmitWithdrawResponse response;

    public SubmitWithdrawResponse getResponse() {
        return response;
    }

    public void setResponse(SubmitWithdrawResponse response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
