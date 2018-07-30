package com.tokopedia.posapp.payment.otp.data.pojo.transaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 5/3/18.
 */

public class CheckTransactionRequest {
    @SerializedName("merchant_code")
    @Expose
    private String merchantCode;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
