package com.tokopedia.cacheapi.data.source.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.tokopedia.cacheapi.util.LoggingUtils;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by nathan on 9/29/17.
 */

public class CacheApiVersionCache {

    private static final String PREFERENCE_NAME = "cache";
    private static final String CACHE_API_VERSION = "CACHE_API_VERSION";

    private SharedPreferences sharedPreferences;
    private String versionName;

    public CacheApiVersionCache(Context context, String versionName) {
        this.versionName = versionName;
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, 0);
    }

    public Observable<Boolean> isWhiteListVersionUpdated() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String storedVersionName = sharedPreferences.getString(CACHE_API_VERSION, null);
                LoggingUtils.dumper(String.format("Current vs local version: %s - %s", storedVersionName, versionName));
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
                sharedPreferences.edit().putString(CACHE_API_VERSION, versionName);
                sharedPreferences.edit().commit();
                subscriber.onNext(true);
            }
        });
    }
}