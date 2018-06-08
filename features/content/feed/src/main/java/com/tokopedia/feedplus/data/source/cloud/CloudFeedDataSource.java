package com.tokopedia.feedplus.data.source.cloud;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.feedplus.data.api.FeedApi;
import com.tokopedia.feedplus.data.mapper.FeedListMapper;
import com.tokopedia.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class CloudFeedDataSource {

    private static final int FEED_LIMIT = 3;
    private static final String FEED_SOURCE = "feeds";
    private final FeedApi feedApi;
    private final FeedListMapper feedListMapper;
    protected final GlobalCacheManager globalCacheManager;
    protected final FeedResultMapper feedResultMapper;

    public CloudFeedDataSource(@ApplicationContext Context context,
                               FeedApi feedApi,
                               FeedListMapper feedListMapper,
                               FeedResultMapper feedResultMapper,
                               GlobalCacheManager globalCacheManager) {

        this.feedApi = feedApi;
        this.feedListMapper = feedListMapper;
        this.globalCacheManager = globalCacheManager;
        this.feedResultMapper = feedResultMapper;
    }

    public Observable<FeedResult> getNextPageFeedsList(RequestParams requestParams) {
        return getFeedsList(requestParams).map(feedResultMapper);
    }

    protected Observable<FeedDomain> getFeedsList(RequestParams requestParams) {
        return feedApi.getFeedData(null).map(feedListMapper);
    }
}
