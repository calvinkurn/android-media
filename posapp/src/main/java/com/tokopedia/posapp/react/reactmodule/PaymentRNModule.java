package com.tokopedia.posapp.react.reactmodule;

import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 * Created by okasurya on 9/22/17.
 */

public class PaymentRNModule extends ReactContextBaseJavaModule {
    public PaymentRNModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "PaymentModule";
    }

    @ReactMethod
    public void pay(String data, Promise promise) {
        Log.d("POSAPP PAYMENT", data);
        promise.resolve("");
    }
}
