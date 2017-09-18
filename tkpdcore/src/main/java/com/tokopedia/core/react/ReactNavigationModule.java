package com.tokopedia.core.react;

import android.content.Context;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.util.SessionHandler;

/**
 * @author ricoharisin .
 */

public class ReactNavigationModule extends ReactContextBaseJavaModule {
    private static final int LOGIN_REQUEST_CODE = 1005;

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
                .actionApplink(this.getCurrentActivity(), appLinks);
    }

    @ReactMethod
    public void navigateWithMobileUrl(String appLinks, String mobileUrl, String extra) {
        if (((IDigitalModuleRouter) context.getApplicationContext()).isSupportedDelegateDeepLink(appLinks)) {
            ((TkpdCoreRouter) context.getApplicationContext())
                    .actionApplink(this.getCurrentActivity(), appLinks);
        } else {
            ((TkpdCoreRouter) context.getApplicationContext())
                    .actionOpenGeneralWebView(this.getCurrentActivity(), mobileUrl);
        }
    }

    @ReactMethod
    public void navigateToLoginWithResult(Promise promise) {
        if (((IDigitalModuleRouter) context.getApplicationContext()).isSupportedDelegateDeepLink(Constants.Applinks.LOGIN)) {
            ((TkpdCoreRouter) context.getApplicationContext())
                    .actionApplink(this.getCurrentActivity(), Constants.Applinks.LOGIN);
        }
    }

    @ReactMethod
    public void getCurrentUserId(Promise promise) {
        promise.resolve(SessionHandler.getLoginID(context));
    }
}
