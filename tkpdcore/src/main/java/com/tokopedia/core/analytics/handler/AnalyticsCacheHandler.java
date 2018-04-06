package com.tokopedia.core.analytics.handler;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.pojo.UserDrawerData;

import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Herdi_WORK on 10.05.17.
 */

public class AnalyticsCacheHandler {

    private CompositeSubscription subscription;

    private GlobalCacheManager cacheManager;
    private static final String USER_ATTR = "USER_ATTR";
    private static final String ADS_ID = "AF_ADS_ID";

    public AnalyticsCacheHandler() {
        cacheManager = new GlobalCacheManager();
        subscription = new CompositeSubscription();
    }

    public void getUserAttrGraphQLCache(final GetUserDataListener listener) {

        Single<UserDrawerData> getData = Single.just(USER_ATTR)
                .map(new Func1<String, UserDrawerData>() {
                    @Override
                    public UserDrawerData call(String s) {
                        return cacheManager.getConvertObjData(USER_ATTR, UserDrawerData.class);
                    }
                });
        executor(getData, new SingleSubscriber<UserDrawerData>() {

            @Override
            public void onSuccess(UserDrawerData data) {
                listener.onSuccessGetUserAttr(data);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });

    }

    public String getAdsId() {
        return Single.just(ADS_ID)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return cacheManager.getValueString(s);
                    }
                }).toBlocking().value();
    }

    public boolean isAdsIdAvailable() {
        return Single.just(ADS_ID)
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return !TextUtils.isEmpty(cacheManager.getValueString(s));
                    }
                }).toBlocking().value();
    }

    public void setAdsId(String adsId) {

        Single<String> saveData = Single.just(adsId);
        executor(saveData, new SingleSubscriber<String>() {
            @Override
            public void onSuccess(String adsId) {
                cacheManager.setKey(ADS_ID);
                cacheManager.setValue(adsId);
                cacheManager.store();
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });

    }

    public void setUserDataGraphQLCache(UserDrawerData data) {
        Single<UserDrawerData> saveData = Single.just(data);
        executor(saveData, new SingleSubscriber<UserDrawerData>() {
            @Override
            public void onSuccess(UserDrawerData value) {

                cacheManager.setKey(USER_ATTR);
                cacheManager.setValue(CacheUtil.convertModelToString(value,
                        new TypeToken<UserDrawerData>() {
                        }.getType()));
                cacheManager.store();
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    private void executor(Single single, SingleSubscriber subscriber) {
        subscription.add(single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber));
    }


    public interface GetUserDataListener {
        void onSuccessGetUserAttr(UserDrawerData data);

        void onError(Throwable e);
    }
}