package com.tokopedia.buyerorder.list.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TabData {

    @SerializedName("orderLabelCategoryList")
    @Expose
    List<OrderLabelList> orderLabelList;

    public List<OrderLabelList> getOrderLabelList() {
        return orderLabelList;
    }
}