package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/21/18.
 */
public class DepositModel {

    @SerializedName(value = "deposit", alternate = {"buyer_usable"})
    private long buyerUsable = 0;

    @SerializedName("seller_usable")
    private long sellerUsable = 0;

    public long getBuyerUsable() {
        return buyerUsable;
    }

    public void setBuyerUsable(long buyerUsable) {
        this.buyerUsable = buyerUsable;
    }

    public long getSellerUsable() {
        return sellerUsable;
    }

    public void setSellerUsable(long sellerUsable) {
        this.sellerUsable = sellerUsable;
    }

    public long getDepositLong() {
        return getBuyerUsable() + getSellerUsable();
    }

}
