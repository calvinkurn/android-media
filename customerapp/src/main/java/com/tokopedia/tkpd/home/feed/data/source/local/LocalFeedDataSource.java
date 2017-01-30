package com.tokopedia.tkpd.home.feed.data.source.local;

import com.tokopedia.tkpd.home.feed.data.mapper.FeedMapperResult;
import com.tokopedia.tkpd.home.feed.data.source.local.dbManager.FeedDbManager;
import com.tokopedia.tkpd.home.feed.domain.model.Feed;

import rx.Observable;

/**
 * @author Kulomady on 12/30/16.
 */

public class LocalFeedDataSource {
    private final FeedDbManager mFeedDbManager;
    private final FeedMapperResult mFeedMapper;

    public LocalFeedDataSource(FeedDbManager feedDbManager, FeedMapperResult feedMapper) {
        mFeedDbManager = feedDbManager;
        mFeedMapper = feedMapper;
    }

    public Observable<Feed> getFeedCache() {
        if (mFeedDbManager.isExpired(System.currentTimeMillis())) {
            return Observable.empty();
        }
        return mFeedDbManager.getData().map(mFeedMapper);
    }
}
