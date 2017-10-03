package com.tokopedia.posapp.react.nativemodules;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

/**
 * Created by okasurya on 10/3/17.
 */

public class UtilRNModule extends ReactContextBaseJavaModule {
    public UtilRNModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "UtilModule";
    }

    @ReactMethod
    public void generateHmac(String data, String url, Promise promise) {
        promise.resolve(AuthUtil.calculateRFC2104HMAC(data, getSecretKey(url)));
    }

    private String getSecretKey(String url) {
        if(url.equals(TkpdBaseURL.PAYMENT_DOMAIN + TkpdBaseURL.Payment.PATH_PAYMENT)) {
            return "PdsU23He0aJ828P1st8";
        }
        return "";
    }
}
