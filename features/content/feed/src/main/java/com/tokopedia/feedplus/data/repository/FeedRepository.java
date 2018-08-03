package com.tokopedia.feedplus.data.repository;

import com.tokopedia.feedplus.domain.model.CheckFeedDomain;
import com.tokopedia.feedplus.domain.model.FollowKolDomain;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.feedplus.domain.model.recentview.RecentViewProductDomain;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public interface FeedRepository {

    Observable<FeedResult> getFeedsFromCloud(RequestParams requestParams);

    Observable<FeedResult> getFirstPageFeedsFromCloud(RequestParams parameters);

    Observable<FeedResult> getFirstPageFeedsFromLocal();

    Observable<List<RecentViewProductDomain>> getRecentViewProduct(RequestParams requestParams);

    Observable<CheckFeedDomain> checkNewFeed(RequestParams parameters);

    Observable<FollowKolDomain> followUnfollowKol(RequestParams requestParams);

}
