package com.tokopedia.shop.etalase.domain.model;

import android.text.TextUtils;

import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant;

import java.util.HashMap;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalaseRequestModel {

    private static final String SHOW_ALL = "1";

    private String shopId;
    private String userId;
    private String deviceId;

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public HashMap<String, String> getHashMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        if (!TextUtils.isEmpty(shopId)) {
            hashMap.put(ShopCommonParamApiConstant.SHOP_ID, shopId);
        }
        if (!TextUtils.isEmpty(userId)) {
            hashMap.put(ShopCommonParamApiConstant.USER_ID, userId);
        }
        if (!TextUtils.isEmpty(deviceId)) {
            hashMap.put(ShopCommonParamApiConstant.DEVICE_ID, deviceId);
        }
        hashMap.put(ShopCommonParamApiConstant.SHOW_ALL, SHOW_ALL);
        hashMap.put(ShopCommonParamApiConstant.OS_TYPE, ShopCommonParamApiConstant.VALUE_OS_TYPE);
        return hashMap;
    }
}