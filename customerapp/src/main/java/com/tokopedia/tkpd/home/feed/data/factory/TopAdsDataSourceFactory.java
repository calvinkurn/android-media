package com.tokopedia.tkpd.home.feed.data.factory;

import com.tokopedia.core.base.common.service.TopAdsService;
import com.tokopedia.tkpd.home.feed.data.mapper.TopAdsMapper;
import com.tokopedia.tkpd.home.feed.data.source.TopAdsDataSource;
import com.tokopedia.tkpd.home.feed.data.source.cloud.CloudTopAdsDataStore;
import com.tokopedia.tkpd.home.feed.data.source.local.LocalTopAdsDataStore;
import com.tokopedia.core.base.common.dbManager.TopAdsDbManager;

/**
 * @author Kulomady on 12/9/16.
 */

public class TopAdsDataSourceFactory {

    private TopAdsDbManager topAdsDbManager;
    private final TopAdsService topAdsService;
    private TopAdsMapper topAdsMapper;

    public TopAdsDataSourceFactory(TopAdsDbManager dbManager,
                                   TopAdsService topAdsService,
                                   TopAdsMapper topAdsMapper) {
        topAdsDbManager = dbManager;
        this.topAdsService = topAdsService;
        this.topAdsMapper = topAdsMapper;
    }

    public TopAdsDataSource createTopAdsDataSource() {
        return new CloudTopAdsDataStore(topAdsDbManager, topAdsService, topAdsMapper);
    }


    public TopAdsDataSource createTopAdsLocalDataStore() {
        return new LocalTopAdsDataStore(topAdsDbManager, topAdsMapper);
    }
}
