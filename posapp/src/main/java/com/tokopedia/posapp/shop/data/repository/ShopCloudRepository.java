package com.tokopedia.posapp.shop.data.repository;

import com.tokopedia.posapp.shop.data.source.cloud.ShopCloudSource;
import com.tokopedia.posapp.shop.domain.model.ShopDomain;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 8/3/17.
 */

public class ShopCloudRepository implements ShopRepository {
    private ShopCloudSource shopCloudSource;

    @Inject
    public ShopCloudRepository(ShopCloudSource shopCloudSource) {
        this.shopCloudSource = shopCloudSource;
    }

    @Override
    public Observable<ShopDomain> getShop(RequestParams requestParams) {
        return shopCloudSource.getShop(requestParams);
    }
}
