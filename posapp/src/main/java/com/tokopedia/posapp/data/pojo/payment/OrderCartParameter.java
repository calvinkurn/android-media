package com.tokopedia.posapp.data.pojo.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by okasurya on 10/11/17.
 */

public class OrderCartParameter {
    @SerializedName("details")
    @Expose
    private List<CartDetail> details;
    @SerializedName("shop_id")
    @Expose
    private int shopId;
    @SerializedName("addr_id")
    @Expose
    private int addressId;

    public List<CartDetail> getDetails() {
        return details;
    }

    public void setDetails(List<CartDetail> details) {
        this.details = details;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
