package com.tokopedia.posapp.shop.data.source.cloud;

import com.tokopedia.posapp.shop.data.GetShopMapper;
import com.tokopedia.posapp.shop.domain.model.ShopDomain;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 8/3/17.
 */

public class ShopCloudSource {
    private ShopApi shopApi;
    private GetShopMapper getShopMapper;

    @Inject
    public ShopCloudSource(ShopApi shopApi,
                           GetShopMapper getShopMapper) {
        this.shopApi = shopApi;
        this.getShopMapper = getShopMapper;
    }

    public Observable<ShopDomain> getShop(RequestParams params) {
        return shopApi.getInfo(params.getParamsAllValueInString()).map(getShopMapper);
    }
}
