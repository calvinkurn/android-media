package com.tokopedia.shop.common.data.source.cloud;

import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant;
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonApi;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;

import retrofit2.Response;
import rx.Observable;

/**
 * @author hendry on 4/4/17.
 */

public class ShopCommonCloudDataSource {

    private final ShopCommonApi shopApi;

    public ShopCommonCloudDataSource(ShopCommonApi shopApi) {
        this.shopApi = shopApi;
    }

    public Observable<Response<DataResponse<ShopInfo>>> getShopInfo(String shopId, String userId, String deviceId) {
        String osType = ShopCommonParamApiConstant.VALUE_OS_TYPE;
        return shopApi.getShopInfo(shopId, userId, osType, deviceId);
    }

    public Observable<Response<DataResponse<ShopInfo>>> getShopInfoByDomain(String shopDomain, String userId, String deviceId) {
        String osType = ShopCommonParamApiConstant.VALUE_OS_TYPE;
        return shopApi.getShopInfoByDomain(shopDomain, userId, osType, deviceId);
    }
}
