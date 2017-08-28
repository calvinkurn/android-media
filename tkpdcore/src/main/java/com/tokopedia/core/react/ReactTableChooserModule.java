package com.tokopedia.core.react;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tokopedia.core.react.data.Constants;

import dagger.Provides;

/**
 * Created by okasurya on 8/28/17.
 */

public abstract class ReactTableChooserModule extends ReactContextBaseJavaModule {
    public ReactTableChooserModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @ReactMethod
    public void getProductTable(Promise promise) {
        promise.resolve(Constants.Table.PRODUCT);
    }

    @ReactMethod
    public void getCartTable(Promise promise) {
        promise.resolve(Constants.Table.CART);
    }
}
