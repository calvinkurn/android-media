package com.tokopedia.shop.favourite.domain.model;

import android.text.TextUtils;

import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant;
import com.tokopedia.shop.common.constant.ShopParamApiConstant;

import java.util.HashMap;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopFavouriteRequestModel {

    private String shopId;
    private String userId;
    private String deviceId;
    private int page;
    private int perPage;

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
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
            hashMap.put(ShopParamApiConstant.KEYWORD, userId);
        }
        if (!TextUtils.isEmpty(deviceId)) {
            hashMap.put(ShopCommonParamApiConstant.DEVICE_ID, deviceId);
        }
        hashMap.put(ShopParamApiConstant.PAGE, String.valueOf(page));
        if (perPage > 0) {
            hashMap.put(ShopParamApiConstant.PER_PAGE, String.valueOf(perPage));
        }
        return hashMap;
    }
}
