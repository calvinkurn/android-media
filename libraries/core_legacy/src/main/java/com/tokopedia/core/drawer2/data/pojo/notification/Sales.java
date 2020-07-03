package com.tokopedia.core.drawer2.data.pojo.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 18/12/2015.
 */
public class Sales {

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

    public int getSalesShippingConfirm() {
        return salesShippingConfirm;
    }

    public int getSalesShippingStatus() {
        return salesShippingStatus;
    }
}
