package com.tokopedia.tkpd.home.feed.data.factory;

import com.tokopedia.core.base.common.service.TopAdsService;
import com.tokopedia.tkpd.home.feed.data.mapper.TopAdsMapperResult;
import com.tokopedia.tkpd.home.feed.data.source.TopAdsDataSource;
import com.tokopedia.tkpd.home.feed.data.source.cloud.CloudTopAdsDataStore;
import com.tokopedia.tkpd.home.feed.data.source.local.LocalTopAdsDataStore;
import com.tokopedia.tkpd.home.feed.data.source.local.dbManager.TopAdsDbManager;

/**
 * @author Kulomady on 12/9/16.
 */

public class TopAdsDataSourceFactory {

    private TopAdsDbManager mDbManager;
    private final TopAdsService mTopAdsService;
    private TopAdsMapperResult mTopAdsMapperResult;

    public TopAdsDataSourceFactory(TopAdsDbManager dbManager,
                                   TopAdsService topAdsService,
                                   TopAdsMapperResult topAdsMapperResult) {
        mDbManager = dbManager;
        mTopAdsService = topAdsService;
        mTopAdsMapperResult = topAdsMapperResult;
    }

    public TopAdsDataSource createTopAdsDataSource() {
        return new CloudTopAdsDataStore(mDbManager, mTopAdsService, mTopAdsMapperResult);
    }


    public TopAdsDataSource createTopAdsLocalDataStore() {
        return new LocalTopAdsDataStore(mDbManager, mTopAdsMapperResult);
    }
}
