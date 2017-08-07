package com.tokopedia.core.react;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;

/**
 * @author ricoharisin .
 */

public class ReactNavigationModule extends ReactContextBaseJavaModule {

    private Context context;

    public ReactNavigationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
    }

    @Override
    public String getName() {
        return "NavigationModule";
    }

    @ReactMethod
    public void navigate(String appLinks, String extra) {
        ((TkpdCoreRouter) context.getApplicationContext())
                .delegateAppLink(this.getCurrentActivity(), appLinks);
    }
}
