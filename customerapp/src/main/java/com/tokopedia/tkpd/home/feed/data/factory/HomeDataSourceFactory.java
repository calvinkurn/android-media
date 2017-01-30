package com.tokopedia.tkpd.home.feed.data.factory;

import android.content.Context;

import com.tokopedia.core.base.common.service.ServiceVersion4;
import com.tokopedia.tkpd.home.feed.data.mapper.GetShopIdMapperResult;
import com.tokopedia.tkpd.home.feed.data.source.FavoritShopIdDataSource;
import com.tokopedia.tkpd.home.feed.data.source.cloud.CloudFavoriteShopIdDataSource;

/**
 * @author Kulomady on 12/9/16.
 */

public class HomeDataSourceFactory {

    private Context mContext;
    private ServiceVersion4 mServiceVersion4;
    private GetShopIdMapperResult mGetShopIdMapperResult;

    public HomeDataSourceFactory(Context context,
                                 ServiceVersion4 serviceVersion4,
                                 GetShopIdMapperResult getShopIdMapperResult) {
        mContext = context;
        mServiceVersion4 = serviceVersion4;
        mGetShopIdMapperResult = getShopIdMapperResult;
    }

    public FavoritShopIdDataSource createFavoriteShopIdDataSource() {
        return new CloudFavoriteShopIdDataSource(mContext, mServiceVersion4, mGetShopIdMapperResult);
    }

}
