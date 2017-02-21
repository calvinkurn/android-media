package com.tokopedia.tkpd.home.feed.data.source.local;

import com.tokopedia.tkpd.home.feed.data.mapper.FeedMapper;
import com.tokopedia.core.base.common.dbManager.FeedDbManager;
import com.tokopedia.tkpd.home.feed.domain.model.Feed;

import rx.Observable;

/**
 * @author Kulomady on 12/30/16.
 */

public class LocalFeedDataSource {
    private final FeedDbManager feedDbManager;
    private final FeedMapper feedMapper;

    public LocalFeedDataSource(FeedDbManager feedDbManager, FeedMapper feedMapper) {
        this.feedDbManager = feedDbManager;
        this.feedMapper = feedMapper;
    }

    public Observable<Feed> getFeedCache() {
        if (feedDbManager.isExpired(System.currentTimeMillis())) {
            return Observable.empty();
        }
        return feedDbManager.getData().map(feedMapper);
    }
}
