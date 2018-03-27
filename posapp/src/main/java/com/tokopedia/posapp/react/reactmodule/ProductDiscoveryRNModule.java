package com.tokopedia.posapp.react.reactmodule;

import android.content.Context;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.gson.Gson;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.posapp.di.component.DaggerReactCacheComponent;
import com.tokopedia.posapp.react.datasource.cloud.ReactProductCloudSource;
import com.tokopedia.posapp.react.datasource.model.ProductSearchRequest;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by okasurya on 9/18/17.
 */

public class ProductDiscoveryRNModule extends ReactContextBaseJavaModule {
    private Context context;

    @Inject
    ReactProductCloudSource reactProductCacheSource;

    @Inject
    Gson gson;

    public ProductDiscoveryRNModule(ReactApplicationContext reactContext) {
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
        return "ProductDiscoveryModule";
    }


    @ReactMethod
    public void search(String data, final Promise promise) {
        ProductSearchRequest request = gson.fromJson(data, ProductSearchRequest.class);
        reactProductCacheSource.search(request)
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
