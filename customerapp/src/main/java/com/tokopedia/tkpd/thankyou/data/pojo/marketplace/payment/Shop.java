package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by okasurya on 1/19/18.
 */

public class Shop {
    @SerializedName("is_official")
    @Expose
    private boolean isOffical;
    @SerializedName("shop_id")
    @Expose
    private int shopId;
    @SerializedName("shop_name")
    @Expose
    private String shopName;

    public boolean isOffical() {
        return isOffical;
    }

    public void setOffical(boolean offical) {
        isOffical = offical;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
