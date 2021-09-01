package com.tokopedia.buyerorder.detail.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailsData {
    @SerializedName("orderDetails")
    @Expose

    private OrderDetails orderDetails;

    public OrderDetails  orderDetails() {
        return orderDetails;
    }
}
