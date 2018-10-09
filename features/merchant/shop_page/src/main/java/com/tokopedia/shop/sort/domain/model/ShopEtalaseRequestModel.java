package com.tokopedia.shop.sort.domain.model;

import android.text.TextUtils;

import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant;

import java.util.HashMap;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalaseRequestModel {
    private String shopId;
    private String shopDomain;
    private static final String SHOW_ALL = "1";


    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }

    public HashMap<String, String> getHashMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        if (!TextUtils.isEmpty(shopId)) {
            hashMap.put(ShopCommonParamApiConstant.SHOP_ID, shopId);
        }
        if (!TextUtils.isEmpty(shopDomain)) {
            hashMap.put(ShopCommonParamApiConstant.SHOP_DOMAIN, shopDomain);
        }

        hashMap.put(ShopCommonParamApiConstant.SHOW_ALL, SHOW_ALL);

        return hashMap;
    }
}
