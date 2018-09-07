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
import com.tokopedia.applink.ApplinkDelegate;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.ApplinkUnsupported;
import com.tokopedia.applink.TkpdApplinkDelegate;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.mitra.applink.MitraAppApplinkModuleLoader;
import com.tokopedia.mitra.fingerprint.FingerprintModelGenerator;
import com.tokopedia.mitra.fingerprint.LocationUtils;
import com.tokopedia.mitra.homepage.activity.MitraParentHomepageActivity;
import com.tokopedia.mitra.session.UserSessionImpl;
import com.tokopedia.network.NetworkRouter;
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
        super.onCreate();

        initDbFlow();

        instance = this;

        locationUtils = new LocationUtils(this);
        locationUtils.initLocationBackground();
        applinkDelegate = new TkpdApplinkDelegate(new MitraAppApplinkModuleLoader());
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
