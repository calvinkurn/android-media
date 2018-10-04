package com.tokopedia.posapp.react.reactmodule;

import android.content.Context;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tokopedia.posapp.PosApplication;
import com.tokopedia.posapp.react.datasource.ReactRepositoryImpl;
import com.tokopedia.posapp.react.di.component.DaggerReactDataComponent;
import com.tokopedia.posapp.react.di.component.ReactDataComponent;

import javax.inject.Inject;

import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by okasurya on 8/25/17.
 */

public class DataReactModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    @Inject
    ReactRepositoryImpl reactRepository;

    private Context context;
    private CompositeSubscription subscriptions;

    public DataReactModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
        initInjection();
        reactContext.addLifecycleEventListener(this);
    }

    private void initInjection() {
        ReactDataComponent reactDataComponent = DaggerReactDataComponent
                .builder()
                .posAppComponent(((PosApplication) context.getApplicationContext()).getPosAppComponent())
                .build();

        reactDataComponent.inject(this);
    }

    @Override
    public String getName() {
        return "PosCacheModule";
    }

    @ReactMethod
    public void getData(String tableName, String id, Promise promise) {
        subscriptions.add(reactRepository.getData(tableName, id)
                .subscribeOn(Schedulers.newThread())
                .subscribe(getDefaultSubscriber(promise)));
    }

    @ReactMethod
    public void getDataList(String tableName, int offset, int limit, Promise promise) {
        subscriptions.add(reactRepository.getDataList(tableName, offset, limit)
                .subscribeOn(Schedulers.newThread())
                .subscribe(getDefaultSubscriber(promise)));
    }

    @ReactMethod
    public void getDataAll(String tableName, final Promise promise) {
        subscriptions.add(reactRepository.getDataAll(tableName)
                .subscribeOn(Schedulers.newThread())
                .subscribe(getDefaultSubscriber(promise)));
    }

    @ReactMethod
    public void deleteAll(String tableName, Promise promise) {
        subscriptions.add(reactRepository.deleteAll(tableName)
                .subscribeOn(Schedulers.newThread())
                .subscribe(getDefaultSubscriber(promise)));
    }

    @ReactMethod
    public void deleteItem(String tableName, String id, final Promise promise) {
        subscriptions.add(reactRepository.deleteItem(tableName, id)
                .subscribeOn(Schedulers.newThread())
                .subscribe(getDefaultSubscriber(promise)));
    }

    @ReactMethod
    public void update(String tableName, String data, final Promise promise) {
        subscriptions.add(reactRepository.update(tableName, data)
                .subscribeOn(Schedulers.newThread())
                .subscribe(getDefaultSubscriber(promise)));
    }

    @ReactMethod
    public void insert(String tableName, String data, final Promise promise) {
        subscriptions.add(reactRepository.insert(tableName, data)
                .subscribeOn(Schedulers.newThread())
                .subscribe(getDefaultSubscriber(promise)));
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

    @Override
    public void onHostResume() {
        if (subscriptions == null || subscriptions.isUnsubscribed()) subscriptions = new CompositeSubscription();
    }

    @Override
    public void onHostPause() {
        if (subscriptions != null) subscriptions.unsubscribe();
    }

    @Override
    public void onHostDestroy() {

    }
}
