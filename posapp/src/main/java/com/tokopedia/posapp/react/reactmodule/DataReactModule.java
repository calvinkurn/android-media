package com.tokopedia.posapp.react.reactmodule;

import android.content.Context;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.posapp.di.component.DaggerReactDataComponent;
import com.tokopedia.posapp.react.di.component.ReactDataComponent;
import com.tokopedia.posapp.react.datasource.ReactRepositoryImpl;

import javax.inject.Inject;

import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by okasurya on 8/25/17.
 */

public class DataReactModule extends ReactContextBaseJavaModule {
    @Inject
    ReactRepositoryImpl reactRepository;

    private Context context;

    public DataReactModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
        initInjection();
    }

    private void initInjection() {
        ReactDataComponent reactDataComponent = DaggerReactDataComponent
                .builder()
                .baseAppComponent(((BaseMainApplication) context.getApplicationContext()).getBaseAppComponent())
                .build();

        reactDataComponent.inject(this);
    }

    @Override
    public String getName() {
        return "PosCacheModule";
    }

    @ReactMethod
    public void getData(String tableName, String id, Promise promise) {
        reactRepository.getData(tableName, id)
                .subscribeOn(Schedulers.newThread())
                .subscribe(getDefaultSubscriber(promise));
    }

    @ReactMethod
    public void getDataList(String tableName, int offset, int limit, Promise promise) {
        reactRepository.getDataList(tableName, offset, limit)
                .subscribeOn(Schedulers.newThread())
                .subscribe(getDefaultSubscriber(promise));
    }

    @ReactMethod
    public void getDataAll(String tableName, final Promise promise) {
        reactRepository.getDataAll(tableName)
                .subscribeOn(Schedulers.newThread())
                .subscribe(getDefaultSubscriber(promise));
    }

    @ReactMethod
    public void deleteAll(String tableName, Promise promise) {
        reactRepository.deleteAll(tableName)
                .subscribeOn(Schedulers.newThread())
                .subscribe(getDefaultSubscriber(promise));
    }

    @ReactMethod
    public void deleteItem(String tableName, String id, final Promise promise) {
        reactRepository.deleteItem(tableName, id)
                .subscribeOn(Schedulers.newThread())
                .subscribe(getDefaultSubscriber(promise));
    }

    @ReactMethod
    public void update(String tableName, String data, final Promise promise) {
        reactRepository.update(tableName, data)
                .subscribeOn(Schedulers.newThread())
                .subscribe(getDefaultSubscriber(promise));
    }

    @ReactMethod
    public void insert(String tableName, String data, final Promise promise) {
        reactRepository.insert(tableName, data)
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
                e.printStackTrace();
                promise.reject(e);
            }

            @Override
            public void onNext(String s) {
                promise.resolve(s);
            }
        };
    }
}
