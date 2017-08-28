package com.tokopedia.core.react;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tokopedia.core.react.di.ReactCacheDependencies;
import com.tokopedia.core.react.domain.ReactCacheRepository;

import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by okasurya on 8/25/17.
 */

public class ReactCacheModule extends ReactTableChooserModule {
    ReactCacheRepository reactCacheRepository;

    public ReactCacheModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactCacheRepository = new ReactCacheDependencies(reactContext).provideReactCacheRepository();
    }

    @Override
    public String getName() {
        return null;
    }

    @ReactMethod
    public void getData(String tableName, String id, final Promise promise) {
        reactCacheRepository.getData(tableName, id)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
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
                });
    }

    @ReactMethod
    public void getDataList(String tableName, int offset, int limit, final Promise promise) {
        reactCacheRepository.getDataList(tableName, offset, limit)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
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
                });
    }
}
