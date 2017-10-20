package com.tokopedia.core.analytics.handler;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.anals.UserAttribute;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileData;

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
    private static final String USER_DATA = "USER_DATA";
    private static final String USER_ATTR = "USER_ATTR";
    private static final String ADS_ID = "AF_ADS_ID";

    public AnalyticsCacheHandler(){
        cacheManager = new GlobalCacheManager();
        subscription = new CompositeSubscription();
    }

    public void getUserDataCache(final GetUserDataListener listener){

        Single<String> getData = Single.just(USER_DATA);
        executor(getData, new SingleSubscriber<String>() {
            @Override
            public void onSuccess(String value) {
                listener.onSuccessGetUserData(cacheManager.getConvertObjData(USER_DATA, ProfileData.class));
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });

    }

    public void getUserAttrGraphQLCache(final GetUserDataListener listener){

            Single<String> getData = Single.just(USER_ATTR);
        executor(getData, new SingleSubscriber<String>() {
            @Override
            public void onSuccess(String value) {
                listener.onSuccessGetUserAttr(cacheManager.getConvertObjData(USER_ATTR, UserAttribute.Data.class));
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });

    }

    public String isUserDataCached(){
        return Single.just(USER_DATA)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return cacheManager.getValueString(s);
                    }
                }).toBlocking().value();
    }

    public String getAdsId(){
        return Single.just(ADS_ID)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return cacheManager.getValueString(s);
                    }
                }).toBlocking().value();
    }

    public boolean isAdsIdAvailable(){
        return Single.just(ADS_ID)
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return !TextUtils.isEmpty(cacheManager.getValueString(s));
                    }
                }).toBlocking().value();
    }

    public void setAdsId(String adsId){

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

    public void setUserDataCache(ProfileData userData){

        Single<ProfileData> saveData = Single.just(userData);
        executor(saveData, new SingleSubscriber<ProfileData>() {
            @Override
            public void onSuccess(ProfileData value) {
                cacheManager.setKey(USER_DATA);
                cacheManager.setValue(CacheUtil.convertModelToString(value,
                        new TypeToken<ProfileData>() {
                        }.getType()));
                cacheManager.store();
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });

    }

    public void setUserDataGraphQLCache(UserAttribute.Data data){

        Single<UserAttribute.Data> saveData = Single.just(data);
        executor(saveData, new SingleSubscriber<UserAttribute.Data>() {
            @Override
            public void onSuccess(UserAttribute.Data value) {

                cacheManager.setKey(USER_ATTR);
                cacheManager.setValue(CacheUtil.convertModelToString(value,
                        new TypeToken<UserAttribute.Data>() {
                        }.getType()));
                cacheManager.store();
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });

    }

    private void executor(Single single, SingleSubscriber subscriber){
        subscription.add(single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber));
    }


    public interface GetUserDataListener {
        void onSuccessGetUserData(ProfileData result);

        void onSuccessGetUserAttr(UserAttribute.Data data);

        void onError(Throwable e);
    }
}