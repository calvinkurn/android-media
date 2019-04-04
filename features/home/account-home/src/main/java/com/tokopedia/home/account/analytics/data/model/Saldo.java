
package com.tokopedia.home.account.analytics.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Saldo {

    @SerializedName("buyer_usable")
    @Expose
    private long buyerUsable = 0;
    @SerializedName("seller_usable")
    @Expose
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

    public long getDeposit() {
        return getBuyerUsable() + getSellerUsable();
    }
}
