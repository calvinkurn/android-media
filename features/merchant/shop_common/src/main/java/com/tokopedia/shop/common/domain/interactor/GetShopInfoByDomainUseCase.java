package com.tokopedia.shop.common.domain.interactor;

import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.repository.ShopCommonRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by User on 9/8/2017.
 */

public class GetShopInfoByDomainUseCase extends UseCase<ShopInfo> {

    private static final String SHOP_DOMAIN = "SHOP_DOMAIN";
    private static final String USER_ID = "USER_ID";
    private static final String DEVICE_ID = "DEVICE_ID";

    private ShopCommonRepository shopRepository;

    public GetShopInfoByDomainUseCase(ShopCommonRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Override
    public Observable<ShopInfo> createObservable(RequestParams requestParams) {
        String shopDomain = requestParams.getString(SHOP_DOMAIN, "");
        String userId = requestParams.getString(USER_ID, "0");
        String deviceId = requestParams.getString(DEVICE_ID, "");
        return shopRepository.getShopInfoByDomain(shopDomain, userId, deviceId);
    }

    public static RequestParams createRequestParam(String shopDomain, String userId, String deviceId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_DOMAIN, shopDomain);
        requestParams.putString(USER_ID, userId);
        requestParams.putString(DEVICE_ID, deviceId);
        return requestParams;
    }
}
