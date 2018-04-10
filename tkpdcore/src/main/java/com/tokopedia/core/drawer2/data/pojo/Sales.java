
package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sales {

    @SerializedName("sales_new_order")
    @Expose
    private Integer salesNewOrder;
    @SerializedName("sales_shipping_status")
    @Expose
    private Integer salesShippingStatus;
    @SerializedName("sales_shipping_confirm")
    @Expose
    private Integer salesShippingConfirm;

    public Integer getSalesNewOrder() {
        return salesNewOrder;
    }

    public void setSalesNewOrder(Integer salesNewOrder) {
        this.salesNewOrder = salesNewOrder;
    }

    public Integer getSalesShippingStatus() {
        return salesShippingStatus;
    }

    public void setSalesShippingStatus(Integer salesShippingStatus) {
        this.salesShippingStatus = salesShippingStatus;
    }

    public Integer getSalesShippingConfirm() {
        return salesShippingConfirm;
    }

    public void setSalesShippingConfirm(Integer salesShippingConfirm) {
        this.salesShippingConfirm = salesShippingConfirm;
    }

}
