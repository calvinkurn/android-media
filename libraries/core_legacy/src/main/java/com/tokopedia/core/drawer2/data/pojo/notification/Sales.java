package com.tokopedia.core.drawer2.data.pojo.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 18/12/2015.
 */
public class Sales {
    private static final String TAG = Sales.class.getSimpleName();

    @SerializedName("sales_new_order")
    @Expose
    private int salesNewOrder;
    @SerializedName("sales_shipping_confirm")
    @Expose
    private int salesShippingConfirm;
    @SerializedName("sales_shipping_status")
    @Expose
    private int salesShippingStatus;

    public int getSalesNewOrder() {
        return salesNewOrder;
    }

    public void setSalesNewOrder(int salesNewOrder) {
        this.salesNewOrder = salesNewOrder;
    }

    public int getSalesShippingConfirm() {
        return salesShippingConfirm;
    }

    public void setSalesShippingConfirm(int salesShippingConfirm) {
        this.salesShippingConfirm = salesShippingConfirm;
    }

    public int getSalesShippingStatus() {
        return salesShippingStatus;
    }

    public void setSalesShippingStatus(int salesShippingStatus) {
        this.salesShippingStatus = salesShippingStatus;
    }
}
