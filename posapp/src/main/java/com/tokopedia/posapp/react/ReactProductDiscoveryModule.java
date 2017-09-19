package com.tokopedia.posapp.react;

import android.content.Context;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.tokopedia.posapp.react.datasource.cache.ReactProductCacheSource;

import rx.Subscriber;

/**
 * Created by okasurya on 9/18/17.
 */

public class ReactProductDiscoveryModule extends ReactContextBaseJavaModule {
    private Context context;
    private ReactProductCacheSource reactProductCacheSource;

    public ReactProductDiscoveryModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
    }

    @Override
    public String getName() {
        return "ProductDiscoveryModule";
    }

    public void searchProduct(String keyword, String etalaseId, int offset, int limit, final Promise promise) {
        reactProductCacheSource.searchProduct(keyword, etalaseId, offset, limit).subscribe(
                new Subscriber<String>() {
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
                }
        );
    }
}
