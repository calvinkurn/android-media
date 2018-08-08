
package com.tokopedia.core.network.entity.replacement.opportunitydata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("is_allow_manage_tx")
    @Expose
    private int isAllowManageTx;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("is_gold_shop")
    @Expose
    private int isGoldShop;
    @SerializedName("total_order_retry")
    @Expose
    private int totalOrderRetry;

    public int getIsAllowManageTx() {
        return isAllowManageTx;
    }

    public void setIsAllowManageTx(int isAllowManageTx) {
        this.isAllowManageTx = isAllowManageTx;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getIsGoldShop() {
        return isGoldShop;
    }

    public void setIsGoldShop(int isGoldShop) {
        this.isGoldShop = isGoldShop;
    }

    public int getTotalOrderRetry() {
        return totalOrderRetry;
    }

    public void setTotalOrderRetry(int totalOrderRetry) {
        this.totalOrderRetry = totalOrderRetry;
    }

}
