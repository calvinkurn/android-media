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
import com.tkpd.remoteresourcerequest.task.ResourceDownloadManager;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.analytics.performance.util.SplashScreenPerformanceTracker;
import com.tokopedia.analyticsdebugger.cassava.AnalyticsSource;
import com.tokopedia.analyticsdebugger.debugger.FpmLogger;
import com.tokopedia.analyticsdebugger.cassava.GtmLogger;
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
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.network.CoreNetworkApplication;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.instrumentation.test.BuildConfig;
import com.tokopedia.instrumentation.test.R;
import com.tokopedia.interceptors.authenticator.TkpdAuthenticatorGql;
import com.tokopedia.interceptors.refreshtoken.RefreshTokenGql;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.remoteconfig.RemoteConfigInstance;
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface;
import com.tokopedia.test.application.environment.interceptor.TopAdsDetectorInterceptor;
import com.tokopedia.test.application.environment.interceptor.size.GqlNetworkAnalyzerInterceptor;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.interfaces.ContextAnalytics;
import com.tokopedia.user.session.UserSession;

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
import timber.log.Timber;

public class InstrumentationTestApp extends CoreNetworkApplication
        implements AbstractionRouter,
        TkpdCoreRouter,
        NetworkRouter,
        ApplinkRouter,
        TopAdsVerificatorInterface {
    private int topAdsProductCount = 0;

    private Map<String, Interceptor> testInterceptors = new HashMap<>();
    private CacheManager cacheManager;

    @Override
    public void onCreate() {
        SplashScreenPerformanceTracker.isColdStart = true;
        GlobalConfig.DEBUG = true;
        GlobalConfig.VERSION_NAME = "3.150";
        initFileDirConfig();
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
        GraphqlClient.init(this, getAuthenticator());
        RemoteConfigInstance.initAbTestPlatform(this);

        super.onCreate();

        ResourceDownloadManager
                .Companion.getManager()
                .setBaseAndRelativeUrl("http://dummy.dummy", "dummy")
                .initialize(this, R.raw.dummy_description);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private TkpdAuthenticatorGql getAuthenticator() {
        return new TkpdAuthenticatorGql(this, this, new UserSession(this), new RefreshTokenGql());
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

    public void initFileDirConfig(){
        GlobalConfig.INTERNAL_CACHE_DIR = this.getCacheDir().getAbsolutePath();
        GlobalConfig.INTERNAL_FILE_DIR = this.getFilesDir().getAbsolutePath();
        GlobalConfig.EXTERNAL_CACHE_DIR = this.getExternalCacheDir() != null ? this.getExternalCacheDir().getAbsolutePath() : "";
        GlobalConfig.EXTERNAL_FILE_DIR = this.getExternalFilesDir(null) != null ? this.getExternalFilesDir(null).getAbsolutePath() : "";
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
            GtmLogger.getInstance(getContext()).save(eventValue, eventName, AnalyticsSource.APPS_FLYER);
        }

        @Override
        public void sendTrackEvent(Map<String, Object> data, String eventName) {
            sendTrackEvent(eventName, data);
        }

        @Override
        public void sendTrackEvent(String eventName, Map<String, Object> eventValue) {
            GtmLogger.getInstance(getContext()).save(eventValue, eventName, AnalyticsSource.APPS_FLYER);
        }
    }

    @Override
    public IAppNotificationReceiver getAppNotificationReceiver() {
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
        return toBase64("", Base64.NO_WRAP);
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
