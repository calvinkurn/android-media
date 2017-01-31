package com.tokopedia.tkpd.home.feed.data.factory;

import android.content.Context;

import com.tokopedia.core.base.common.service.ServiceV4;
import com.tokopedia.tkpd.home.feed.data.mapper.GetShopIdMapper;
import com.tokopedia.tkpd.home.feed.data.source.FavoritShopIdDataSource;
import com.tokopedia.tkpd.home.feed.data.source.cloud.CloudFavoriteShopIdDataSource;

/**
 * @author Kulomady on 12/9/16.
 */

public class HomeDataSourceFactory {

    private Context context;
    private ServiceV4 serviceV4;
    private GetShopIdMapper getShopIdMapper;

    public HomeDataSourceFactory(Context context,
                                 ServiceV4 serviceV4,
                                 GetShopIdMapper getShopIdMapper) {
        this.context = context;
        this.serviceV4 = serviceV4;
        this.getShopIdMapper = getShopIdMapper;
    }

    public FavoritShopIdDataSource createFavoriteShopIdDataSource() {
        return new CloudFavoriteShopIdDataSource(context, serviceV4, getShopIdMapper);
    }

}
