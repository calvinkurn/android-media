package com.tokopedia.tkpd.home.feed.data.factory;

import android.content.Context;

import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.tkpd.home.feed.data.mapper.RecentProductMapperResult;
import com.tokopedia.tkpd.home.feed.data.source.cloud.CloudRecentProductDataSource;
import com.tokopedia.tkpd.home.feed.data.source.local.LocalRecentProductDataSource;
import com.tokopedia.tkpd.home.feed.data.source.local.dbManager.RecentProductDbManager;

/**
 * @author Kulomady on 12/9/16.
 */

public class RecentProductSourceFactory {

    private Context mContext;
    private MojitoService mMojitoService;
    private RecentProductDbManager mDbManager;
    private RecentProductMapperResult mMapperResult;

    public RecentProductSourceFactory(Context context,
                                      MojitoService mojitoService,
                                      RecentProductDbManager dbManager,
                                      RecentProductMapperResult mapperResult) {
        mContext = context;
        mMojitoService = mojitoService;
        mDbManager = dbManager;
        mMapperResult = mapperResult;

    }

    public CloudRecentProductDataSource createRecentProductDataSource() {

        return new CloudRecentProductDataSource(
                mContext, mDbManager, mMojitoService, mMapperResult);
    }

    public LocalRecentProductDataSource createRecentProductCacheDataStore() {
        return new LocalRecentProductDataSource(mDbManager, mMapperResult);
    }
}
