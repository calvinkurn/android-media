package com.tokopedia.posapp.react.reactmodule;

import android.net.Uri;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.gson.Gson;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.posapp.PosConstants;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

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
        promise.resolve(AuthUtil.calculateHmacSHA1(data, getSecretKey(url)));
    }

    @ReactMethod
    public void convertParamToJson(String param, Promise promise) {
        try {
            String mock = "http://www.tokopedia.com?" + URLDecoder.decode(param, "UTF-8");

            Uri uri = Uri.parse(mock).buildUpon().build();
            Map<String, String> maps = new HashMap<>();
            for(String key: uri.getQueryParameterNames()) {
                maps.put(key, uri.getQueryParameter(key));
            }
            promise.resolve(new Gson().toJson(maps));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            promise.reject(e);
        }
    }

    private String getSecretKey(String url) {
        if(url.equals(TkpdBaseURL.SCROOGE_DOMAIN + TkpdBaseURL.Payment.PATH_PAYMENT)) {
            return PosConstants.KEY_PAYMENT;
        }

        return AuthUtil.KEY.KEY_WSV4;
    }
}
