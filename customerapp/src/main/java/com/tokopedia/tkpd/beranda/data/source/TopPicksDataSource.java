package com.tokopedia.tkpd.beranda.data.source;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.ace.apis.SearchApi;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.beranda.data.mapper.TopPicksMapper;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksResponseModel;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;


/**
 * @author by errysuprayogi on 11/28/17.
 */

public class TopPicksDataSource {

    private final SearchApi searchApi;
    private final TopPicksMapper topPicksMapper;
    private final GlobalCacheManager cacheManager;
    private final Gson gson;

    public TopPicksDataSource(SearchApi searchApi, TopPicksMapper topPicksMapper, GlobalCacheManager cacheManager, Gson gson) {
        this.searchApi = searchApi;
        this.topPicksMapper = topPicksMapper;
        this.cacheManager = cacheManager;
        this.gson = gson;
    }

    public Observable<TopPicksResponseModel> getTopPicks(final RequestParams requestParams) {
        return getCloud(requestParams);
    }

    @NonNull
    private Observable<TopPicksResponseModel> getCloud(RequestParams requestParams) {
        return searchApi.getTopPicks(requestParams.getParamsAllValueInString(),
                GlobalConfig.VERSION_NAME, "android").map(topPicksMapper)
                .doOnNext(saveToCache());
    }

    private Action1<TopPicksResponseModel> saveToCache() {
        return new Action1<TopPicksResponseModel>() {
            @Override
            public void call(TopPicksResponseModel topPicksResponseModel) {
                cacheManager.setKey(TkpdCache.Key.HOME_TOP_PICK_CACHE);
                cacheManager.setValue(gson.toJson(topPicksResponseModel));
                cacheManager.store();
            }
        };
    }

    public Observable<TopPicksResponseModel> getCache() {
        return Observable.just(true).map(new Func1<Boolean, TopPicksResponseModel>() {
            @Override
            public TopPicksResponseModel call(Boolean aBoolean) {
                String cache = cacheManager.getValueString(TkpdCache.Key.HOME_TOP_PICK_CACHE);
                if (cache != null)
                    return gson.fromJson(cache, TopPicksResponseModel.class);
                throw new RuntimeException("Cache is empty!!");
            }
        });
    }
}
