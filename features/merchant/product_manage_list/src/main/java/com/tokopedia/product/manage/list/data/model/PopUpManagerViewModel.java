package com.tokopedia.product.manage.list.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PopUpManagerViewModel {
    @SerializedName("getShopManagerPopups")
    @Expose
    ShopManagerPopups shopManagerPopups;

    public ShopManagerPopups getShopManagerPopups() {
        return shopManagerPopups;
    }

    public void setShopManagerPopups(ShopManagerPopups shopManagerPopups) {
        this.shopManagerPopups = shopManagerPopups;
    }
}

