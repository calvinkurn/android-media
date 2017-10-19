package com.tokopedia.core.cache.data.source.cache;

import android.text.TextUtils;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.cache.di.qualifier.ApiCacheQualifier;
import com.tokopedia.core.cache.di.qualifier.VersionNameQualifier;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by nathan on 9/29/17.
 */

public class CacheApiVersionCache {
    private static final String CACHE_API_VERSION = "CACHE_API_VERSION";

    private LocalCacheHandler localCacheHandler;
    private String versionName;

    @Inject
    public CacheApiVersionCache(@ApiCacheQualifier LocalCacheHandler localCacheHandler, @VersionNameQualifier String versionName) {
        this.versionName = versionName;
        this.localCacheHandler = localCacheHandler;
    }

    public Observable<Boolean> isWhiteListVersionUpdated() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String storedVersionName = localCacheHandler.getString(CACHE_API_VERSION);
                CommonUtils.dumper(String.format("Current vs local version: %s - %s", storedVersionName, versionName));
                // Fresh install or different version
                boolean whiteListVersionUpdated = TextUtils.isEmpty(storedVersionName) || !storedVersionName.equals(versionName);
                subscriber.onNext(whiteListVersionUpdated);
            }
        });

    }

    public Observable<Boolean> updateCacheWhiteListVersion() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                localCacheHandler.putString(CACHE_API_VERSION, versionName);
                localCacheHandler.applyEditor();
                subscriber.onNext(true);
            }
        });
    }
}