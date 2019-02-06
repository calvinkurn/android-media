package com.tokopedia.core.analytics.container;

import android.app.Activity;
import android.app.Application;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.tkpd.library.utils.legacy.CommonUtils;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.gcm.FCMCacheManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author by Nanda J.A on 6/25/2015.
 * Modified by Alvarisi
 * Modified by Alvarisi 29/11/2016
 * appsflyer get commented.
 */
@Deprecated
public class AppsflyerContainer implements IAppsflyerContainer {

    private static final String TAG = AppsflyerContainer.class.getSimpleName();
    private Application context;
    public static final String APPSFLYER_KEY = "SdSopxGtYr9yK8QEjFVHXL";
    private static final String KEY_INSTALL_SOURCE = "install_source";

    private static String deferredDeeplinkPath = "";

    public interface AFAdsIDCallback {
        void onGetAFAdsID(String string);

        void onErrorAFAdsID();
    }

    private AppsflyerContainer(Application context) {
        this.context = context;
    }

    public static AppsflyerContainer newInstance(Application context) {
        return new AppsflyerContainer(context);
    }

    @Override
    public void initAppsFlyer(String key, String userID) {
        setCurrencyCode("IDR");
        setUserID(userID);
        setAFLog(BuildConfig.DEBUG);
        setGCMId(Jordan.GCM_PROJECT_NUMBER);
        initUninstallTracking(key);
        setAppsFlyerKey(key);
        updateFCMToken(FCMCacheManager.getRegistrationId(context.getApplicationContext()));
    }

    @Override
    public void initAppsFlyer(String key, String userID, AppsFlyerConversionListener conversionListener) {
        AppsFlyerLib.getInstance().init(key, conversionListener, context);
        initAppsFlyer(key, userID);
    }

    private void setAppsFlyerKey(String key) {
        AppsFlyerLib.getInstance().startTracking(context, key);
    }

    private void initUninstallTracking(String key) {
        AppsFlyerLib.getInstance().enableUninstallTracking(key);
    }

    @Override
    public void updateFCMToken(String fcmToken) {
        AppsFlyerLib.getInstance().updateServerUninstallToken(context, fcmToken);
    }

    private void setCurrencyCode(String code) {
        AppsFlyerLib.getInstance().setCurrencyCode(code);
    }

    @Override
    public void setUserID(String userID) {
        HashMap<String, Object> addData = new HashMap<>();
        addData.put(KEY_INSTALL_SOURCE, context.getPackageManager().getInstallerPackageName(
                context.getPackageName()));
        AppsFlyerLib.getInstance().setCustomerUserId(userID);
        AppsFlyerLib.getInstance().setAdditionalData(addData);
        CommonUtils.dumper(TAG + " appsflyer initiated with UID " + userID);
    }

    private void setGCMId(String gcmID) {
        AppsFlyerLib.getInstance().setGCMProjectNumber(gcmID);
        AppsFlyerLib.getInstance().setGCMProjectID(gcmID);
    }

    private void setAFLog(boolean login) {
        AppsFlyerLib.getInstance().setDebugLog(login);
        if(com.tokopedia.config.GlobalConfig.IS_PREINSTALL) {
            AppsFlyerLib.getInstance().setPreinstallAttribution(
                    com.tokopedia.config.GlobalConfig.PREINSTALL_NAME,
                    com.tokopedia.config.GlobalConfig.PREINSTALL_DESC,
                    com.tokopedia.config.GlobalConfig.PREINSTALL_SITE
            );
        }
    }

    @Override
    public void sendTrackEvent(String eventName, Map<String, Object> eventValue) {
        CommonUtils.dumper(TAG + " Appsflyer send " + eventName + " " + eventValue);
        AppsFlyerLib.getInstance().trackEvent(context, eventName, eventValue);
    }

    @Override
    public void getAdsID(final AFAdsIDCallback callback) {

        Observable.just(true)
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<Boolean, AdvertisingIdClient.Info>() {
                    @Override
                    public AdvertisingIdClient.Info call(Boolean b) {
                        AdvertisingIdClient.Info adInfo = null;
                        try {
                            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
                        } catch (IOException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        }
                        return adInfo;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AdvertisingIdClient.Info>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callback.onErrorAFAdsID();
                    }

                    @Override
                    public void onNext(AdvertisingIdClient.Info info) {
                        callback.onGetAFAdsID(info.getId());
                        CommonUtils.dumper(TAG + " appsflyer succeded init ads ID " + info.getId() + " " + info.isLimitAdTrackingEnabled());
                    }
                });
    }

    @Override
    public String getAdsIdDirect() {

        AdvertisingIdClient.Info adInfo;
        try {
            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
            return adInfo.getId();
        } catch (IOException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    @Override
    public String getUniqueId() {
        return AppsFlyerLib.getInstance().getAppsFlyerUID(context);
    }

    @Override
    public void sendDeeplinkData(Activity activity) {
        AppsFlyerLib.getInstance().sendDeepLinkData(activity);
    }

    public static String getDefferedDeeplinkPathIfExists() {
        return deferredDeeplinkPath;
    }

    public static void setDefferedDeeplinkPathIfExists(String deeplinkPath) {
        deferredDeeplinkPath = deeplinkPath;
    }
}
