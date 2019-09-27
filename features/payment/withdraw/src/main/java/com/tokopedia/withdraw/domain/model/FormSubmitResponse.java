package com.tokopedia.withdraw.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FormSubmitResponse {

    @SerializedName("message")
    List<String> message;
    @SerializedName("message_error")
    String messageError;
    @SerializedName("status")
    String status;

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
