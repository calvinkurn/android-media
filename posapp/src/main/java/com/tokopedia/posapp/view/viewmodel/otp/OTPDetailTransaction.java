package com.tokopedia.posapp.view.viewmodel.otp;

/**
 * Created by okasurya on 10/10/17.
 */

public class OTPDetailTransaction {
    public String transactionId;
    public String signature;
    public String id;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
