package com.tokopedia.tkpdreactnative.react;

import android.content.Context;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.container.GTMDataLayer;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeView;

import org.json.JSONObject;

import java.util.HashMap;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

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
        if(!extra.isEmpty()) {
            ((TkpdCoreRouter) context.getApplicationContext())
                    .actionApplink(this.getCurrentActivity(), appLinks, extra);
        } else {
            ((TkpdCoreRouter) context.getApplicationContext())
                    .actionApplink(this.getCurrentActivity(), appLinks);
        }
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


    @ReactMethod
    public void getCurrentDeviceId(Promise promise){
        if(context.getApplicationContext() instanceof AbstractionRouter) {
            promise.resolve( ((AbstractionRouter) context.getApplicationContext()).getSession().getDeviceId() );
        }
    }

    @ReactMethod
    public void setTitleToolbar(final String title, final Promise promise) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (getCurrentActivity() != null && getCurrentActivity() instanceof ReactNativeView) {
                    ((ReactNativeView) getCurrentActivity()).actionSetToolbarTitle(title);
                    promise.resolve("OK");
                } else {
                    promise.resolve("NOT OK");
                }

            }
        });

    }

    @ReactMethod
    public void getFlavor(Promise promise) {
        if (getCurrentActivity() != null && getCurrentActivity().getApplication() instanceof TkpdCoreRouter){
            promise.resolve(((TkpdCoreRouter) getCurrentActivity().getApplication()).getFlavor());
        } else {
            promise.resolve("");
        }
    }

    @ReactMethod
    public void sendTrackingEvent(ReadableMap dataLayer) {
        HashMap<String, Object> maps = dataLayer.toHashMap();
        TrackingUtils.eventTrackingEnhancedEcommerce(maps);
    }

    @ReactMethod
    public void getGraphQLRequestHeader(Promise promise) {
        promise.resolve(AuthUtil.getHeaderRequestReactNative(context));
    }
  
    @ReactMethod
    public void finish() {
        if(getCurrentActivity() != null) {
            getCurrentActivity().finish();
        }
    }

}
