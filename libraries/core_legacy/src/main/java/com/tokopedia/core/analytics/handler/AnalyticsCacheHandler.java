package com.tokopedia.core.analytics.handler;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.core.drawer2.data.pojo.UserData;

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

    private CacheManager cacheManager;
    private static final String USER_ATTR = "USER_ATTR";
    private static final String ADS_ID = "AF_ADS_ID";

    public AnalyticsCacheHandler(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        subscription = new CompositeSubscription();
    }

    public void getUserAttrGraphQLCache(final GetUserDataListener listener) {
        Single<UserData> getData = Single.just(USER_ATTR)
                .map(new Func1<String, UserData>() {
                    @Override
                    public UserData call(String s) {
                        return CacheUtil.convertStringToModel(cacheManager.get(USER_ATTR), new TypeToken<UserData>() {}.getType());
                    }
                });
        executor(getData, new SingleSubscriber<UserData>() {

            @Override
            public void onSuccess(UserData data) {
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
                        return cacheManager.get(s);
                    }
                }).toBlocking().value();
    }

    public boolean isAdsIdAvailable() {
        return Single.just(ADS_ID)
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return !TextUtils.isEmpty(cacheManager.get(s));
                    }
                }).toBlocking().value();
    }

    public void setAdsId(String adsId) {
        Single<String> saveData = Single.just(adsId);
        executor(saveData, new SingleSubscriber<String>() {
            @Override
            public void onSuccess(String adsId) {
                cacheManager.save(
                  ADS_ID, adsId, 0
                );
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });

    }

    public void setUserDataGraphQLCache(UserData data) {
        Single<UserData> saveData = Single.just(data);
        executor(saveData, new SingleSubscriber<UserData>() {
            @Override
            public void onSuccess(UserData value) {
                cacheManager.save(USER_ATTR, CacheUtil.convertModelToString(value,
                        new TypeToken<UserData>() {
                        }.getType()), 0);
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
        void onSuccessGetUserAttr(UserData data);

        void onError(Throwable e);
    }
}