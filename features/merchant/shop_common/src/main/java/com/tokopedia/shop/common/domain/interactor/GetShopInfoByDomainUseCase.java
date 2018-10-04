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

    private ShopCommonRepository shopRepository;

    public GetShopInfoByDomainUseCase(ShopCommonRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Override
    public Observable<ShopInfo> createObservable(RequestParams requestParams) {
        String shopDomain = requestParams.getString(SHOP_DOMAIN, null);
        return shopRepository.getShopInfoByDomain(shopDomain);
    }

    public static RequestParams createRequestParam(String shopDomain) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_DOMAIN, shopDomain);
        return requestParams;
    }
}
