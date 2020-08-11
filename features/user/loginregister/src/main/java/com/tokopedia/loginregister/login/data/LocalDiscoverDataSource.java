package com.tokopedia.loginregister.login.data;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.loginregister.login.view.model.DiscoverViewModel;

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
    private final CacheManager globalCacheManager;

    @Inject
    public LocalDiscoverDataSource(CacheManager globalCacheManager) {
        this.globalCacheManager = globalCacheManager;
    }

    public Observable<DiscoverViewModel> getDiscover(String source) {
        return Observable.just(KEY_DISCOVER + source)
                .map((Func1<String, DiscoverViewModel>) s -> {
                    if (getCache(s) != null) {
                        return CacheUtil.convertStringToModel(getCache(s),
                                new TypeToken<DiscoverViewModel>() {
                                }.getType());
                    } else {
                        throw new RuntimeException(CACHE_EXPIRED);
                    }
                })
                .first(discoverViewModel -> !discoverViewModel.getProviders().isEmpty()
                        && !TextUtils.isEmpty(discoverViewModel.getUrlBackground()));
    }

    private String getCache(String source) {
        return globalCacheManager.get(source);
    }
}
