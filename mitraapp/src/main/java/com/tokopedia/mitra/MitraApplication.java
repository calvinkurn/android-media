package com.tokopedia.mitra;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.tokopedia.SessionRouter;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.constant.AbstractionBaseURL;
import com.tokopedia.applink.ApplinkDelegate;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.ApplinkUnsupported;
import com.tokopedia.applink.TkpdApplinkDelegate;
import com.tokopedia.common_digital.common.constant.DigitalUrl;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.graphql.data.source.cloud.api.GraphqlUrl;
import com.tokopedia.imageuploader.data.ImageUploaderUrl;
import com.tokopedia.logout.data.LogoutUrl;
import com.tokopedia.mitra.applink.MitraAppApplinkModuleLoader;
import com.tokopedia.mitra.fingerprint.FingerprintModelGenerator;
import com.tokopedia.mitra.fingerprint.LocationUtils;
import com.tokopedia.mitra.homepage.activity.MitraParentHomepageActivity;
import com.tokopedia.mitra.session.UserSessionImpl;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.SessionUrl;
import com.tokopedia.network.data.model.FingerprintModel;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by Rizky on 30/08/18.
 */
public class MitraApplication extends MainApplication implements NetworkRouter, AbstractionRouter, SessionRouter, ApplinkRouter {

    private static MitraApplication instance;
    private LocationUtils locationUtils;
    private TkpdApplinkDelegate applinkDelegate;

    @Override
    public void onCreate() {
        GlobalConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
        GlobalConfig.DEBUG = BuildConfig.DEBUG;
        GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        generateVariantUrl();

        super.onCreate();

        initDbFlow();

        instance = this;

        locationUtils = new LocationUtils(this);
        locationUtils.initLocationBackground();
        applinkDelegate = new TkpdApplinkDelegate(new MitraAppApplinkModuleLoader());
    }

    private void generateVariantUrl() {
        TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL = MitraAppBaseUrl.BASE_TOKOPEDIA_WEBSITE;
        TkpdBaseURL.BASE_DOMAIN = MitraAppBaseUrl.BASE_DOMAIN;
        TkpdBaseURL.BASE_API_DOMAIN = MitraAppBaseUrl.BASE_API_DOMAIN;
        TkpdBaseURL.ACE_DOMAIN = MitraAppBaseUrl.BASE_ACE_DOMAIN;
        TkpdBaseURL.TOME_DOMAIN = MitraAppBaseUrl.BASE_TOME_DOMAIN;
        TkpdBaseURL.TOPADS_DOMAIN = MitraAppBaseUrl.BASE_TOPADS_DOMAIN;
        TkpdBaseURL.MOJITO_DOMAIN = MitraAppBaseUrl.BASE_MOJITO_DOMAIN;
        TkpdBaseURL.HADES_DOMAIN = MitraAppBaseUrl.BASE_HADES_DOMAIN;
        TkpdBaseURL.ACCOUNTS_DOMAIN = MitraAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        TkpdBaseURL.INBOX_DOMAIN = MitraAppBaseUrl.BASE_INBOX_DOMAIN;
        TkpdBaseURL.JS_DOMAIN = MitraAppBaseUrl.BASE_JS_DOMAIN;
        TkpdBaseURL.KERO_DOMAIN = MitraAppBaseUrl.BASE_KERO_DOMAIN;
        TkpdBaseURL.KERO_RATES_DOMAIN = MitraAppBaseUrl.BASE_KERO_RATES_DOMAIN;
        TkpdBaseURL.JAHE_DOMAIN = MitraAppBaseUrl.BASE_JAHE_DOMAIN;
        TkpdBaseURL.PULSA_WEB_DOMAIN = MitraAppBaseUrl.BASE_PULSA_WEB_DOMAIN;
        TkpdBaseURL.GOLD_MERCHANT_DOMAIN = MitraAppBaseUrl.BASE_GOLD_MERCHANT_DOMAIN;
        TkpdBaseURL.WEB_DOMAIN = MitraAppBaseUrl.BASE_WEB_DOMAIN;
        TkpdBaseURL.MOBILE_DOMAIN = MitraAppBaseUrl.BASE_MOBILE_DOMAIN;
        TkpdBaseURL.BASE_CONTACT_US = MitraAppBaseUrl.BASE_WEB_DOMAIN + "contact-us";
        TkpdBaseURL.TOKO_CASH_DOMAIN = MitraAppBaseUrl.BASE_TOKO_CASH_DOMAIN;
        TkpdBaseURL.BASE_ACTION = MitraAppBaseUrl.BASE_DOMAIN + "v4/action/";
        TkpdBaseURL.DIGITAL_API_DOMAIN = MitraAppBaseUrl.BASE_DIGITAL_API_DOMAIN;
        TkpdBaseURL.DIGITAL_WEBSITE_DOMAIN = MitraAppBaseUrl.BASE_DIGITAL_WEBSITE_DOMAIN;
        TkpdBaseURL.GRAPHQL_DOMAIN = MitraAppBaseUrl.GRAPHQL_DOMAIN;
        TkpdBaseURL.HOME_DATA_BASE_URL = MitraAppBaseUrl.HOME_DATA_BASE_URL;
        TkpdBaseURL.SCROOGE_DOMAIN = MitraAppBaseUrl.SCROOGE_DOMAIN;
        TkpdBaseURL.SCROOGE_CREDIT_CARD_DOMAIN = MitraAppBaseUrl.SCROOGE_CREDIT_CARD_DOMAIN;
        TkpdBaseURL.PAYMENT_DOMAIN = MitraAppBaseUrl.PAYMENT_DOMAIN;
        TkpdBaseURL.GALADRIEL = MitraAppBaseUrl.GALADRIEL;
        TkpdBaseURL.CHAT_DOMAIN = MitraAppBaseUrl.CHAT_DOMAIN;
        TkpdBaseURL.CHAT_WEBSOCKET_DOMAIN = MitraAppBaseUrl.CHAT_WEBSOCKET_DOMAIN;
        TkpdBaseURL.MAPS_DOMAIN = MitraAppBaseUrl.MAPS_DOMAIN;
        TkpdBaseURL.WALLET_DOMAIN = MitraAppBaseUrl.BASE_WALLET;
        TkpdBaseURL.EVENTS_DOMAIN = MitraAppBaseUrl.EVENT_DOMAIN;
        TkpdBaseURL.TOKOPOINT_API_DOMAIN = MitraAppBaseUrl.TOKOPOINT_API_DOMAIN;
        AbstractionBaseURL.JS_DOMAIN = MitraAppBaseUrl.BASE_JS_DOMAIN;
        SessionUrl.ACCOUNTS_DOMAIN = MitraAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        SessionUrl.BASE_DOMAIN = MitraAppBaseUrl.BASE_DOMAIN;
        DigitalUrl.WEB_DOMAIN = MitraAppBaseUrl.BASE_WEB_DOMAIN;
        com.tokopedia.network.constant.TkpdBaseURL.DEFAULT_TOKOPEDIA_GQL_URL = MitraAppBaseUrl.BASE_TOKOPEDIA_GQL;
        SessionUrl.CHANGE_PHONE_DOMAIN = MitraAppBaseUrl.CHANGE_PHONE_DOMAIN;
        GraphqlUrl.BASE_URL = MitraAppBaseUrl.GRAPHQL_DOMAIN;
        ImageUploaderUrl.BASE_URL = MitraAppBaseUrl.BASE_DOMAIN;
        LogoutUrl.Companion.setBASE_URL(MitraAppBaseUrl.BASE_DOMAIN);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(MitraApplication.this);
    }

    public static MitraApplication getInstance() {
        return instance;
    }

    protected void initDbFlow() {
        if (BuildConfig.DEBUG) {
            FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
        }
        FlowManager.init(new FlowConfig.Builder(this)
                .build());
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
    public void showForceLogoutDialog(Response response) {

    }

    @Override
    public void showForceLogoutTokenDialog(String response) {

    }

    @Override
    public void showServerError(Response response) {

    }

    @Override
    public void gcmUpdate() throws IOException {

    }

    @Override
    public void refreshToken() throws IOException {

    }

    @Override
    public UserSession getSession() {
        return new UserSessionImpl(this);
    }

    @Override
    public void init() {

    }

    @Override
    public void registerShake(String screenName, Activity activity) {

    }

    @Override
    public void unregisterShake() {

    }

    @Override
    public CacheManager getGlobalCacheManager() {
        return new CacheManager() {
            @Override
            public void save(String key, String value, long durationInSeconds) {

            }

            @Override
            public void delete(String key) {

            }

            @Override
            public String get(String key) {
                return null;
            }

            @Override
            public boolean isExpired(String key) {
                return false;
            }
        };
    }

    @Override
    public AnalyticTracker getAnalyticTracker() {
        return new AnalyticTracker() {
            @Override
            public void sendEventTracking(Map<String, Object> events) {

            }

            @Override
            public void sendEventTracking(String event, String category, String action, String label) {

            }

            @Override
            public void sendScreen(Activity activity, String screenName) {

            }

            @Override
            public void sendEnhancedEcommerce(Map<String, Object> trackingData) {

            }
        };
    }

    @Override
    public void showForceHockeyAppDialog() {

    }

    @Override
    public void logInvalidGrant(Response response) {

    }

    @Override
    public FingerprintModel getFingerprintModel() {
        return FingerprintModelGenerator.generateFingerprintModel(this);
    }

    @Override
    public void doRelogin(String newAccessToken) {

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        locationUtils.deInitLocationBackground();
    }

    @Override
    public Intent getHomeIntent(Context context) {
        return MitraParentHomepageActivity.getCallingIntent(context);
    }

    @Override
    public Intent getTopProfileIntent(Context context, String userId) {
        return null;
    }

    @Override
    public Interceptor getChuckInterceptor() {
        return null;
    }

    @Override
    public Intent getShopPageIntent(Context context, String shopId) {
        return null;
    }

    @Override
    public boolean isLoginInactivePhoneLinkEnabled() {
        return false;
    }

    @Override
    public Intent getWithdrawIntent(Context context) {
        return null;
    }

    @Override
    public void goToApplinkActivity(Context context, String applink) {

    }

    @Override
    public void goToApplinkActivity(Activity activity, String applink, Bundle bundle) {

    }

    @Override
    public Intent getApplinkIntent(Context context, String applink) {
        try {
            return applinkDelegate.getIntent((Activity) context, applink);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isSupportApplink(String appLink) {
        return applinkDelegate.supportsUri(appLink);
    }

    @Override
    public ApplinkUnsupported getApplinkUnsupported(Activity activity) {
        return null;
    }

    @Override
    public ApplinkDelegate applinkDelegate() {
        return null;
    }
}
