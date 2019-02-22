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
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.tkpd.library.utils.legacy.CommonUtils;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.track.interfaces.ContextAnalytics;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AppsflyerAnalytics extends ContextAnalytics {
    private static final String TAG = AppsflyerAnalytics.class.getSimpleName();
    private static boolean isAppsflyerCallbackHandled = false;
    public static final String APPSFLYER_KEY = "SdSopxGtYr9yK8QEjFVHXL";
    private static final String KEY_INSTALL_SOURCE = "install_source";
    public static final String GCM_PROJECT_NUMBER = "692092518182";

    private static String deferredDeeplinkPath = "";

    public AppsflyerAnalytics(Context context) {
        super(context);
    }

    @Override
    public void initialize() {
        super.initialize();

        final SessionHandler sessionHandler = RouterUtils.getRouterFromContext(getContext())
                .legacySessionHandler();

        final String userID = sessionHandler.isV4Login() ? sessionHandler.getLoginID() : "00000";


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
                        setDefferedDeeplinkPathIfExists(deeplink);
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

    @Override
    public void sendGeneralEvent(Map<String, Object> value) {
        // no op, only for GTM
    }

    @Override
    public void sendEnhanceECommerceEvent(Map<String, Object> value) {
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

    public void initAppsFlyer(String key, String userID, AppsFlyerConversionListener conversionListener) {
        AppsFlyerLib.getInstance().init(key, conversionListener, getContext());
        initAppsFlyer(key, userID);
    }

    public void initAppsFlyer(String key, String userID) {
        AppsFlyerLib.getInstance().setCurrencyCode("IDR");
        setUserID(userID);
        AppsFlyerLib.getInstance().setDebugLog(BuildConfig.DEBUG);
        AppsFlyerLib.getInstance().setGCMProjectNumber(GCM_PROJECT_NUMBER);
        AppsFlyerLib.getInstance().startTracking(getContext(), key);
    }

    public void sendTrackEvent(String eventName, Map<String, Object> eventValue) {
        CommonUtils.dumper(TAG + " Appsflyer send " + eventName + " " + eventValue);
        AppsFlyerLib.getInstance().trackEvent(getContext(), eventName, eventValue);
    }

    public void sendDeeplinkData(Activity activity) {
        AppsFlyerLib.getInstance().sendDeepLinkData(activity);
    }

    public void getAdsID(final AppsflyerContainer.AFAdsIDCallback callback) {
            AdvertisingIdClient.Info adInfo = null;
            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(getContext());
                callback.onGetAFAdsID(adInfo.getId());
                CommonUtils.dumper(TAG + " appsflyer succeded init ads ID " + adInfo.getId() + " " + adInfo.isLimitAdTrackingEnabled());
            } catch (IOException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                e.printStackTrace();
                callback.onErrorAFAdsID();
            }
    }

    public String getAdsIdDirect() {

        AdvertisingIdClient.Info adInfo;
        try {
            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(getContext());
            return adInfo.getId();
        } catch (IOException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getUniqueId() {
        return AppsFlyerLib.getInstance().getAppsFlyerUID(getContext());
    }

    public void setUserID(String userID) {
        HashMap<String, Object> addData = new HashMap<>();
        addData.put(KEY_INSTALL_SOURCE, getContext().getPackageManager().getInstallerPackageName(
                getContext().getPackageName()));
        AppsFlyerLib.getInstance().setCustomerUserId(userID);
        AppsFlyerLib.getInstance().setAdditionalData(addData);
        CommonUtils.dumper(TAG + " appsflyer initiated with UID " + userID);
    }

    public static String getDefferedDeeplinkPathIfExists() {
        return deferredDeeplinkPath;
    }

    public static void setDefferedDeeplinkPathIfExists(String deeplinkPath) {
        deferredDeeplinkPath = deeplinkPath;
    }
}
