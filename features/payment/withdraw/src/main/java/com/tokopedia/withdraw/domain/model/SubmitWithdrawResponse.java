package com.tokopedia.withdraw.domain.model;

import com.google.gson.annotations.SerializedName;

public class SubmitWithdrawResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("message_error")
    private String messageError;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }
}
