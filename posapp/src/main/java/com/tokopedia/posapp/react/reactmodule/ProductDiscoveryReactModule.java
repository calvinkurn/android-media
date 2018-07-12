package com.tokopedia.posapp.react.reactmodule;

import android.content.Context;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.gson.Gson;
import com.tokopedia.posapp.PosApplication;
import com.tokopedia.posapp.react.di.component.DaggerReactDataComponent;
import com.tokopedia.posapp.react.di.component.ReactDataComponent;
import com.tokopedia.posapp.react.datasource.cloud.ReactProductCloudSource;
import com.tokopedia.posapp.react.datasource.model.ProductSearchRequest;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by okasurya on 9/18/17.
 */

public class ProductDiscoveryReactModule extends ReactContextBaseJavaModule {
    @Inject
    ReactProductCloudSource reactProductDataSource;
    @Inject
    Gson gson;
    private Context context;

    public ProductDiscoveryReactModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
        initInjection();
    }

    private void initInjection() {
        ReactDataComponent daggerReactDataComponent = DaggerReactDataComponent.builder()
                .posAppComponent(((PosApplication) context.getApplicationContext()).getPosAppComponent())
                .build();

        daggerReactDataComponent.inject(this);
    }

    @Override
    public String getName() {
        return "ProductDiscoveryModule";
    }


    @ReactMethod
    public void search(String data, final Promise promise) {
        ProductSearchRequest request = gson.fromJson(data, ProductSearchRequest.class);
        reactProductDataSource.search(request)
                .subscribe(
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
