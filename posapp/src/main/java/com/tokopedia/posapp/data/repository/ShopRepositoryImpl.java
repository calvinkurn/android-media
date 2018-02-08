package com.tokopedia.posapp.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.data.factory.ShopFactory;
import com.tokopedia.posapp.domain.model.shop.ShopDomain;

import rx.Observable;

/**
 * Created by okasurya on 8/3/17.
 */

public class ShopRepositoryImpl implements ShopRepository {
    private ShopFactory shopFactory;

    public ShopRepositoryImpl(ShopFactory shopFactory) {
        this.shopFactory = shopFactory;
    }

    @Override
    public Observable<ShopDomain> getShop(RequestParams requestParams) {
        return shopFactory.cloud().getShop(requestParams);
    }
}
