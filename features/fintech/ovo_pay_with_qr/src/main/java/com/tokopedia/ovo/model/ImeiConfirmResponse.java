package com.tokopedia.ovo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImeiConfirmResponse {
    @SerializedName("transaction_id")
    @Expose
    private int transactionId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("pin_url")
    @Expose
    private String pinUrl;

    public int getTransactionId() {
        return transactionId;
    }

    public String getStatus() {
        return status;
    }

    public String getPinUrl() {
        return pinUrl;
    }
}
