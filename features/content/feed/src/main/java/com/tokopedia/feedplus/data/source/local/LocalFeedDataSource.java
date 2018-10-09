package com.tokopedia.feedplus.data.source.local;


import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author ricoharisin .
 */

public class LocalFeedDataSource {

    public static final String KEY_FEED_PLUS = "FEED_PLUS";
    private static final int CACHE_DURATION = 10;
    private GlobalCacheManager cacheManager;
    private FeedResultMapper feedResultMapper;

    public LocalFeedDataSource(GlobalCacheManager globalCacheManager,
                               FeedResultMapper feedResultMapper) {
        this.cacheManager = globalCacheManager;
        this.feedResultMapper = feedResultMapper;
    }

    public Observable<FeedResult> getFeeds() {

        return Observable.fromCallable(new Callable<FeedDomain>() {
            @Override
            public FeedDomain call() throws Exception {
                cacheManager = new GlobalCacheManager();
                cacheManager.setKey(KEY_FEED_PLUS);
                cacheManager.setCacheDuration(CACHE_DURATION);
                FeedDomain feedDomain = CacheUtil.convertStringToModel(cacheManager.getValueString(KEY_FEED_PLUS),
                        new TypeToken<FeedDomain>() {
                        }.getType());
                return feedDomain;
            }
        }).doOnNext(new Action1<FeedDomain>() {
            @Override
            public void call(FeedDomain dataFeedDomains) {
                if (dataFeedDomains.getListFeed() == null || dataFeedDomains.getListFeed().size() == 0) {
                    throw new RuntimeException("No Data");
                }
            }
        }).map(feedResultMapper);

    }
}
