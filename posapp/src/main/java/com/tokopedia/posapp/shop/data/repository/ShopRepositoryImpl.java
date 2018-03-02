package com.tokopedia.posapp.shop.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.shop.data.source.cloud.ShopCloudSource;
import com.tokopedia.posapp.shop.domain.model.ShopDomain;

import rx.Observable;

/**
 * Created by okasurya on 8/3/17.
 */

public class ShopRepositoryImpl implements ShopRepository {
    private ShopCloudSource shopCloudSource;

    public ShopRepositoryImpl(ShopCloudSource shopCloudSource) {
        this.shopCloudSource = shopCloudSource;
    }

    @Override
    public Observable<ShopDomain> getShop(RequestParams requestParams) {
        return shopCloudSource.getShop(requestParams);
    }
}
