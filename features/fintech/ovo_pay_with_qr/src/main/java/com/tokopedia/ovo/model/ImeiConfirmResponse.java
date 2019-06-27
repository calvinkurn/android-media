package com.tokopedia.ovo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImeiConfirmResponse {
    @SerializedName("transfer_id")
    @Expose
    private int transferId;
    @SerializedName("transaction_id")
    @Expose
    private int transactionId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("pin_url")
    @Expose
    private String pinUrl;
    @SerializedName("errors")
    @Expose
    private List<Errors> errors;

    public int getTransactionId() {
        return transactionId;
    }

    public int getTransferId() {
        return transferId;
    }

    public String getStatus() {
        return status;
    }

    public String getPinUrl() {
        return pinUrl;
    }

    public List<Errors> getErrors() {
        return errors;
    }
}
