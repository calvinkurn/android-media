package com.tokopedia.tkpd.home.feed.data.factory;

import android.content.Context;

import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.tkpd.home.feed.data.mapper.RecentProductMapper;
import com.tokopedia.tkpd.home.feed.data.source.cloud.CloudRecentProductDataSource;
import com.tokopedia.tkpd.home.feed.data.source.local.LocalRecentProductDataSource;
import com.tokopedia.core.base.common.dbManager.RecentProductDbManager;

/**
 * @author Kulomady on 12/9/16.
 */

public class RecentProductSourceFactory {

    private Context context;
    private MojitoService mojitoService;
    private RecentProductDbManager recentProductDbManager;
    private RecentProductMapper recentProductMapper;

    public RecentProductSourceFactory(Context context,
                                      MojitoService mojitoService,
                                      RecentProductDbManager dbManager,
                                      RecentProductMapper mapperResult) {
        this.context = context;
        this.mojitoService = mojitoService;
        recentProductDbManager = dbManager;
        recentProductMapper = mapperResult;

    }

    public CloudRecentProductDataSource createRecentProductDataSource() {
        return new CloudRecentProductDataSource(
                context, recentProductDbManager, mojitoService, recentProductMapper);
    }

    public LocalRecentProductDataSource createRecentProductCacheDataStore() {
        return new LocalRecentProductDataSource(recentProductDbManager, recentProductMapper);
    }
}
