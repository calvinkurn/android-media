package com.tokopedia.tkpd.deeplink.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.tkpd.deeplink.data.factory.ShopInfoSourceFactory;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 1/5/18.
 */

public class ShopInfoRepositoryImpl implements ShopInfoRepository {
    private ShopInfoSourceFactory shopInfoSourceFactory;
    private SimpleDataResponseMapper<ShopModel> mapper;

    @Inject
    public ShopInfoRepositoryImpl(ShopInfoSourceFactory shopInfoSourceFactory) {
        this.shopInfoSourceFactory = shopInfoSourceFactory;
        this.mapper = new SimpleDataResponseMapper<>();
    }

    @Override
    public Observable<ShopModel> getShopInfo(RequestParams requestParams) {
        return shopInfoSourceFactory.cloud().getShopInfo(requestParams).map(mapper);
    }
}
