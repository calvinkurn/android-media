package com.tokopedia.shop.common.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant;
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonApi;
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonWSApi;
import com.tokopedia.shop.common.data.source.cloud.model.ShopFavourite;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.user.session.UserSessionInterface;

import retrofit2.Response;
import rx.Observable;

/**
 * @author hendry on 4/4/17.
 */

public class ShopCommonCloudDataSource {

    private final ShopCommonApi shopApi;
    private final ShopCommonWSApi shopCommonWS4Api;
    private final UserSessionInterface userSession;

    public ShopCommonCloudDataSource(ShopCommonApi shopApi, ShopCommonWSApi shopCommonWS4Api, UserSessionInterface userSession) {
        this.shopApi = shopApi;
        this.shopCommonWS4Api = shopCommonWS4Api;
        this.userSession = userSession;
    }

    public Observable<Response<DataResponse<ShopInfo>>> getShopInfo(String shopId) {
        String userId = userSession.getUserId();
        String osType = ShopCommonParamApiConstant.VALUE_OS_TYPE;
        String deviceId = userSession.getDeviceId();
        return shopApi.getShopInfo(shopId, userId, osType, deviceId);
    }

    public Observable<Response<DataResponse<ShopInfo>>> getShopInfoByDomain(String shopDomain) {
        String userId = userSession.getUserId();
        String osType = ShopCommonParamApiConstant.VALUE_OS_TYPE;
        String deviceId = userSession.getDeviceId();
        return shopApi.getShopInfoByDomain(shopDomain, userId, osType, deviceId);
    }

    public Observable<Response<DataResponse<ShopFavourite>>> toggleFavouriteShop(String shopId) {
        String userId = userSession.getUserId();
        String osType = ShopCommonParamApiConstant.VALUE_OS_TYPE;
        String deviceId = userSession.getDeviceId();
        return shopCommonWS4Api.toggleFavouriteShop(shopId, userId, osType, deviceId);
    }
}
