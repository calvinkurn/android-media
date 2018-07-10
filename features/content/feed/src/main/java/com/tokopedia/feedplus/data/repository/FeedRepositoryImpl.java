package com.tokopedia.feedplus.data.repository;

import com.tokopedia.feedplus.data.factory.FeedFactory;
import com.tokopedia.feedplus.data.source.KolSource;
import com.tokopedia.feedplus.domain.model.CheckFeedDomain;
import com.tokopedia.feedplus.domain.model.FollowKolDomain;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.feedplus.domain.model.feeddetail.DataFeedDetailDomain;
import com.tokopedia.feedplus.domain.model.recentview.RecentViewProductDomain;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class FeedRepositoryImpl implements FeedRepository {

    private final KolSource kolSource;
    private FeedFactory feedFactory;

    public FeedRepositoryImpl(FeedFactory feedFactory,
                              KolSource kolSource) {
        this.feedFactory = feedFactory;
        this.kolSource = kolSource;
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

    @Override
    public Observable<List<DataFeedDetailDomain>> getFeedsDetail(RequestParams requestParams) {
        return feedFactory.createCloudDetailFeedDataSource().getFeedsDetailList(requestParams);
    }

    @Override
    public Observable<List<RecentViewProductDomain>> getRecentViewProduct(RequestParams requestParams) {
        return feedFactory.createCloudRecentViewedProductSource().getRecentProduct(requestParams);
    }

    @Override
    public Observable<CheckFeedDomain> checkNewFeed(RequestParams parameters) {
        return feedFactory.createCloudCheckNewFeedDataSource().checkNewFeed(parameters);
    }

    @Override
    public Observable<FollowKolDomain> followUnfollowKol(RequestParams requestParams) {
        return kolSource.followKolPost(requestParams);
    }

}
