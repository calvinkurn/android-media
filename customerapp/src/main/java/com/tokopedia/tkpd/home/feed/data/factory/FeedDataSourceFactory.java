package com.tokopedia.tkpd.home.feed.data.factory;

import android.content.Context;

import com.tokopedia.core.base.common.service.AceService;
import com.tokopedia.tkpd.home.feed.data.mapper.FeedMapperResult;
import com.tokopedia.tkpd.home.feed.data.source.cloud.CloudFeedDataStore;
import com.tokopedia.tkpd.home.feed.data.source.local.LocalFeedDataSource;
import com.tokopedia.tkpd.home.feed.data.source.local.dbManager.FeedDbManager;

/**
 * @author Kulomady on 12/9/16.
 */

public class FeedDataSourceFactory {

    private Context mContext;
    private final AceService mAceService;
    private FeedMapperResult mFeedMapperResult;
    private FeedDbManager mFeedDbManager;

    public FeedDataSourceFactory(Context context,
                                 AceService aceService,
                                 FeedMapperResult feedMapperResult, FeedDbManager feedDbManager) {
        mContext = context;
        mAceService = aceService;
        mFeedMapperResult = feedMapperResult;
        mFeedDbManager = feedDbManager;
    }

    public CloudFeedDataStore createFeedDataSource() {

        return new CloudFeedDataStore(mContext, mAceService, mFeedMapperResult, mFeedDbManager);
    }

    public LocalFeedDataSource createFeedCacheDataSource() {
        return new LocalFeedDataSource(mFeedDbManager, mFeedMapperResult);
    }

}
