package com.tokopedia.feedplus.data.repository;

import com.tokopedia.feedplus.domain.model.CheckFeedDomain;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public interface FeedRepository {

    Observable<FeedResult> getFeedsFromCloud(RequestParams requestParams);

    Observable<FeedResult> getFirstPageFeedsFromCloud(RequestParams parameters);

    Observable<FeedResult> getFirstPageFeedsFromLocal();

}
