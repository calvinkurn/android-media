package com.tokopedia.contactus.orderquery.data;

import com.google.gson.annotations.SerializedName;

public class SubmitTicketResponse {
    @SerializedName("post_key")
    private String postKey;

    @SerializedName("payment")
    private Payment payment;

    @SerializedName("is_success")
    private int isSuccess;

    @SerializedName("order")
    private Order order;

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return
                "SubmitTicketResponse{" +
                        "post_key = '" + postKey + '\'' +
                        ",payment = '" + payment + '\'' +
                        ",is_success = '" + isSuccess + '\'' +
                        ",order = '" + order + '\'' +
                        "}";
    }
}