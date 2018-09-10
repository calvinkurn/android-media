
package com.tokopedia.home.account.analytics.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sales {

    @SerializedName("newOrder")
    @Expose
    private int salesNewOrder;
    @SerializedName("shippingStatus")
    @Expose
    private int salesShippingStatus;
    @SerializedName("shippingConfirm")
    @Expose
    private int salesShippingConfirm;

    public int getSalesNewOrder() {
        return salesNewOrder;
    }

    public void setSalesNewOrder(int salesNewOrder) {
        this.salesNewOrder = salesNewOrder;
    }

    public int getSalesShippingStatus() {
        return salesShippingStatus;
    }

    public void setSalesShippingStatus(int salesShippingStatus) {
        this.salesShippingStatus = salesShippingStatus;
    }

    public int getSalesShippingConfirm() {
        return salesShippingConfirm;
    }

    public void setSalesShippingConfirm(int salesShippingConfirm) {
        this.salesShippingConfirm = salesShippingConfirm;
    }

}
