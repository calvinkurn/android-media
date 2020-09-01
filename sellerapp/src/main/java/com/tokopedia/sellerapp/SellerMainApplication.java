package com.tokopedia.sellerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.URLUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.bugsnag.android.BeforeNotify;
import com.bugsnag.android.Bugsnag;
import com.bugsnag.android.Error;
import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;
import com.google.android.play.core.splitcompat.SplitCompat;
import com.moengage.inapp.InAppManager;
import com.moengage.inapp.InAppMessage;
import com.moengage.inapp.InAppTracker;
import com.moengage.pushbase.push.MoEPushCallBacks;
import com.tokopedia.analyticsdebugger.debugger.FpmLogger;
import com.tokopedia.cacheapi.domain.interactor.CacheApiWhiteListUseCase;
import com.tokopedia.cacheapi.util.CacheApiLoggingUtils;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.container.AppsflyerAnalytics;
import com.tokopedia.core.analytics.container.GTMAnalytics;
import com.tokopedia.core.analytics.container.MoengageAnalytics;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.device.info.DeviceInfo;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.remoteconfig.RemoteConfigInstance;
import com.tokopedia.remoteconfig.abtest.AbTestPlatform;
import com.tokopedia.sellerapp.deeplink.DeepLinkActivity;
import com.tokopedia.sellerapp.deeplink.DeepLinkHandlerActivity;
import com.tokopedia.sellerapp.fcm.AppNotificationReceiver;
import com.tokopedia.sellerapp.utils.CacheApiWhiteList;
import com.tokopedia.sellerapp.utils.SessionActivityLifecycleCallbacks;
import com.tokopedia.sellerapp.utils.timber.LoggerActivityLifecycleCallbacks;
import com.tokopedia.sellerapp.utils.timber.TimberWrapper;
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity;
import com.tokopedia.prereleaseinspector.ViewInspectorSubscriber;
import com.tokopedia.track.TrackApp;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;
import com.tokopedia.tokopatch.TokoPatch;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class SellerMainApplication extends SellerRouterApplication implements MoEPushCallBacks.OnMoEPushNavigationAction,
        InAppManager.InAppMessageListener {

    public static final String ANDROID_ROBUST_ENABLE = "android_sellerapp_robust_enable";
    private static final String ADD_BROTLI_INTERCEPTOR = "android_add_brotli_interceptor";

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onInAppShown(InAppMessage message) {
        InAppTracker.getInstance(this).trackInAppClicked(message);
    }

    @Override
    public boolean showInAppMessage(InAppMessage message) {
        InAppTracker.getInstance(this).trackInAppClicked(message);
        return false;
    }

    @Override
    public void onInAppClosed(InAppMessage message) {

    }

    @Override
    public boolean onInAppClick(@Nullable String screenName, @Nullable Bundle extras, @Nullable Uri deepLinkUri) {
        return handleClick(screenName, extras, deepLinkUri);
    }

    @Override
    public boolean onClick(@Nullable String screenName, @Nullable Bundle extras, @Nullable Uri deepLinkUri) {
        return handleClick(screenName, extras, deepLinkUri);
    }

    private boolean handleClick(@Nullable String screenName, @Nullable Bundle extras, @Nullable Uri deepLinkUri) {
        if (deepLinkUri != null) {
            Timber.d("FCM moengage SELLER clicked " + deepLinkUri.toString());
            if (URLUtil.isNetworkUrl(deepLinkUri.toString())) {
                Intent intent = new Intent(this, DeepLinkActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse(deepLinkUri.toString()));
                startActivity(intent);

            } else if (Constants.Schemes.APPLINKS_SELLER.equals(deepLinkUri.getScheme())) {
                Intent intent = new Intent(this, DeepLinkHandlerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse(deepLinkUri.toString()));
                startActivity(intent);
            } else {
                Timber.d("FCM entered no one");
            }

            return true;
        } else {
            return false;
        }

    }

    @Override
    public void onCreate() {
        initBugsnag();
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION;
        GlobalConfig.PACKAGE_APPLICATION = GlobalConfig.PACKAGE_SELLER_APP;
        setVersionCode();
        GlobalConfig.DEBUG = BuildConfig.DEBUG;
        GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        com.tokopedia.config.GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION;
        com.tokopedia.config.GlobalConfig.PACKAGE_APPLICATION = GlobalConfig.PACKAGE_SELLER_APP;
        com.tokopedia.config.GlobalConfig.DEBUG = BuildConfig.DEBUG;
        com.tokopedia.config.GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        com.tokopedia.config.GlobalConfig.APPLICATION_ID = BuildConfig.APPLICATION_ID;
        com.tokopedia.config.GlobalConfig.HOME_ACTIVITY_CLASS_NAME = SellerHomeActivity.class.getName();
        com.tokopedia.config.GlobalConfig.DEEPLINK_HANDLER_ACTIVITY_CLASS_NAME = DeepLinkHandlerActivity.class.getName();
        com.tokopedia.config.GlobalConfig.DEEPLINK_ACTIVITY_CLASS_NAME = DeepLinkActivity.class.getName();
        com.tokopedia.config.GlobalConfig.DEVICE_ID = DeviceInfo.getAndroidId(this);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            com.tokopedia.config.GlobalConfig.VERSION_NAME = pInfo.versionName;
            com.tokopedia.config.GlobalConfig.VERSION_NAME = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        FpmLogger.init(this);
        TokopediaUrl.Companion.init(this);
        generateSellerAppNetworkKeys();
        initRemoteConfig();
        TrackApp.initTrackApp(this);

        TrackApp.getInstance().registerImplementation(TrackApp.GTM, GTMAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.APPSFLYER, AppsflyerAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.MOENGAGE, MoengageAnalytics.class);
        TrackApp.getInstance().initializeAllApis();

        PersistentCacheManager.init(this);

        TimberWrapper.init(this);
        super.onCreate();
        MoEPushCallBacks.getInstance().setOnMoEPushNavigationAction(this);
        InAppManager.getInstance().setInAppListener(this);
        initCacheApi();
        GraphqlClient.init(this, remoteConfig.getBoolean(ADD_BROTLI_INTERCEPTOR, false));
        NetworkClient.init(this);
        initializeAbTestVariant();

        initAppNotificationReceiver();
        registerActivityLifecycleCallbacks();
        initBlockCanary();
//        TokoPatch.init(this);
    }

    private void initBugsnag() {
        Bugsnag.init(this);
        Bugsnag.beforeNotify(new BeforeNotify() {
            @Override
            public boolean run(Error error) {
                UserSessionInterface userSession = new UserSession(SellerMainApplication.this);
                error.setUser(userSession.getUserId(), userSession.getEmail(), userSession.getName());
                error.addToTab("squad", "package", getPackageName(error));
                return true;
            }

            private String getPackageName(Error error) {
                String packageName = "";
                String errorText = error.getException().getMessage();
                String startText = "/com.tokopedia.";
                int startIndex = errorText.indexOf(startText);
                if (startIndex > 0) {
                    int endIndex = errorText.indexOf(".", startIndex + startText.length());
                    packageName = errorText.substring(startIndex + 1, endIndex);
                }
                return packageName;
            }
        });
    }

    public void initBlockCanary(){
        BlockCanary.install(context, new BlockCanaryContext()).start();
    }

    private void registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new LoggerActivityLifecycleCallbacks());
        registerActivityLifecycleCallbacks(new SessionActivityLifecycleCallbacks());
        registerActivityLifecycleCallbacks(new ViewInspectorSubscriber());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        SplitCompat.install(this);
    }

    private void initializeAbTestVariant() {
        SharedPreferences sharedPreferences = getSharedPreferences(AbTestPlatform.Companion.getSHARED_PREFERENCE_AB_TEST_PLATFORM(), Context.MODE_PRIVATE);
        Long timestampAbTest = sharedPreferences.getLong(AbTestPlatform.Companion.getKEY_SP_TIMESTAMP_AB_TEST(), 0);
        RemoteConfigInstance.initAbTestPlatform(this);
        Long current = new Date().getTime();

        if (current >= timestampAbTest + TimeUnit.HOURS.toMillis(1)) {
            RemoteConfigInstance.getInstance().getABTestPlatform().fetch(getRemoteConfigListener());
        }
    }

    protected AbTestPlatform.Listener getRemoteConfigListener() { return null; }

    private void setVersionCode() {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            GlobalConfig.VERSION_CODE = pInfo.versionCode;
            com.tokopedia.config.GlobalConfig.VERSION_CODE = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            GlobalConfig.VERSION_CODE = BuildConfig.VERSION_CODE;
            com.tokopedia.config.GlobalConfig.VERSION_CODE = BuildConfig.VERSION_CODE;
        }
    }

    private void generateSellerAppNetworkKeys() {
        AuthUtil.KEY.KEY_CREDIT_CARD_VAULT = SellerAppNetworkKeys.CREDIT_CARD_VAULT_AUTH_KEY;
        AuthUtil.KEY.ZEUS_WHITELIST = SellerAppNetworkKeys.ZEUS_WHITELIST;
    }

    private void initCacheApi() {
        CacheApiLoggingUtils.setLogEnabled(GlobalConfig.isAllowDebuggingTools());
        new CacheApiWhiteListUseCase(this).executeSync(
                CacheApiWhiteListUseCase.createParams(
                        CacheApiWhiteList.getWhiteList(),
                        String.valueOf(getCurrentVersion(getApplicationContext())))
        );
    }

    //Please do not delete this function to keep AppNotificationReceiver
    private void initAppNotificationReceiver() {
        AppNotificationReceiver appNotificationReceiver = new AppNotificationReceiver();
        String tag = appNotificationReceiver.getClass().getSimpleName();
        Log.d("Init %s", tag);
    }

    @Override
    public Class<?> getDeeplinkClass() {
        return DeepLinkActivity.class;
    }

}
