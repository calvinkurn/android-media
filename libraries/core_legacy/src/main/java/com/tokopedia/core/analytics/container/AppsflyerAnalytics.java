package com.tokopedia.core.analytics.container;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.tokopedia.analyticsdebugger.cassava.AnalyticsSource;
import com.tokopedia.analyticsdebugger.cassava.GtmLogger;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.device.info.DeviceInfo;
import com.tokopedia.track.interfaces.ContextAnalytics;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;


public class AppsflyerAnalytics extends ContextAnalytics {
    private static final String TAG = AppsflyerAnalytics.class.getSimpleName();
    private static boolean isAppsflyerCallbackHandled = false;
    private static final String KEY_INSTALL_SOURCE = "install_source";

    private static String deferredDeeplinkPath = "";

    public static final String ADVERTISINGID = "ADVERTISINGID";
    public static final String KEY_ADVERTISINGID = "KEY_ADVERTISINGID";

    public AppsflyerAnalytics(Context context) {
        super(context);
    }

    @Override
    public void initialize() {
        super.initialize();

        UserSessionInterface userSession = new UserSession(context);
        final String userID = userSession.isLoggedIn() ? userSession.getUserId() : "00000";


        Timber.d("Appsflyer login userid " + userID);

        try {
            Bundle bundle = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(),
                    PackageManager.GET_META_DATA).metaData;

            // suddenly hit appflyer init.
            if (getContext() instanceof TkpdCoreRouter) {
                TkpdCoreRouter context = (TkpdCoreRouter) getContext().getApplicationContext();
                context.onAppsFlyerInit();
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Timber.d("Error key Appsflyer");
        }
    }

    @Override
    public void sendAppsflyerRegisterEvent(String userId, String method) {
        Map<String, Object> eventVal = new HashMap<>();
        eventVal.put("custom_prop1", "registration");
        eventVal.put("os", "Android");
    }

    public void updateFCMToken(String fcmToken) {
    }

    @Override
    public void sendGeneralEvent(Map<String, Object> value) {
        // no op, only for GTM
    }

    @Override
    public void sendGeneralEvent(String event, String category, String action, String label) {
        // no op, only for GTM
    }

    @Override
    public void sendEnhanceEcommerceEvent(Map<String, Object> value) {
        // no op, only for GTM
    }

    @Override
    public void sendScreenAuthenticated(String screenName) {
        // no op, only for GTM
    }

    @Override
    public void sendScreenAuthenticated(String screenName, Map<String, String> customDimension) {
        // no op, only for GTM
    }

    @Override
    public void sendScreenAuthenticated(String screenName, String shopID, String shopType, String pageType, String productId) {
        // no op, only for GTM
    }

    public void sendEvent(String eventName, Map<String, Object> eventValue) {
        saveAppsFlyerEvent(eventName, eventValue);
    }

    //aliasing
    @Override
    public void sendTrackEvent(Map<String, Object> data, String eventName) {
        sendTrackEvent(eventName, data);
    }

    @Override
    public void sendTrackEvent(String eventName, Map<String, Object> eventValue) {
        saveAppsFlyerEvent(eventName, eventValue);
    }

    public void sendDeeplinkData(Activity activity) {
    }

    @Deprecated
    @Override
    public String getGoogleAdId() {
        return DeviceInfo.getAdsId(context);
    }

    public String getUniqueId() {
        return TrackingUtils.getClientID(getContext().getApplicationContext());
    }

    public void setUserID(String userID) {
        HashMap<String, Object> addData = new HashMap<>();
        addData.put(KEY_INSTALL_SOURCE, getContext().getPackageManager().getInstallerPackageName(
                getContext().getPackageName()));
        Timber.d(TAG + " appsflyer initiated with UID " + userID);
    }

    @Override
    public String getDefferedDeeplinkPathIfExists() {
        return deferredDeeplinkPath;
    }

    public void setDefferedDeeplinkPathIfExists(String deeplinkPath) {
        deferredDeeplinkPath = deeplinkPath;

    }

    public void saveAppsFlyerEvent(String eventName, Map<String, Object> eventValue) {
        if (!GlobalConfig.isAllowDebuggingTools()) {
            return;
        }

        try {
            GtmLogger.getInstance(getContext()).save(eventValue, eventName, AnalyticsSource.APPS_FLYER);
        } catch (Exception e) {
            Timber.e(e);
        }
    }
}
