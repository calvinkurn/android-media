package com.tokopedia.feedplus.data.factory;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.feedplus.data.api.FeedApi;
import com.tokopedia.feedplus.data.mapper.CheckNewFeedMapper;
import com.tokopedia.feedplus.data.mapper.FeedListMapper;
import com.tokopedia.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.feedplus.data.mapper.RecentProductMapper;
import com.tokopedia.feedplus.data.source.CloudCheckNewFeedDataSource;
import com.tokopedia.feedplus.data.source.CloudFirstFeedDataSource;
import com.tokopedia.feedplus.data.source.cloud.CloudFeedDataSource;
import com.tokopedia.feedplus.data.source.cloud.CloudRecentProductDataSource;
import com.tokopedia.feedplus.data.source.local.LocalFeedDataSource;

/**
 * @author ricoharisin .
 */

public class FeedFactory {

    private final Context context;
    private final FeedApi feedApi;
    private final ApolloClient apolloClient;
    private final FeedListMapper feedListMapper;
    private final FeedResultMapper feedResultMapperLocal;
    private final FeedResultMapper feedResultMapperCloud;
    private final GlobalCacheManager globalCacheManager;
    private final MojitoService mojitoService;
    private final RecentProductMapper recentProductMapper;
    private final CheckNewFeedMapper checkNewFeedMapper;

    public FeedFactory(Context context, FeedApi feedApi, ApolloClient apolloClient,
                       FeedListMapper feedListMapper, FeedResultMapper feedResultMapperLocal,
                       FeedResultMapper feedResultMapperCloud,
                       GlobalCacheManager globalCacheManager,
                       MojitoService mojitoService,
                       RecentProductMapper recentProductMapper,
                       CheckNewFeedMapper checkNewFeedMapper) {
        this.context = context;
        this.feedApi = feedApi;
        this.apolloClient = apolloClient;
        this.feedListMapper = feedListMapper;
        this.feedResultMapperLocal = feedResultMapperLocal;
        this.feedResultMapperCloud = feedResultMapperCloud;
        this.globalCacheManager = globalCacheManager;
        this.mojitoService = mojitoService;
        this.recentProductMapper = recentProductMapper;
        this.checkNewFeedMapper = checkNewFeedMapper;
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

    public CloudRecentProductDataSource createCloudRecentViewedProductSource() {
        return new CloudRecentProductDataSource(globalCacheManager, mojitoService,
                recentProductMapper);
    }

    public CloudCheckNewFeedDataSource createCloudCheckNewFeedDataSource() {
        return new CloudCheckNewFeedDataSource(apolloClient,
                checkNewFeedMapper);
    }
}
