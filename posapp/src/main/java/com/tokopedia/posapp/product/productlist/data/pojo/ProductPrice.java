package com.tokopedia.posapp.product.productlist.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 4/2/18.
 */

public class ProductPrice {
    @SerializedName("value_idr")
    @Expose
    private double value;
    @SerializedName("outlet_status")
    @Expose
    private int outletStatus;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getOutletStatus() {
        return outletStatus;
    }

    public void setOutletStatus(int outletStatus) {
        this.outletStatus = outletStatus;
    }
}