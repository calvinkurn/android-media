package com.tokopedia.feedplus.data.source.local;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;

import java.lang.reflect.Type;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class LocalFeedDataSource {

    public static final String KEY_FEED_PLUS = "FEED_PLUS";
    private CacheManager cacheManager;
    private FeedResultMapper feedResultMapper;

    public LocalFeedDataSource(CacheManager globalCacheManager,
                               FeedResultMapper feedResultMapper) {
        this.cacheManager = globalCacheManager;
        this.feedResultMapper = feedResultMapper;
    }

    public Observable<FeedResult> getFeeds() {

        return Observable.fromCallable(() -> {
            return LocalFeedDataSource.<FeedDomain>convertStringToModel(cacheManager.get(KEY_FEED_PLUS),
                    new TypeToken<FeedDomain>() {
                    }.getType());
        }).doOnNext(dataFeedDomains -> {
            if (dataFeedDomains.getListFeed() == null || dataFeedDomains.getListFeed().size() == 0) {
                throw new RuntimeException("No Data");
            }
        }).map(feedResultMapper);

    }

    private static <T> T convertStringToModel(String json, Type type) {
        Gson gson = new Gson();
        return (gson.fromJson(json, type));
    }

}
