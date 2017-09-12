package com.tokopedia.posapp.react;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 * Created by okasurya on 8/28/17.
 */

public abstract class ReactTableChooserModule extends ReactContextBaseJavaModule {
    public ReactTableChooserModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @ReactMethod
    public void getProductTable(Promise promise) {
        promise.resolve(PosReactConst.CacheTable.PRODUCT);
    }

    @ReactMethod
    public void getCartTable(Promise promise) {
        promise.resolve(PosReactConst.CacheTable.CART);
    }

    @ReactMethod
    public void getBankTable(Promise promise) {
        promise.resolve(PosReactConst.CacheTable.BANK);
    }
}
