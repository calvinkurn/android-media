package com.tokopedia.posapp.domain.model.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by okasurya on 8/3/17.
 */

public class ShopDomain {
    private ShopInfoDomain shopInfo;

    public ShopInfoDomain getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfoDomain shopInfo) {
        this.shopInfo = shopInfo;
    }
}
