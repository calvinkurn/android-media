package com.tokopedia.test.application.environment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.play.core.splitcompat.SplitCompat;
import com.google.gson.Gson;
import com.tkpd.remoteresourcerequest.task.ResourceDownloadManager;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.analytics.performance.util.SplashScreenPerformanceTracker;
import com.tokopedia.analyticsdebugger.AnalyticsSource;
import com.tokopedia.analyticsdebugger.debugger.FpmLogger;
import com.tokopedia.analyticsdebugger.debugger.GtmLogger;
import com.tokopedia.applink.ApplinkDelegate;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.ApplinkUnsupported;
import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.container.GTMAnalytics;
import com.tokopedia.core.analytics.container.MoengageAnalytics;
import com.tokopedia.core.analytics.fingerprint.LocationCache;
import com.tokopedia.core.analytics.fingerprint.domain.model.FingerPrint;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.network.CoreNetworkApplication;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.instrumentation.test.R;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.remoteconfig.RemoteConfigInstance;
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface;
import com.tokopedia.test.application.environment.interceptor.TopAdsDetectorInterceptor;
import com.tokopedia.test.application.environment.interceptor.size.GqlNetworkAnalyzerInterceptor;
import com.tokopedia.test.application.util.DeviceConnectionInfo;
import com.tokopedia.test.application.util.DeviceInfo;
import com.tokopedia.test.application.util.DeviceScreenInfo;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.interfaces.ContextAnalytics;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.Interceptor;
import okhttp3.Response;

public class InstrumentationTestApp extends CoreNetworkApplication
        implements AbstractionRouter,
        TkpdCoreRouter,
        NetworkRouter,
        ApplinkRouter,
        TopAdsVerificatorInterface {
    public static final String MOCK_ADS_ID = "2df9e57a-849d-4259-99ea-673107469eef";
    public static final String MOCK_FINGERPRINT_HASH = "eyJjYXJyaWVyIjoiQW5kcm9pZCIsImN1cnJlbnRfb3MiOiI4LjAuMCIsImRldmljZV9tYW51ZmFjdHVyZXIiOiJHb29nbGUiLCJkZXZpY2VfbW9kZWwiOiJBbmRyb2lkIFNESyBidWlsdCBmb3IgeDg2IiwiZGV2aWNlX25hbWUiOiJBbmRyb2lkIFNESyBidWlsdCBmb3IgeDg2IiwiZGV2aWNlX3N5c3RlbSI6ImFuZHJvaWQiLCJpc19lbXVsYXRvciI6dHJ1ZSwiaXNfamFpbGJyb2tlbl9yb290ZWQiOmZhbHNlLCJpc190YWJsZXQiOmZhbHNlLCJsYW5ndWFnZSI6ImVuX1VTIiwibG9jYXRpb25fbGF0aXR1ZGUiOiItNi4xNzU3OTQiLCJsb2NhdGlvbl9sb25naXR1ZGUiOiIxMDYuODI2NDU3Iiwic2NyZWVuX3Jlc29sdXRpb24iOiIxMDgwLDE3OTQiLCJzc2lkIjoiXCJBbmRyb2lkV2lmaVwiIiwidGltZXpvbmUiOiJHTVQrNyIsInVzZXJfYWdlbnQiOiJEYWx2aWsvMi4xLjAgKExpbnV4OyBVOyBBbmRyb2lkIDguMC4wOyBBbmRyb2lkIFNESyBidWlsdCBmb3IgeDg2IEJ1aWxkL09TUjEuMTcwOTAxLjA0MykifQ==";
    public static final String MOCK_DEVICE_ID = "cx68b1CtPII:APA91bEV_bdZfq9qPB-xHn2z34ccRQ5M8y9c9pfqTbpIy1AlOrJYSFMKzm_GaszoFsYcSeZY-bTUbdccqmW8lwPQVli3B1fCjWnASz5ZePCpkh9iEjaWjaPovAZKZenowuo4GMD68hoR";
    private int topAdsProductCount = 0;
    private Long totalSizeInBytes = 0L;
    private Map<String, Interceptor> testInterceptors = new HashMap<>();
    private CacheManager cacheManager;

    @Override
    public void onCreate() {
        SplashScreenPerformanceTracker.isColdStart = true;
        GlobalConfig.DEBUG = true;
        GlobalConfig.VERSION_NAME = "3.115";
        SplitCompat.install(this);
        FpmLogger.init(this);
        PersistentCacheManager.init(this);

        TrackApp.initTrackApp(this);
        TrackApp.getInstance().registerImplementation(TrackApp.GTM, GTMAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.APPSFLYER, DummyAppsFlyerAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.MOENGAGE, MoengageAnalytics.class);
        initAkamaiBotManager();
        LinkerManager.initLinkerManager(getApplicationContext()).setGAClientId(TrackingUtils.getClientID(getApplicationContext()));
        TrackApp.getInstance().initializeAllApis();
        NetworkClient.init(this);
        GraphqlClient.init(this);
        RemoteConfigInstance.initAbTestPlatform(this);

        super.onCreate();

        ResourceDownloadManager
                .Companion.getManager()
                .setBaseAndRelativeUrl("http://dummy.dummy", "dummy")
                .initialize(this, R.raw.dummy_description);
    }

    private void initAkamaiBotManager() {
        com.tokopedia.akamai_bot_lib.UtilsKt.initAkamaiBotManager(InstrumentationTestApp.this);
        //Thread sleep to ensure akamai hit properly
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setDarkMode(Boolean isDarkMode) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void enableTopAdsDetector() {
        if (GlobalConfig.DEBUG) {
            addInterceptor(new TopAdsDetectorInterceptor(new Function1<Integer, Unit>() {
                @Override
                public Unit invoke(Integer newCount) {
                    topAdsProductCount += newCount;
                    return null;
                }
            }));
        }
    }

    public void enableSizeDetector(@Nullable List<String> listToAnalyze) {
        if (GlobalConfig.DEBUG) {
            GqlNetworkAnalyzerInterceptor.reset();
            GqlNetworkAnalyzerInterceptor.addGqlQueryListToAnalyze(listToAnalyze);

            addInterceptor(new GqlNetworkAnalyzerInterceptor());
        }
    }

    public void addInterceptor(Interceptor interceptor) {
        if (!testInterceptors.containsKey(interceptor.getClass().getCanonicalName())) {
            testInterceptors.put(interceptor.getClass().getCanonicalName(), interceptor);
            ArrayList<Interceptor> interceptorList = new ArrayList<Interceptor>(testInterceptors.values());
            GraphqlClient.reInitRetrofitWithInterceptors(interceptorList, this);
        }
    }

    public void setInterceptor(Interceptor interceptor) {
        GraphqlClient.reInitRetrofitWithInterceptors(Collections.singletonList(interceptor), this);
    }

    /**
     * this method is just for mock response API with custom interceptor
     * common_network with use case RestRequestSupportInterceptorUseCase
     */
    public void addRestSupportInterceptor(Interceptor interceptor) {
        NetworkClient.getApiInterfaceCustomInterceptor(Collections.singletonList(interceptor), this);
    }

    @Override
    public int getMinimumTopAdsProductFromResponse() {
        return topAdsProductCount;
    }

    @Override
    public void goToApplinkActivity(Context context, String applink) {

    }

    @Override
    public void goToApplinkActivity(Activity activity, String applink, Bundle bundle) {

    }

    @Override
    public void sendRefreshTokenAnalytics(String errorMessage) {

    }

    @Override
    public Intent getApplinkIntent(Context context, String applink) {
        return null;
    }

    @Override
    public boolean isSupportApplink(String appLink) {
        return false;
    }

    @Override
    public ApplinkUnsupported getApplinkUnsupported(Activity activity) {
        return null;
    }

    @Override
    public ApplinkDelegate applinkDelegate() {
        return null;
    }

    public static class DummyAppsFlyerAnalytics extends ContextAnalytics {

        public DummyAppsFlyerAnalytics(Context context) {
            super(context);
        }

        @Override
        public void sendGeneralEvent(Map<String, Object> value) {

        }

        @Override
        public void sendGeneralEvent(String event, String category, String action, String label) {

        }

        @Override
        public void sendEnhanceEcommerceEvent(Map<String, Object> value) {

        }

        @Override
        public void sendScreenAuthenticated(String screenName) {

        }

        @Override
        public void sendScreenAuthenticated(String screenName, Map<String, String> customDimension) {

        }

        @Override
        public void sendScreenAuthenticated(String screenName, String shopID, String shopType, String pageType, String productId) {

        }

        @Override
        public void sendEvent(String eventName, Map<String, Object> eventValue) {
            GtmLogger.getInstance(getContext()).save(eventName, eventValue, AnalyticsSource.APPS_FLYER);
        }

        @Override
        public void sendTrackEvent(Map<String, Object> data, String eventName) {
            sendTrackEvent(eventName, data);
        }

        @Override
        public void sendTrackEvent(String eventName, Map<String, Object> eventValue) {
            GtmLogger.getInstance(getContext()).save(eventName, eventValue, AnalyticsSource.APPS_FLYER);
        }
    }

    @Override
    public Class<?> getDeeplinkClass() {
        return null;
    }

    @Override
    public Intent getInboxTalkCallingIntent(Context mContext) {
        return null;
    }

    @Override
    public IAppNotificationReceiver getAppNotificationReceiver() {
        return null;
    }

    @Override
    public Class<?> getInboxMessageActivityClass() {
        return null;
    }

    @Override
    public Class<?> getInboxResCenterActivityClassReal() {
        return null;
    }

    @Override
    public Intent getHomeIntent(Context context) {
        return null;
    }

    @Override
    public Class<?> getHomeClass() {
        return null;
    }

    @Override
    public NotificationPass setNotificationPass(Context mContext, NotificationPass mNotificationPass, Bundle data, String notifTitle) {
        return null;
    }

    @Override
    public void onAppsFlyerInit() {

    }

    @Override
    public Intent getMaintenancePageIntent() {
        return new Intent();
    }

    public void refreshFCMTokenFromBackgroundToCM(String token, boolean force) {

    }

    @Override
    public void refreshFCMFromInstantIdService(String token) {

    }

    @Override
    public void onForceLogout(Activity activity) {

    }

    @Override
    public void showTimezoneErrorSnackbar() {

    }

    @Override
    public void showMaintenancePage() {

    }

    @Override
    public void sendForceLogoutAnalytics(String url, boolean isInvalidToken, boolean isRequestDenied) {

    }

    @Override
    public void showForceLogoutTokenDialog(String response) {

    }

    @Override
    public void showServerError(Response response) {

    }

    @Override
    public void logInvalidGrant(Response response) {

    }

    @Override
    public void logRefreshTokenException(String error, String type, String path, String accessToken) {

    }

    @Override
    public FingerprintModel getFingerprintModel() {
        FingerprintModel fingerprintModel = new FingerprintModel();
        fingerprintModel.setRegistrarionId(getRegistrarianId());
        fingerprintModel.setAdsId(getAdsId());
        try {
            fingerprintModel.setFingerprintHash(getFingerprintHash());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return fingerprintModel;
    }

    public String toBase64(String text, int mode) throws UnsupportedEncodingException {
        byte[] data = text.getBytes("UTF-8");
        return Base64.encodeToString(data, mode);
    }

    public String getFingerprintHash() throws UnsupportedEncodingException {
        String deviceName = DeviceInfo.getModelName();
        String deviceFabrik = DeviceInfo.getManufacturerName();
        String deviceOS = DeviceInfo.getOSName();
        String deviceSystem = "android";
        boolean isRooted = DeviceInfo.isRooted();
        String timezone = DeviceInfo.getTimeZoneOffset();
        String userAgent = DeviceConnectionInfo.getHttpAgent();
        boolean isEmulator = DeviceInfo.isEmulated();
        boolean isTablet = DeviceScreenInfo.isTablet(this);
        String screenReso = DeviceScreenInfo.getScreenResolution(this);
        String deviceLanguage = DeviceInfo.getLanguage();
        String ssid = DeviceConnectionInfo.getSSID(this);
        String carrier = DeviceConnectionInfo.getCarrierName(this);
        String adsId = getAdsId();
        String androidId = DeviceInfo.getAndroidId(this);
        boolean isx86 = DeviceInfo.isx86();
        String packageName = DeviceInfo.getPackageName(this);

        FingerPrint fp = new FingerPrint.FingerPrintBuilder()
                .uniqueId(adsId)
                .deviceName(deviceName)
                .deviceManufacturer(deviceFabrik)
                .model(deviceName)
                .system(deviceSystem)
                .currentOS(deviceOS)
                .jailbreak(isRooted)
                .timezone(timezone)
                .userAgent(userAgent)
                .emulator(isEmulator)
                .tablet(isTablet)
                .screenReso(screenReso)
                .language(deviceLanguage)
                .ssid(ssid)
                .carrier(carrier)
                .deviceLat(new LocationCache(this).getLatitudeCache())
                .deviceLng(new LocationCache(this).getLongitudeCache())
                .androidId(androidId)
                .isx86(isx86)
                .packageName(packageName)
                .build();
        Gson gson = new Gson();
        String json = gson.toJson(fp);
        return toBase64(json, Base64.NO_WRAP);
    }

    public String getRegistrarianId() {
        return UUID.randomUUID().toString();
    }

    public String getAdsId() {
        AdvertisingIdClient.Info adInfo;
        try {
            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(this);
        } catch (IOException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            return "";
        }

        if (adInfo != null) {
            String adID = adInfo.getId();
            return adID;
        }
        return "";
    }

    @Override
    public void doRelogin(String newAccessToken) {

    }

    @Override
    public void gcmUpdate() throws IOException {

    }

    @Override
    public void refreshToken() throws IOException {

    }

    @Override
    public CacheManager getPersistentCacheManager() {
        if (cacheManager == null)
            cacheManager = new PersistentCacheManager(this);
        return cacheManager;
    }

    @Override
    public boolean isAllowLogOnChuckInterceptorNotification() {
        return false;
    }

    @Override
    public void onNewIntent(Context context, Intent intent) {

    }

    @Override
    public void sendAnalyticsAnomalyResponse(String s, String s1, String s2, String s3, String s4) {

    }
}
