package com.tokopedia.tkpd.beranda.data.source;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.etc.apis.home.CategoryApi;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.beranda.data.mapper.TickerMapper;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandsOfficialStoreResponseModel;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class TickerDataSource {

    private final Context context;
    private final CategoryApi categoryApi;
    private final TickerMapper tickerMapper;
    private final GlobalCacheManager cacheManager;
    private final Gson gson;

    public TickerDataSource(Context context, CategoryApi categoryApi, TickerMapper tickerMapper,
                            GlobalCacheManager cacheManager, Gson gson) {
        this.context = context;
        this.categoryApi = categoryApi;
        this.tickerMapper = tickerMapper;
        this.cacheManager = cacheManager;
        this.gson = gson;
    }

    public Observable<Ticker> getTicker() {
        return getCloud();
    }

    @NonNull
    private Observable<Ticker> getCloud() {
        return categoryApi.getTickers(SessionHandler.getLoginID(context),
                CategoryApi.size,
                CategoryApi.FILTER_ANDROID_DEVICE).map(tickerMapper).doOnNext(saveToCache());
    }

    private Action1<Ticker> saveToCache() {
        return new Action1<Ticker>() {
            @Override
            public void call(Ticker ticker) {
                cacheManager.setKey(TkpdCache.Key.HOME_TICKER_CACHE);
                cacheManager.setValue(gson.toJson(ticker));
                cacheManager.store();
            }
        };
    }

    public Observable<Ticker> getCache() {
        return Observable.just(true).map(new Func1<Boolean, Ticker>() {
            @Override
            public Ticker call(Boolean aBoolean) {
                String cache = cacheManager.getValueString(TkpdCache.Key.HOME_TICKER_CACHE);
                if (cache != null)
                    return gson.fromJson(cache, Ticker.class);
                throw new RuntimeException("Cache is empty!!");
            }
        }).onErrorResumeNext(getCloud());
    }
}
