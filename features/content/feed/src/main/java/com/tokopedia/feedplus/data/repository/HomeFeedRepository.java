package com.tokopedia.feedplus.data.repository;

import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by henrypriyono on 12/29/17.
 */

public interface HomeFeedRepository {
    Observable<FeedResult> getHomeFeeds(RequestParams requestParams);
}
