
package com.tokopedia.home.account.analytics.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stats {

    @SerializedName("__typename")
    @Expose
    private String typename = "";
    @SerializedName("shop_total_transaction")
    @Expose
    private String shopTotalTransaction = "";
    @SerializedName("shop_item_sold")
    @Expose
    private String shopItemSold = "";

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getShopTotalTransaction() {
        return shopTotalTransaction;
    }

    public void setShopTotalTransaction(String shopTotalTransaction) {
        this.shopTotalTransaction = shopTotalTransaction;
    }

    public String getShopItemSold() {
        return shopItemSold;
    }

    public void setShopItemSold(String shopItemSold) {
        this.shopItemSold = shopItemSold;
    }

}
