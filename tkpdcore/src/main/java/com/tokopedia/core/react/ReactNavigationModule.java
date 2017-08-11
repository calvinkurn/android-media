package com.tokopedia.core.react;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.util.SessionHandler;

/**
 * @author ricoharisin .
 */

public class ReactNavigationModule extends ReactContextBaseJavaModule {
    private static final int LOGIN_REQUEST_CODE = 1005;
    private Promise mPickerPromise;

    private Context context;

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
            if (requestCode == LOGIN_REQUEST_CODE) {
                if (mPickerPromise != null) {
                    if (resultCode == Activity.RESULT_CANCELED) {
                        mPickerPromise.reject("");
                    } else if (resultCode == Activity.RESULT_OK) {
                        mPickerPromise.resolve(SessionHandler.getLoginID(context));
                    }
                    mPickerPromise = null;
                }
            }
        }
    };


    public ReactNavigationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
        reactContext.addActivityEventListener(mActivityEventListener);
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
    public void navigate(String appLinks, String mobileUrl, String extra) {
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
        Activity currentActivity = getCurrentActivity();

        if (currentActivity == null) {
            promise.reject("Activity doesn't exist");
            return;
        }

        mPickerPromise = promise;

        if (((IDigitalModuleRouter) context.getApplicationContext()).isSupportedDelegateDeepLink(Constants.Applinks.LOGIN)) {
            Intent intent = ((IDigitalModuleRouter) context.getApplicationContext()).getIntentDeepLinkHandlerActivity();
            intent.setData(Uri.parse(Constants.Applinks.LOGIN));
            currentActivity.startActivityForResult(intent, LOGIN_REQUEST_CODE);
        } else {
            promise.reject("Activity doesn't exist");
        }
    }
}
