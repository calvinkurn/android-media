package com.tokopedia.posapp.data.pojo.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by okasurya on 10/9/17.
 */

public class CheckoutDataResponse {
    @SerializedName("payment_amount")
    @Expose
    private double paymentAmount;

    @SerializedName("merchantCode")
    @Expose
    private String merchantCode;

    @SerializedName("profile_code")
    @Expose
    private String profileCode;

    @SerializedName("transaction_id")
    @Expose
    private int transactionId;

    @SerializedName("signature")
    @Expose
    private String signature;

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
