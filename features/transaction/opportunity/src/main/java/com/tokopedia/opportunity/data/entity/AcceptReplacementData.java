package com.tokopedia.opportunity.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 3/3/17.
 */

@Deprecated
public class AcceptReplacementData {
    @SerializedName("status")
    private int isSuccess;

    @SerializedName("message")
    private String message;

    @SerializedName("order_id")
    private int orderId;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return getIsSuccess() == 1;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
