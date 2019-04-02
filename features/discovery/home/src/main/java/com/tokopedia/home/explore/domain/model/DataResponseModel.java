package com.tokopedia.home.explore.domain.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by errysuprayogi on 2/2/18.
 */

public class DataResponseModel {

    @SerializedName("dynamicHomeIcon")
    private DynamicHomeIcon dynamicHomeIcon;
    @SerializedName("shopInfo")
    private ShopInfo shopInfo;

    public DynamicHomeIcon getDynamicHomeIcon() {
        return dynamicHomeIcon;
    }

    public void setDynamicHomeIcon(DynamicHomeIcon dynamicHomeIcon) {
        this.dynamicHomeIcon = dynamicHomeIcon;
    }

    public ShopInfo getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
    }

}
