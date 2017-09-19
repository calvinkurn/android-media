package com.tokopedia.posapp.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.data.factory.ShopFactory;
import com.tokopedia.posapp.domain.model.shop.ShopEtalaseDomain;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/19/17.
 */

public class EtalaseRepositoryImpl implements EtalaseRepository {
    ShopFactory shopFactory;

    public EtalaseRepositoryImpl(ShopFactory shopFactory) {
        this.shopFactory = shopFactory;
    }

    @Override
    public Observable<List<ShopEtalaseDomain>> getEtalase(RequestParams requestParams) {
        return shopFactory.cloud().getShopEtalase(requestParams);
    }
}
