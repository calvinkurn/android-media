package com.tokopedia.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.security.ProviderInstaller;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.applink.ApplinkDelegate;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.ApplinkUnsupported;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.cacheapi.domain.interactor.CacheApiWhiteListUseCase;
import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.tkpd.network.DataSource;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.interfaces.ContextAnalytics;
import com.tokopedia.user.session.UserSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by hendry on 25/06/18.
 */

public class MyApplication extends BaseMainApplication
        implements AbstractionRouter,
        NetworkRouter,
        ApplinkRouter {

    // Used to loadWishlist the 'native-lib' library on application startup.
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate() {
        GlobalConfig.PACKAGE_APPLICATION = getApplicationInfo().packageName;
        GlobalConfig.DEBUG = BuildConfig.DEBUG;
        GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        com.tokopedia.config.GlobalConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
        com.tokopedia.config.GlobalConfig.PACKAGE_APPLICATION = getApplicationInfo().packageName;
        com.tokopedia.config.GlobalConfig.DEBUG = BuildConfig.DEBUG;
        com.tokopedia.config.GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;

        // for staging-only
        /*TokopediaUrl.Companion.setEnvironment(this, Env.STAGING);
        TokopediaUrl.Companion.deleteInstance();
        TokopediaUrl.Companion.init(this);*/

        upgradeSecurityProvider();

        GraphqlClient.init(this);
        NetworkClient.init(this);
        TrackApp.initTrackApp(this);
        TrackApp.getInstance().registerImplementation(TrackApp.GTM, GTMAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.APPSFLYER, AppsflyerAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.MOENGAGE, MoengageAnalytics.class);
        TrackApp.getInstance().initializeAllApis();

        PersistentCacheManager.init(this);
        super.onCreate();
        initCacheApi();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void upgradeSecurityProvider() {
        try {
            ProviderInstaller.installIfNeededAsync(this, new ProviderInstaller.ProviderInstallListener() {
                @Override
                public void onProviderInstalled() {
                    // Do nothing
                }

                @Override
                public void onProviderInstallFailed(int i, Intent intent) {
                    // Do nothing
                }
            });
        } catch (Throwable t) {
            // Do nothing
        }
    }

    public static class GTMAnalytics extends DummyAnalytics {

        public GTMAnalytics(Context context) {
            super(context);
        }
    }

    public static class AppsflyerAnalytics extends DummyAnalytics {

        public AppsflyerAnalytics(Context context) {
            super(context);
        }
    }

    public static class MoengageAnalytics extends DummyAnalytics {

        public MoengageAnalytics(Context context) {
            super(context);
        }
    }

    public static abstract class DummyAnalytics extends ContextAnalytics {

        public DummyAnalytics(Context context) {
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

    private void initCacheApi() {
        new CacheApiWhiteListUseCase(this).executeSync(CacheApiWhiteListUseCase.createParams(
                getWhiteList(), String.valueOf(System.currentTimeMillis())));
    }

    public static List<CacheApiWhiteListDomain> getWhiteList() {
        List<CacheApiWhiteListDomain> cacheApiWhiteList = new ArrayList<>();
        cacheApiWhiteList.addAll(getShopWhiteList());
        return cacheApiWhiteList;
    }

    public static final List<CacheApiWhiteListDomain> getShopWhiteList() {
        List<CacheApiWhiteListDomain> cacheApiWhiteList = new ArrayList<>();
        return cacheApiWhiteList;
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
    public void gcmUpdate() throws IOException {

    }

    @Override
    public void refreshToken() throws IOException {

    }

    /**
     * Use UserSession object from library usersession directly.
     */
    @Deprecated
    public UserSession getSession() {
        com.tokopedia.user.session.UserSession userSession =
                new com.tokopedia.user.session.UserSession(this);
        return new UserSession(this) {
            @Override
            public String getAccessToken() {
                return userSession.getAccessToken();
            }

            @Override
            public String getFreshToken() {
                return userSession.getFreshToken();
            }

            @Override
            public String getUserId() {
                return userSession.getUserId();
            }

            @Override
            public String getDeviceId() {
                return userSession.getDeviceId();
            }

            @Override
            public boolean isLoggedIn() {
                return userSession.isLoggedIn();
            }

            @Override
            public String getShopId() {
                return userSession.getShopId();
            }

            @Override
            public boolean hasShop() {
                return userSession.hasShop();
            }

            @Override
            public String getName() {
                return userSession.getName();
            }

            @Override
            public String getProfilePicture() {
                return userSession.getProfilePicture();
            }

            @Override
            public boolean isMsisdnVerified() {
                return userSession.isMsisdnVerified();
            }

        };
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
        return null;
    }


    @Override
    public void logInvalidGrant(Response response) {

    }

    @Override
    public boolean isAllowLogOnChuckInterceptorNotification() {
        return false;
    }


    @Override
    public FingerprintModel getFingerprintModel() {
        return DataSource.generateFingerprintModel();
    }

    @Override
    public void doRelogin(String newAccessToken) {

    }

    /**
     * use RouteManager.route(context,applink) directly instead
     */
    @Deprecated
    @Override
    public void goToApplinkActivity(Context context, String applink) {
        Toast.makeText(getApplicationContext(), "deprecated - GO TO " + applink, Toast.LENGTH_LONG).show();
        RouteManager.route(context, applink);
    }

    /**
     * use RouteManager.route(context,applink) directly instead
     */
    @Deprecated
    @Override
    public void goToApplinkActivity(Activity activity, String applink, Bundle bundle) {
        Toast.makeText(getApplicationContext(), "deprecated - GO TO " + applink, Toast.LENGTH_LONG).show();
        RouteManager.route(activity, applink);
    }

    /**
     * use RouteManager.getIntent(context,applink) directly instead
     */
    @Deprecated
    @Override
    public Intent getApplinkIntent(Context context, String applink) {
        return RouteManager.getIntent(context, applink);
    }

    @Deprecated
    @Override
    public boolean isSupportApplink(String appLink) {
        Toast.makeText(getApplicationContext(), "check for airbnb deeplink " + appLink, Toast.LENGTH_LONG).show();
        return false;
    }

    @Deprecated
    @Override
    public ApplinkUnsupported getApplinkUnsupported(Activity activity) {
        return null;
    }

    @Deprecated
    @Override
    public ApplinkDelegate applinkDelegate() {
        return null;
    }

    @Override
    public void onActivityDestroyed(String screenName, Activity baseActivity) {

    }
}
