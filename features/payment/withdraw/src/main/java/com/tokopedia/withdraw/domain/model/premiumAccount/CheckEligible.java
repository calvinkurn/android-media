package com.tokopedia.withdraw.domain.model.premiumAccount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckEligible {

    @SerializedName("status")
    @Expose
    private long status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}