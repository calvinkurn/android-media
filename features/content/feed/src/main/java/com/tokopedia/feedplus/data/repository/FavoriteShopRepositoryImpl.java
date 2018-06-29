package com.tokopedia.feedplus.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.feedplus.data.factory.FavoriteShopFactory;

import rx.Observable;

/**
 * Created by stevenfredian on 5/26/17.
 */

public class FavoriteShopRepositoryImpl implements FavoriteShopRepository{

    private FavoriteShopFactory factory;

    public FavoriteShopRepositoryImpl(FavoriteShopFactory factory) {
        this.factory = factory;
    }

    @Override
    public Observable<Boolean> doFavoriteShop(RequestParams requestParams){
        return factory.createCloudDoFavoriteShop().favoriteShop(requestParams.getParameters());
    }
}
