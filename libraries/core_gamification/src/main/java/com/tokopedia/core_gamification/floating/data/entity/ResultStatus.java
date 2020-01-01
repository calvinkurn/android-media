package com.tokopedia.core_gamification.floating.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultStatus {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private List<String> message = null;
    @SerializedName("reason")
    @Expose
    private String reason;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
