package com.tokopedia.posapp.payment.otp.data.pojo.transaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 5/3/18.
 */

public class CheckTransactionResponse {
    @SerializedName("transaction_status")
    @Expose
    private int transactionStatus;
    @SerializedName("transaction_code")
    @Expose
    private String transactionCode;
    @SerializedName("transaction_message")
    @Expose
    private String transactionMessage;
    @SerializedName("order_data")
    @Expose
    private OrderDataResponse orderData;

    public int getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(int transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getTransactionMessage() {
        return transactionMessage;
    }

    public void setTransactionMessage(String transactionMessage) {
        this.transactionMessage = transactionMessage;
    }

    public OrderDataResponse getOrderData() {
        return orderData;
    }

    public void setOrderData(OrderDataResponse orderData) {
        this.orderData = orderData;
    }
}
