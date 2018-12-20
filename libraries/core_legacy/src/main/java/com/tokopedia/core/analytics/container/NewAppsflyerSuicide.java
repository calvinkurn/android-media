package com.tokopedia.core.analytics.container;

import android.app.Activity;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.tkpd.library.utils.legacy.CommonUtils;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.track.interfaces.ContextAnalytics;

import java.util.HashMap;
import java.util.Map;

/**
 * butuh userId
 */
public class NewAppsflyerSuicide extends ContextAnalytics {
    private static final String TAG = NewAppsflyerSuicide.class.getSimpleName();
    private static boolean isAppsflyerCallbackHandled = false;
    public static final String APPSFLYER_KEY = "SdSopxGtYr9yK8QEjFVHXL";
    private static final String KEY_INSTALL_SOURCE = "install_source";
    public static final String GCM_PROJECT_NUMBER = "692092518182";

    public NewAppsflyerSuicide(Context context) {
        super(context);
    }

    @Override
    public void initialize() {
        super.initialize();

        /**
         * TODO what if object that store from other application store here?
         */
        String userID = RouterUtils.getRouterFromContext(getContext())
                .legacySessionHandler().getUserId();

        CommonUtils.dumper("Appsflyer login userid " + userID);

        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {
            @Override
            public void onInstallConversionDataLoaded(Map<String, String> conversionData) {
                if (isAppsflyerCallbackHandled) return;
                isAppsflyerCallbackHandled = true;

                try {
                    //get first launch and deeplink
                    String isFirstLaunch = conversionData.get("is_first_launch");
                    String deeplink = conversionData.get("af_dp");

                    if (!TextUtils.isEmpty(isFirstLaunch) && isFirstLaunch.equalsIgnoreCase("true") && !TextUtils.isEmpty(deeplink)) {
                        //open deeplink
                        AppsflyerContainer.setDefferedDeeplinkPathIfExists(deeplink);
                    }
                } catch (ActivityNotFoundException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onInstallConversionFailure(String s) {
                // @TODO
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {

            }

            @Override
            public void onAttributionFailure(String s) {
                // @TODO
            }
        };

        try {
            Bundle bundle = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(),
                    PackageManager.GET_META_DATA).metaData;
            initAppsFlyer(bundle.getString(AppEventTracking.AF.APPSFLYER_KEY), userID, conversionListener);

            // suddenly hit appflyer init.
            if(getContext() instanceof TkpdCoreRouter){
                TkpdCoreRouter context = (TkpdCoreRouter) getContext().getApplicationContext();
                context.onAppsFlyerInit();
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            CommonUtils.dumper("Error key Appsflyer");
            initAppsFlyer(AppsflyerContainer.APPSFLYER_KEY, userID, conversionListener);
        }
    }

    public void initAppsFlyer(String key, String userID, AppsFlyerConversionListener conversionListener) {
        AppsFlyerLib.getInstance().init(key, conversionListener, getContext());
        initAppsFlyer(key, userID);
    }

    public void initAppsFlyer(String key, String userID) {
        AppsFlyerLib.getInstance().setCurrencyCode("IDR");
        setUserID(userID);
        AppsFlyerLib.getInstance().setDebugLog(BuildConfig.DEBUG);
        AppsFlyerLib.getInstance().setGCMProjectNumber(GCM_PROJECT_NUMBER);
        Application context = (Application) getContext();
        AppsFlyerLib.getInstance().startTracking(context, key);;
    }

    public void sendDeeplinkData(Activity activity) {
        AppsFlyerLib.getInstance().sendDeepLinkData(activity);
    }

    public void setUserID(String userID) {
        HashMap<String, Object> addData = new HashMap<>();
        addData.put(KEY_INSTALL_SOURCE, getContext().getPackageManager().getInstallerPackageName(
                getContext().getPackageName()));
        AppsFlyerLib.getInstance().setCustomerUserId(userID);
        AppsFlyerLib.getInstance().setAdditionalData(addData);
        CommonUtils.dumper(TAG + " appsflyer initiated with UID " + userID);
    }
}
