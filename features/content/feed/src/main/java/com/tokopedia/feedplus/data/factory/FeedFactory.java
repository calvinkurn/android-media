package com.tokopedia.feedplus.data.factory;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.feedplus.data.api.FeedApi;
import com.tokopedia.feedplus.data.mapper.FeedListMapper;
import com.tokopedia.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.feedplus.data.source.CloudFirstFeedDataSource;
import com.tokopedia.feedplus.data.source.cloud.CloudFeedDataSource;
import com.tokopedia.feedplus.data.source.local.LocalFeedDataSource;

/**
 * @author ricoharisin .
 */

public class FeedFactory {

    private final Context context;
    private final FeedApi feedApi;
    private final FeedListMapper feedListMapper;
    private final FeedResultMapper feedResultMapperLocal;
    private final FeedResultMapper feedResultMapperCloud;
    private final CacheManager globalCacheManager;

    public FeedFactory(Context context, FeedApi feedApi,
                       FeedListMapper feedListMapper, FeedResultMapper feedResultMapperLocal,
                       FeedResultMapper feedResultMapperCloud,
                       CacheManager globalCacheManager) {
        this.context = context;
        this.feedApi = feedApi;
        this.feedListMapper = feedListMapper;
        this.feedResultMapperLocal = feedResultMapperLocal;
        this.feedResultMapperCloud = feedResultMapperCloud;
        this.globalCacheManager = globalCacheManager;
    }

    public CloudFeedDataSource createCloudFeedDataSource() {
        return new CloudFeedDataSource(context, feedApi, feedListMapper, feedResultMapperCloud,
                globalCacheManager);
    }

    public LocalFeedDataSource createLocalFeedDataSource() {
        return new LocalFeedDataSource(globalCacheManager, feedResultMapperLocal);
    }

    public CloudFirstFeedDataSource createCloudFirstFeedDataSource() {
        return new CloudFirstFeedDataSource(context, feedApi, feedListMapper, feedResultMapperCloud,
                globalCacheManager);
    }

}
