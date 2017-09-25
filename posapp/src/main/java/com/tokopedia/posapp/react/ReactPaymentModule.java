package com.tokopedia.posapp.react;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

/**
 * Created by okasurya on 9/22/17.
 */

public class ReactPaymentModule extends ReactContextBaseJavaModule {
    public ReactPaymentModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "PaymentModule";
    }

    public void pay(String data) {
        Log.d("POSAPP PAYMENT", data);
    }
}
