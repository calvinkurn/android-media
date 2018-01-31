package com.tokopedia.abstraction.base.data.source.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import rx.Observable;
import rx.Subscriber;


/**
 * Created by nathan on 10/23/17.
 */

public abstract class DataCacheSource {

    private static final String PREF_NAME = "CACHE_DATA_LIST";

    private SharedPreferences sharedPrefs;

    public DataCacheSource(@ApplicationContext Context context) {
        sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    protected abstract String getPrefKeyName();

    protected abstract long getExpiredTimeInSec();

    public Observable<Boolean> isExpired() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(sharedPrefs.getLong(getPrefKeyName(), 0) + getExpiredTimeInSec() < (System.currentTimeMillis()/1000L));
            }
        });
    }

    public Observable<Boolean> updateExpiredTime() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                sharedPrefs.edit().putLong(getPrefKeyName(), System.currentTimeMillis() / 1000L).apply();
                subscriber.onNext(true);
            }
        });
    }

    public Observable<Boolean> setExpired() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                sharedPrefs.edit().putLong(getPrefKeyName(), 0).apply();
                subscriber.onNext(true);
            }
        });
    }
}