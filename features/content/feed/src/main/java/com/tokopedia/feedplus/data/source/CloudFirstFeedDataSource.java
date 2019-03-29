package com.tokopedia.feedplus.data.source;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.feedplus.data.api.FeedApi;
import com.tokopedia.feedplus.data.mapper.FeedListMapper;
import com.tokopedia.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.feedplus.data.source.cloud.CloudFeedDataSource;
import com.tokopedia.feedplus.data.source.local.LocalFeedDataSource;
import com.tokopedia.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by nisie on 7/3/17.
 */

public class CloudFirstFeedDataSource extends CloudFeedDataSource {

    public CloudFirstFeedDataSource(@ApplicationContext Context context,
                                    FeedApi feedApi,
                                    FeedListMapper feedListMapper,
                                    FeedResultMapper feedResultMapperCloud,
                                    CacheManager globalCacheManager) {
        super(context, feedApi, feedListMapper, feedResultMapperCloud, globalCacheManager);
    }

    public Observable<FeedResult> getFirstPageFeedsList(RequestParams requestParams) {
        return getFeedsList(requestParams)
                .doOnNext(saveToCache())
                .map(feedResultMapper);
    }

    private Action1<FeedDomain> saveToCache() {
        return dataFeedDomains -> {
            globalCacheManager.save(LocalFeedDataSource.KEY_FEED_PLUS, convertModelToString(dataFeedDomains,
                    new TypeToken<FeedDomain>() {
                    }.getType()), 0);
        };
    }

    private static String convertModelToString(Object obj, Type type) {
        Gson gson = new Gson();

        JsonElement element = gson.toJsonTree(obj, type);

        if (!element.isJsonObject()) {
            throw new RuntimeException();
        }

        return element.getAsJsonObject().toString();
    }


}
