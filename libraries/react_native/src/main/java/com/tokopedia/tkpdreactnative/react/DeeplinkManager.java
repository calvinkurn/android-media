package com.tokopedia.tkpdreactnative.react;

import android.content.Context;
import android.text.TextUtils;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;

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
        if (!TextUtils.isEmpty(applinks)) {
            if (applinks.toLowerCase().contains("tokopedia://")) {
                RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
                Boolean isNewRouteEnable = remoteConfig.getBoolean(RemoteConfigKey.CONFIG_ENABLE_NEW_ROUTE_REACT, true);
                if (!TextUtils.isEmpty(extra)) { // Check if extra params is not empty
                    ((TkpdCoreRouter) context.getApplicationContext())
                            .actionApplink(this.getCurrentActivity(), applinks, extra);
                } else {
                    if(isNewRouteEnable){
                        RouteManager.route(context, applinks);
                    }else {
                        ((TkpdCoreRouter) context.getApplicationContext())
                                .actionApplinkFromActivity(this.getCurrentActivity(), applinks);
                    }
                }
            } else { // Check if it's web url
                ((TkpdCoreRouter) context.getApplicationContext())
                        .actionOpenGeneralWebView(this.getCurrentActivity(), applinks);
            }
        }
    }
}
