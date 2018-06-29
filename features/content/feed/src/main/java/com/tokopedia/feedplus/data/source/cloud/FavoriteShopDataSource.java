package com.tokopedia.feedplus.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.shopinfo.facades.authservices.ActionService;
import com.tokopedia.feedplus.data.mapper.FavoriteShopMapper;

import rx.Observable;

/**
 * Created by stevenfredian on 5/26/17.
 */

public class FavoriteShopDataSource {

    Context context;
    ActionService service;
    FavoriteShopMapper mapper;

    public FavoriteShopDataSource(Context context, ActionService service, FavoriteShopMapper mapper) {
        this.context = context;
        this.service = service;
        this.mapper = mapper;
    }

    public Observable<Boolean> favoriteShop(TKPDMapParam<String, Object> parameters) {
        return service.getApi().actionFavoriteShop(parameters).map(mapper);
    }
}
