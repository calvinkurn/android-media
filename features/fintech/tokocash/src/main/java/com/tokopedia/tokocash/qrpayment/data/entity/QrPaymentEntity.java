package com.tokopedia.tokocash.qrpayment.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 1/3/18.
 */

public class QrPaymentEntity {

    @SerializedName("payment_id")
    @Expose
    private long paymentId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("transaction_id")
    @Expose
    private long transactionId;
    @SerializedName("datetime")
    @Expose
    private String dateTime;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("amount")
    @Expose
    private long amount;

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
