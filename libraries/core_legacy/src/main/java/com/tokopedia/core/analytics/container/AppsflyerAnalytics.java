package com.tokopedia.core.analytics.container;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.tokopedia.analyticsdebugger.AnalyticsSource;
import com.tokopedia.analyticsdebugger.debugger.GtmLogger;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.track.interfaces.AFAdsIDCallback;
import com.tokopedia.track.interfaces.ContextAnalytics;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.appsflyer.AFInAppEventParameterName.CUSTOMER_USER_ID;
import static com.appsflyer.AFInAppEventParameterName.REGSITRATION_METHOD;

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

        final SessionHandler sessionHandler = RouterUtils.getRouterFromContext(getContext())
                .legacySessionHandler();
        UserSessionInterface userSession = new UserSession(context);
        final String userID = userSession.isLoggedIn() ? sessionHandler.getLoginID() : "00000";


        Timber.d("Appsflyer login userid " + userID);
        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {

            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {
                if (isAppsflyerCallbackHandled) return;
                isAppsflyerCallbackHandled = true;

                try {
                    boolean isFirstLaunch = false;
                    String deeplink = null;
                    if (conversionData.containsKey("is_first_launch"))
                        isFirstLaunch = (boolean) conversionData.get("is_first_launch");
                    if (conversionData.containsKey("af_dp"))
                        deeplink = (String) conversionData.get("af_dp");

                    if (isFirstLaunch && !TextUtils.isEmpty(deeplink)) {
                        setDefferedDeeplinkPathIfExists(deeplink);
                    }
                } catch (ActivityNotFoundException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onConversionDataFail(String s) {

            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {

            }

            @Override
            public void onAttributionFailure(String s) {

            }
        };

        try {
            Bundle bundle = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(),
                    PackageManager.GET_META_DATA).metaData;
            initAppsFlyer(bundle.getString(AppEventTracking.AF.APPSFLYER_KEY), userID, conversionListener);

            // suddenly hit appflyer init.
            if (getContext() instanceof TkpdCoreRouter) {
                TkpdCoreRouter context = (TkpdCoreRouter) getContext().getApplicationContext();
                context.onAppsFlyerInit();
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Timber.d("Error key Appsflyer");
            initAppsFlyer(AppsflyerContainer.APPSFLYER_KEY, userID, conversionListener);
        }
    }

    @Override
    public void sendAppsflyerRegisterEvent(String userId, String method) {
        Map<String, Object> eventVal = new HashMap<>();
        eventVal.put("custom_prop1", "registration");
        eventVal.put("os", "Android");
        eventVal.put(CUSTOMER_USER_ID, userId);
        eventVal.put(REGSITRATION_METHOD, method);
        sendTrackEvent(AFInAppEventType.COMPLETE_REGISTRATION, eventVal);
    }

    public void updateFCMToken(String fcmToken) {
        AppsFlyerLib.getInstance().updateServerUninstallToken(context, fcmToken);
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

    public void initAppsFlyer(String key, String userID, AppsFlyerConversionListener conversionListener) {
        WeaveInterface appsFlyerInitWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return executeInitAppsFlyer(key, userID, conversionListener);
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(appsFlyerInitWeave, RemoteConfigKey.ENABLE_ASYNC_CREATE_APPSFLYER, context);
    }

    @NotNull
    private boolean executeInitAppsFlyer(String key, String userID, AppsFlyerConversionListener conversionListener) {
        AppsFlyerLib.getInstance().init(key, conversionListener, getContext());
        AppsFlyerLib.getInstance().setCurrencyCode("IDR");
        setUserID(userID);
        AppsFlyerLib.getInstance().setDebugLog(BuildConfig.DEBUG);
        if(com.tokopedia.config.GlobalConfig.IS_PREINSTALL) {
            AppsFlyerLib.getInstance().setPreinstallAttribution(
                    com.tokopedia.config.GlobalConfig.PREINSTALL_NAME,
                    com.tokopedia.config.GlobalConfig.PREINSTALL_DESC,
                    com.tokopedia.config.GlobalConfig.PREINSTALL_SITE
            );
        }
        AppsFlyerLib.getInstance().startTracking(getContext(), key);
        return true;
    }

    public void sendEvent(String eventName, Map<String, Object> eventValue) {
        AppsFlyerLib.getInstance().trackEvent(getContext(), eventName, eventValue);
        saveAppsFlyerEvent(eventName, eventValue);
    }

    //aliasing
    @Override
    public void sendTrackEvent(Map<String, Object> data, String eventName) {
        sendTrackEvent(eventName, data);
    }

    @Override
    public void sendTrackEvent(String eventName, Map<String, Object> eventValue) {
        AppsFlyerLib.getInstance().trackEvent(getContext(), eventName, eventValue);
        saveAppsFlyerEvent(eventName, eventValue);
    }

    public void sendDeeplinkData(Activity activity) {
        AppsFlyerLib.getInstance().sendDeepLinkData(activity);
    }

    public void getAdsID(final AFAdsIDCallback callback) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(ADVERTISINGID, Context.MODE_PRIVATE);

        String adsId = sharedPrefs.getString(KEY_ADVERTISINGID, "");
        if (adsId != null && !"".equalsIgnoreCase(adsId.trim())) {
            callback.onGetAFAdsID(adsId);
            return;
        }

        Observable.fromCallable(() -> {
            AdvertisingIdClient.Info adInfo;
            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
            return adInfo.getId();
        }).doOnNext(s -> {
            if (!TextUtils.isEmpty(s)) {
                sharedPrefs.edit().putString(KEY_ADVERTISINGID, s).apply();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
        .subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                callback.onErrorAFAdsID();
            }

            @Override
            public void onNext(String s) {
                if (TextUtils.isEmpty(s)) {
                    callback.onErrorAFAdsID();
                } else {
                    callback.onGetAFAdsID(s);
                }
            }
        });
    }

    @Deprecated
    @Override
    public String getGoogleAdId() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(ADVERTISINGID, Context.MODE_PRIVATE);

        String adsId = sharedPrefs.getString(KEY_ADVERTISINGID, "");
        if (adsId != null && !"".equalsIgnoreCase(adsId.trim())) {
            return adsId;
        }

        return (Observable.just("").subscribeOn(Schedulers.newThread())
                .map(string -> {
                    AdvertisingIdClient.Info adInfo = null;
                    try {
                        adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
                    } catch (IOException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    }
                    return adInfo.getId();
                }).onErrorReturn(throwable -> "")
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(adID -> {
                    if (!TextUtils.isEmpty(adID)) {
                        sharedPrefs.edit().putString(KEY_ADVERTISINGID, adID).apply();
                    }
                })).toBlocking().single();
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
            GtmLogger.getInstance(getContext()).save(eventName, eventValue, AnalyticsSource.APPS_FLYER);
        } catch (Exception e) {
            Timber.e(e);
        }
    }
}
