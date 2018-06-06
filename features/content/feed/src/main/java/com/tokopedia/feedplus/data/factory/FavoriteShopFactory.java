package com.tokopedia.feedplus.data.factory;

import android.content.Context;

import com.tokopedia.core.shopinfo.facades.authservices.ActionService;
import com.tokopedia.feedplus.data.source.cloud.FavoriteShopDataSource;
import com.tokopedia.feedplus.data.mapper.FavoriteShopMapper;

/**
 * Created by stevenfredian on 5/26/17.
 */

public class FavoriteShopFactory {
    private Context context;
    private FavoriteShopMapper mapper;
    private ActionService service;

    public FavoriteShopFactory(Context context, FavoriteShopMapper mapper, ActionService service) {
        this.context = context;
        this.mapper = mapper;
        this.service = service;
    }

    public FavoriteShopDataSource createCloudDoFavoriteShop() {
        return new FavoriteShopDataSource(context, service, mapper);
    }
}
