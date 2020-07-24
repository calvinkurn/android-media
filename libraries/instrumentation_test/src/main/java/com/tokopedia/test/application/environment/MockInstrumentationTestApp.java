package com.tokopedia.test.application.environment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.play.core.splitcompat.SplitCompat;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.analyticsdebugger.debugger.FpmLogger;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.container.GTMAnalytics;
import com.tokopedia.core.analytics.container.MoengageAnalytics;
import com.tokopedia.core.analytics.fingerprint.LocationCache;
import com.tokopedia.core.analytics.fingerprint.domain.model.FingerPrint;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.test.application.environment.interceptor.MockInterceptor;
import com.tokopedia.test.application.util.DeviceConnectionInfo;
import com.tokopedia.test.application.util.DeviceInfo;
import com.tokopedia.test.application.util.DeviceScreenInfo;
import com.tokopedia.test.application.util.MockResponseList;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.interfaces.ContextAnalytics;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import okhttp3.Interceptor;
import okhttp3.Response;

public class MockInstrumentationTestApp extends BaseMainApplication implements TkpdCoreRouter, NetworkRouter, MockResponseInterface {

    @Override
    public void onCreate() {
        SplitCompat.install(this);
        FirebaseApp.initializeApp(this);
        FpmLogger.init(this);
        TrackApp.initTrackApp(this);
        NetworkClient.init(this);
        TrackApp.getInstance().registerImplementation(TrackApp.GTM, GTMAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.APPSFLYER, DummyAppsFlyerAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.MOENGAGE, MoengageAnalytics.class);
        LinkerManager.initLinkerManager(getApplicationContext()).setGAClientId(TrackingUtils.getClientID(getApplicationContext()));
        TrackApp.getInstance().initializeAllApis();
        GlobalConfig.DEBUG = true;
        GlobalConfig.VERSION_NAME = "3.66";
        GraphqlClient.init(this);
        GlobalConfig.DEBUG = true;
        enableMockResponse();
        super.onCreate();
    }

    public void enableMockResponse() {
        if (GlobalConfig.DEBUG) {
            List<Interceptor> testInterceptors = new ArrayList<>();
            testInterceptors.add(new MockInterceptor(MockResponseList.INSTANCE.create(this)));

            GraphqlClient.reInitRetrofitWithInterceptors(testInterceptors, this);
        }
    }

    @Override
    public void reInitMockResponse(HashMap<String, String> mapMockResponse) {
        if (GlobalConfig.DEBUG) {
            List<Interceptor> testInterceptors = new ArrayList<>();
            testInterceptors.add(new MockInterceptor(mapMockResponse));

            GraphqlClient.reInitRetrofitWithInterceptors(testInterceptors, this);
        }
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
    public SessionHandler legacySessionHandler() {
        return new SessionHandler(this) {

            @Override
            public String getGTMLoginID() {
                return "null";
            }

            @Override
            public String getLoginID() {
                return "null";
            }

            @Override
            public String getRefreshToken() {
                return "null";
            }

            @Override
            public boolean isMsisdnVerified() {
                return false;
            }
        };
    }

    @Override
    public GCMHandler legacyGCMHandler() {
        return new GCMHandler(this);
    }

    @Override
    public void refreshFCMTokenFromBackgroundToCM(String token, boolean force) {

    }

    @Override
    public void refreshFCMFromInstantIdService(String token) {

    }

    @Override
    public void refreshFCMTokenFromForegroundToCM() {

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
    public void sendForceLogoutAnalytics(Response response, boolean isInvalidToken, boolean isRequestDenied) {

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
    public FingerprintModel getFingerprintModel() {
        FingerprintModel fingerprintModel = new FingerprintModel();
//        fingerprintModel.setAdsId("411231a3-cb9b-428b-8427-991aecdc34f7");
//        fingerprintModel.setRegistrarionId("f4aKfO2FyP4:APA91bFWIdSwWaj5-22Y6Qb0KpG1NAXYV_2u0KLIXd4prQ2onGOSNq1aPfoHqjzHwpk0td6KjGoL1Tyd1tXmfQb0AsPTFVTK-Jiik_2bjMLpLEevVniZ3sFkj3ctorX4jFMKUC9w4HLG");
//        fingerprintModel.setFingerprintHash("eyJhbmRyb2lkSWQiOiI1ZWRmNGUxYzlkODRhNzhmIiwiYXZhaWxhYmxlUHJvY2Vzc29yIjoiNCIsImNhcnJpZXIiOiJBbmRyb2lkIiwiY3VycmVudF9vcyI6IjEwIiwiZGV2aWNlRHBpIjoiMi42MjUiLCJkZXZpY2VNZW1vcnlDbGFzc0NhcGFjaXR5IjoiNTEyIiwiZGV2aWNlX21hbnVmYWN0dXJlciI6Ikdvb2dsZSIsImRldmljZV9tb2RlbCI6IkFuZHJvaWQgU0RLIGJ1aWx0IGZvciB4ODZfNjQiLCJkZXZpY2VfbmFtZSI6IkFuZHJvaWQgU0RLIGJ1aWx0IGZvciB4ODZfNjQiLCJkZXZpY2Vfc3lzdGVtIjoiYW5kcm9pZCIsImlzX2VtdWxhdG9yIjp0cnVlLCJpc19qYWlsYnJva2VuX3Jvb3RlZCI6dHJ1ZSwiaXNfbmFrYW1hIjoiRmFsc2UiLCJpc190YWJsZXQiOmZhbHNlLCJpc3g4NiI6dHJ1ZSwibGFuZ3VhZ2UiOiJlbl9VUyIsImxvY2F0aW9uX2xhdGl0dWRlIjoiLTYuMTc1Nzk0IiwibG9jYXRpb25fbG9uZ2l0dWRlIjoiMTA2LjgyNjQ1NyIsInBhY2thZ2VOYW1lIjoiY29tLnRva29wZWRpYS50a3BkIiwic2NyZWVuX3Jlc29sdXRpb24iOiIxMDgwLDE3OTQiLCJzc2lkIjoiXHUwMDNjdW5rbm93biBzc2lkXHUwMDNlIiwidGltZXpvbmUiOiJHTVQrNyIsInVuaXF1ZV9pZCI6IjQxMTIzMWEzLWNiOWItNDI4Yi04NDI3LTk5MWFlY2RjMzRmNyIsInVzZXJfYWdlbnQiOiJEYWx2aWsvMi4xLjAgKExpbnV4OyBVOyBBbmRyb2lkIDEwOyBBbmRyb2lkIFNESyBidWlsdCBmb3IgeDg2XzY0IEJ1aWxkL1FQUDYuMTkwNzMwLjAwNS5CMSkifQ==");

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
        String deviceName   = DeviceInfo.getModelName();
        String deviceFabrik = DeviceInfo.getManufacturerName();
        String deviceOS     = DeviceInfo.getOSName();
        String deviceSystem = "android";
        boolean isRooted    = DeviceInfo.isRooted();
        String timezone     = DeviceInfo.getTimeZoneOffset();
        String userAgent    = DeviceConnectionInfo.getHttpAgent();
        boolean isEmulator  = DeviceInfo.isEmulated();
        boolean isTablet    = DeviceScreenInfo.isTablet(this);
        String screenReso     = DeviceScreenInfo.getScreenResolution(this);
        String deviceLanguage = DeviceInfo.getLanguage();
        String ssid         = DeviceConnectionInfo.getSSID(this);
        String carrier      = DeviceConnectionInfo.getCarrierName(this);
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
    public void sendAnalyticsAnomalyResponse(String s, String s1, String s2, String s3, String s4) {

    }

}
