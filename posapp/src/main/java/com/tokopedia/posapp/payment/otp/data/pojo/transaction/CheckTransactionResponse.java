package com.tokopedia.posapp.payment.otp.data.pojo.transaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author okasurya on 5/3/18.
 */

public class CheckTransactionResponse {
    @SerializedName("message_error")
    @Expose
    private List<String> messageError;
    @SerializedName("transaction_status")
    @Expose
    private int transactionStatus;
    @SerializedName("data")
    @Expose
    private OrderDataResponse orderData;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message_status")
    @Expose
    private List<String> messageStatus;

    public List<String> getMessageError() {
        return messageError;
    }

    public void setMessageError(List<String> messageError) {
        this.messageError = messageError;
    }

    public int getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(int transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public OrderDataResponse getOrderData() {
        return orderData;
    }

    public void setOrderData(OrderDataResponse orderData) {
        this.orderData = orderData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(List<String> messageStatus) {
        this.messageStatus = messageStatus;
    }
}
