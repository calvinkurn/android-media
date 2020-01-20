package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderInfoGraphql {
    @SerializedName("data")
    @Expose
    private List<OrderData> orderData;

    public List<OrderData> getOrderData() {
        return orderData;
    }

    public void setOrderData(List<OrderData> orderData) {
        this.orderData = orderData;
    }
}
