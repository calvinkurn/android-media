package com.tokopedia.posapp.react;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tokopedia.posapp.data.mapper.AddToCartMapper;
import com.tokopedia.posapp.data.source.local.CartLocalSource;

/**
 * Created by okasurya on 9/15/17.
 */

public class ReactCartModule extends ReactContextBaseJavaModule {
    public ReactCartModule(ReactApplicationContext reactContext) {
        super(reactContext);
        CartLocalSource cartLocalSource = new CartLocalSource();
    }

    @Override
    public String getName() {
        return "CartModule";
    }

    @ReactMethod
    public void increaseItem(String productId, int quantity, Promise promise) {

    }

    @ReactMethod
    public void decreaseItem(String productId, int quantity, Promise promise) {

    }

    @ReactMethod
    public void deleteAll(Promise promise) {

    }

    @ReactMethod
    public void delete(String productId, Promise promise) {

    }
}
