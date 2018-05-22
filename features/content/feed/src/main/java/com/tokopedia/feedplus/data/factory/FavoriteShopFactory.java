package com.tokopedia.feedplus.data.factory;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.shopinfo.facades.authservices.ActionService;
import com.tokopedia.feedplus.data.source.cloud.FavoriteShopDataSource;
import com.tokopedia.feedplus.data.mapper.FavoriteShopMapper;

import javax.inject.Inject;

/**
 * Created by stevenfredian on 5/26/17.
 */

public class FavoriteShopFactory {
    private Context context;
    private FavoriteShopMapper mapper;
    private ActionService service;

    @Inject
    public FavoriteShopFactory(@ApplicationContext Context context, FavoriteShopMapper mapper,
                               ActionService service) {
        this.context = context;
        this.mapper = mapper;
        this.service = service;
    }

    public FavoriteShopDataSource createCloudDoFavoriteShop() {
        return new FavoriteShopDataSource(context, service, mapper);
    }
}
