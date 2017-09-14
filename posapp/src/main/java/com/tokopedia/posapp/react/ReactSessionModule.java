package com.tokopedia.posapp.react;

import android.content.Context;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tokopedia.core.util.SessionHandler;

/**
 * Created by okasurya on 9/14/17.
 */

public class ReactSessionModule extends ReactContextBaseJavaModule {
    private Context context;

    public ReactSessionModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
    }

    @Override
    public String getName() {
        return "SessionModule";
    }

    @ReactMethod
    public void getShopId(Promise promise) {
        promise.resolve(SessionHandler.getShopID(context));
    }

    @ReactMethod
    public void getShopDomain(Promise promise) {
        promise.resolve(SessionHandler.getShopDomain(context));
    }

    @ReactMethod
    public void getLoginName(Promise promise) {
        promise.resolve(SessionHandler.getLoginName(context));
    }

    @ReactMethod
    public void getLoginId(Promise promise) {
        promise.resolve(SessionHandler.getLoginID(context));
    }
}
