package com.tokopedia.posapp.react.nativemodules;

import android.content.Context;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.PosSessionHandler;

/**
 * Created by okasurya on 9/14/17.
 */

public class SessionRNModule extends ReactContextBaseJavaModule {
    private Context context;

    public SessionRNModule(ReactApplicationContext reactContext) {
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
    public void getShopName(Promise promise) {
        promise.resolve(SessionHandler.getShopName(context));
    }

    @ReactMethod
    public void getAddrId(Promise promise) {
        promise.resolve(PosSessionHandler.getOutletId(context));
    }

    @ReactMethod
    public void getAddressName(Promise promise) {
        promise.resolve(PosSessionHandler.getOutletName(context));
    }

    @ReactMethod
    public void getUserName(Promise promise) {
        promise.resolve(SessionHandler.getLoginName(context));
    }

    @ReactMethod
    public void getUserId(Promise promise) {
        promise.resolve(SessionHandler.getLoginID(context));
    }

    @ReactMethod
    @Deprecated
    public void getEnv(Promise promise) {
        if (getCurrentActivity() != null && getCurrentActivity().getApplication() instanceof TkpdCoreRouter){
            promise.resolve(((TkpdCoreRouter) getCurrentActivity().getApplication()).getFlavor());
        } else {
            promise.resolve("");
        }
    }

    @ReactMethod
    public void isAllowDebuggingTools(Promise promise) {
        promise.resolve(GlobalConfig.isAllowDebuggingTools());
    }
}
