package com.tokopedia.tkpd.manage.people.notification.interactor;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.tkpd.database.CacheUtil;
import com.tokopedia.tkpd.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.manage.people.notification.model.SettingNotification;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Nisie on 6/22/16.
 */
public class ManageNotificationCacheInteractorImpl implements ManageNotificationCacheInteractor {

    private static final String TAG = ManageNotificationCacheInteractorImpl.class.getSimpleName();
    private static final String CACHE_SETTING_NOTIFICATION = "CACHE_SETTING_NOTIFICATION";
    private GlobalCacheManager cacheManager;

    public ManageNotificationCacheInteractorImpl() {
        this.cacheManager = new GlobalCacheManager();
    }

    @Override
    public void getSettingNotificationCache(final SettingNotificationCacheListener listener) {
        Observable.just(CACHE_SETTING_NOTIFICATION)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<String, SettingNotification>() {
                    @Override
                    public SettingNotification call(String s) {
                        return cacheManager.getConvertObjData(s, SettingNotification.class);
                    }
                })
                .subscribe(new Subscriber<SettingNotification>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(SettingNotification result) {
                        try {
                            listener.onSuccess(result);
                        } catch (Exception e) {
                            listener.onError(e);
                        }
                    }
                });
    }

    @Override
    public void setSettingNotificationCache(SettingNotification result) {
        Observable.just(result)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<SettingNotification, Boolean>() {
                    @Override
                    public Boolean call(SettingNotification settingNotification) {
                        Log.i(TAG, "Start to storing the cache.....");

                        GlobalCacheManager cache = new GlobalCacheManager();

                        cache.setKey(CACHE_SETTING_NOTIFICATION);
                        cache.setValue(CacheUtil.convertModelToString(settingNotification,
                                new TypeToken<SettingNotification>() {
                                }.getType()));
                        cache.store();

                        Log.i(TAG, "End of storing the cache.....");

                        return true;
                    }
                })
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                });

    }

}
