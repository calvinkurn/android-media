package com.tokopedia.posapp.product.management.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 4/11/18.
 */

public class OutletProperties {
    @SerializedName("outlet_id")
    @Expose
    private int outletId;
    @SerializedName("local_price")
    @Expose
    private double localPrice;
    @SerializedName("local_product_status")
    @Expose
    private int localProductStatus;

    public int getOutletId() {
        return outletId;
    }

    public void setOutletId(int outletId) {
        this.outletId = outletId;
    }

    public double getLocalPrice() {
        return localPrice;
    }

    public void setLocalPrice(double localPrice) {
        this.localPrice = localPrice;
    }

    public int getLocalProductStatus() {
        return localProductStatus;
    }

    public void setLocalProductStatus(int localProductStatus) {
        this.localProductStatus = localProductStatus;
    }
}
