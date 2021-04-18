package com.tokopedia.loginregister.login.data;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.loginregister.login.view.model.DiscoverDataModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 10/10/17.
 */

public class LocalDiscoverDataSource {

    public static final String KEY_DISCOVER = "KEY_DISCOVER";
    private static final String CACHE_EXPIRED = "Cache has expired";
    public static final long CACHE_DURATION = 3600;
    private final CacheManager cacheManager;

    @Inject
    public LocalDiscoverDataSource(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public Observable<DiscoverDataModel> getDiscover(String source) {
        return Observable.just(KEY_DISCOVER + source)
                .map((Func1<String, DiscoverDataModel>) s -> {
                    if (getCache(s) != null) {
                        return CacheUtil.convertStringToModel(getCache(s),
                                new TypeToken<DiscoverDataModel>() {
                                }.getType());
                    } else {
                        throw new RuntimeException(CACHE_EXPIRED);
                    }
                })
                .first(discoverDataModel -> !discoverDataModel.getProviders().isEmpty()
                        && !TextUtils.isEmpty(discoverDataModel.getUrlBackground()));
    }

    private String getCache(String source) {
        return cacheManager.getString(source);
    }
}
