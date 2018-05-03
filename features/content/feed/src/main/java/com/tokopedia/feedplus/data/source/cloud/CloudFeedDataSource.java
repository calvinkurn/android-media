package com.tokopedia.feedplus.data.source.cloud;

import com.apollographql.android.rx.RxApollo;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloWatcher;
import com.tkpdfeed.feeds.FeedQuery;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.feedplus.data.mapper.FeedListMapper;
import com.tokopedia.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.feedplus.domain.usecase.GetFeedsUseCase;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class CloudFeedDataSource {

    private static final int FEED_LIMIT = 3;
    private static final String FEED_SOURCE = "feeds";
    private ApolloClient apolloClient;
    private FeedListMapper feedListMapper;
    protected GlobalCacheManager globalCacheManager;
    protected FeedResultMapper feedResultMapper;

    public CloudFeedDataSource(ApolloClient apolloClient,
                               FeedListMapper feedListMapper,
                               FeedResultMapper feedResultMapper,
                               GlobalCacheManager globalCacheManager) {

        this.apolloClient = apolloClient;
        this.feedListMapper = feedListMapper;
        this.globalCacheManager = globalCacheManager;
        this.feedResultMapper = feedResultMapper;
    }

    public Observable<FeedResult> getNextPageFeedsList(RequestParams requestParams) {
        return getFeedsList(requestParams).map(feedResultMapper);
    }

    protected Observable<FeedDomain> getFeedsList(RequestParams requestParams) {
        String cursor = requestParams.getString(GetFeedsUseCase.PARAM_CURSOR, "");
        ApolloWatcher<FeedQuery.Data> apolloWatcher = apolloClient.newCall(FeedQuery.builder()
                .userID(requestParams.getInt(GetFeedsUseCase.PARAM_USER_ID, 0))
                .limit(FEED_LIMIT)
                .cursor(cursor)
                .source(FEED_SOURCE)
                .build()).watcher();
        return RxApollo.from(apolloWatcher).map(feedListMapper);
    }
}
