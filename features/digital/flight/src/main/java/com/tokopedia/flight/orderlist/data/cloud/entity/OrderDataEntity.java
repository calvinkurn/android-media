package com.tokopedia.flight.orderlist.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 12/6/17.
 */

public class OrderDataEntity {
    @SerializedName("data")
    @Expose
    private List<OrderEntity> orders;

    public List<OrderEntity> getOrders() {
        return orders;
    }
}
