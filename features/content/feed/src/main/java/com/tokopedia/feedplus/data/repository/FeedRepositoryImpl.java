package com.tokopedia.feedplus.data.repository;

import com.tokopedia.feedplus.data.factory.FeedFactory;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class FeedRepositoryImpl implements FeedRepository {

    private FeedFactory feedFactory;

    public FeedRepositoryImpl(FeedFactory feedFactory) {
        this.feedFactory = feedFactory;
    }

    @Override
    public Observable<FeedResult> getFeedsFromCloud(RequestParams requestParams) {
        return feedFactory.createCloudFeedDataSource().getNextPageFeedsList(requestParams);
    }

    @Override
    public Observable<FeedResult> getFirstPageFeedsFromCloud(RequestParams requestParams) {
        return feedFactory.createCloudFirstFeedDataSource().getFirstPageFeedsList(requestParams);
    }

    @Override
    public Observable<FeedResult> getFirstPageFeedsFromLocal() {
        return feedFactory.createLocalFeedDataSource().getFeeds();
    }

}
