package com.tokopedia.tkpd.home.feed.data.factory;

import android.content.Context;

import com.tokopedia.core.base.common.service.AceService;
import com.tokopedia.tkpd.home.feed.data.mapper.FeedMapper;
import com.tokopedia.tkpd.home.feed.data.source.cloud.CloudFeedDataStore;
import com.tokopedia.tkpd.home.feed.data.source.local.LocalFeedDataSource;
import com.tokopedia.core.base.common.dbManager.FeedDbManager;

/**
 * @author Kulomady on 12/9/16.
 */

public class FeedDataSourceFactory {

    private Context context;
    private final AceService aceService;
    private FeedMapper feedMapper;
    private FeedDbManager feedDbManager;

    public FeedDataSourceFactory(Context context,
                                 AceService aceService,
                                 FeedMapper feedMapper, FeedDbManager feedDbManager) {
        this.context = context;
        this.aceService = aceService;
        this.feedMapper = feedMapper;
        this.feedDbManager = feedDbManager;
    }

    public CloudFeedDataStore createFeedDataSource() {

        return new CloudFeedDataStore(context, aceService, feedMapper, feedDbManager);
    }

    public LocalFeedDataSource createFeedCacheDataSource() {
        return new LocalFeedDataSource(feedDbManager, feedMapper);
    }

}
