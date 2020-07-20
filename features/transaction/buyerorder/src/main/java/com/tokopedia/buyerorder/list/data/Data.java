package com.tokopedia.buyerorder.list.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("orders")
    @Expose
    List<Order> orders;

    public Data(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "[ orders : { " + orders + " } ]";
    }

    public List<Order> orders() {
        return orders;
    }
}