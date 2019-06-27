package com.tokopedia.tkpdreactnative.react;

import android.content.Context;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tokopedia.core.app.TkpdCoreRouter;

public class DeeplinkManager extends ReactContextBaseJavaModule {

    private Context context;

    public DeeplinkManager(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
    }

    @Override
    public String getName() {
        return "DeeplinkManager";
    }

    @ReactMethod
    public void openUrl(String applinks, String extra, Promise promise) {
        // Check if it's applink
        if (applinks.toLowerCase().contains("tokopedia://")) {
            if (!extra.isEmpty()) { // Check if extra params is not empty
                ((TkpdCoreRouter) context.getApplicationContext())
                        .actionApplink(this.getCurrentActivity(), applinks, extra);
            } else {
                ((TkpdCoreRouter) context.getApplicationContext())
                        .actionApplinkFromActivity(this.getCurrentActivity(), applinks);
            }
        } else { // Check if it's web url
            ((TkpdCoreRouter) context.getApplicationContext())
                    .actionOpenGeneralWebView(this.getCurrentActivity(), applinks);
        }
    }
}
