package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/21/18.
 */
public class DepositModel {

    @SerializedName("buyer_usable")
    @Expose
    private long buyerUsable;
    @SerializedName("seller_usable")
    @Expose
    private long sellerUsable;

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
