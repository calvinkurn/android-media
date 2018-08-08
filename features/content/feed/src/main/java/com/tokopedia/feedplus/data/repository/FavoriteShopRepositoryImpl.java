package com.tokopedia.feedplus.data.repository;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.feedplus.data.factory.FavoriteShopFactory;
import com.tokopedia.usecase.RequestParams;

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
        TKPDMapParam<String, Object> mapParam = new TKPDMapParam<>();
        mapParam.putAll(requestParams.getParameters());
        return factory.createCloudDoFavoriteShop().favoriteShop(mapParam);
    }
}
