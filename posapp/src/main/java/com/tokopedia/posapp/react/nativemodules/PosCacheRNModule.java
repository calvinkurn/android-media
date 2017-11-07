package com.tokopedia.posapp.react.nativemodules;

import android.content.Context;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.posapp.di.component.DaggerReactCacheComponent;
import com.tokopedia.posapp.react.datasource.ReactCacheRepository;

import javax.inject.Inject;

import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by okasurya on 8/25/17.
 */

public class PosCacheRNModule extends ReactContextBaseJavaModule {
    @Inject
    ReactCacheRepository reactCacheRepository;

    private Context context;

    public PosCacheRNModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
        initInjection();
    }

    private void initInjection() {
        AppComponent appComponent = ((MainApplication) context.getApplicationContext()).getAppComponent();
        DaggerReactCacheComponent daggerReactCacheComponent =
                (DaggerReactCacheComponent) DaggerReactCacheComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerReactCacheComponent.inject(this);
    }

    @Override
    public String getName() {
        return "PosCacheModule";
    }

    @ReactMethod
    public void getData(String tableName, String id, Promise promise) {
        reactCacheRepository.getData(tableName, id)
                .subscribeOn(Schedulers.newThread())
                .subscribe(getDefaultSubscriber(promise));
    }

    @ReactMethod
    public void getDataList(String tableName, int offset, int limit, Promise promise) {
        reactCacheRepository.getDataList(tableName, offset, limit)
                .subscribeOn(Schedulers.newThread())
                .subscribe(getDefaultSubscriber(promise));
    }

    @ReactMethod
    public void getDataAll(String tableName, final Promise promise) {
        reactCacheRepository.getDataAll(tableName)
                .subscribeOn(Schedulers.newThread())
                .subscribe(getDefaultSubscriber(promise));
    }

    @ReactMethod
    public void deleteAll(String tableName, Promise promise) {
        reactCacheRepository.deleteAll(tableName)
                .subscribeOn(Schedulers.newThread())
                .subscribe(getDefaultSubscriber(promise));
    }

    @ReactMethod
    public void deleteItem(String tableName, String id, final Promise promise) {
        reactCacheRepository.deleteItem(tableName, id)
                .subscribeOn(Schedulers.newThread())
                .subscribe(getDefaultSubscriber(promise));
    }

    @ReactMethod
    public void update(String tableName, String data, final Promise promise) {
        reactCacheRepository.update(tableName, data)
                .subscribeOn(Schedulers.newThread())
                .subscribe(getDefaultSubscriber(promise));
    }

    @ReactMethod
    public void insert(String tableName, String data, final Promise promise) {
        reactCacheRepository.insert(tableName, data)
                .subscribeOn(Schedulers.newThread())
                .subscribe(getDefaultSubscriber(promise));
    }

    private Subscriber<? super String> getDefaultSubscriber(final Promise promise) {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                promise.reject(e);
            }

            @Override
            public void onNext(String s) {
                promise.resolve(s);
            }
        };
    }
}
