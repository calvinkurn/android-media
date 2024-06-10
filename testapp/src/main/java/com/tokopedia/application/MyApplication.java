package com.tokopedia.application;


import static com.tokopedia.developer_options.mssdk.MsSdkPersistentKt.saveStringIntPairDefaultBlocking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.bytedance.applog.AppLog;
import com.bytedance.bdinstall.BDInstall;
import com.bytedance.mobsec.metasec.ov.MSConfig;
import com.bytedance.mobsec.metasec.ov.MSManager;
import com.bytedance.mobsec.metasec.ov.MSManagerUtils;
import com.tokopedia.abstraction.base.view.listener.DispatchTouchListener;
import com.tokopedia.abstraction.base.view.listener.TouchListenerActivity;
import com.tokopedia.analytics.btm.InitBtmSdk;
import com.tokopedia.analytics.byteio.AppLogActivityLifecycleCallback;
import com.tokopedia.analytics.byteio.AppLogAnalytics;
import com.tokopedia.analytics.performance.perf.performanceTracing.trace.Error;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import kotlin.jvm.functions.Function1;

import com.google.android.gms.security.ProviderInstaller;
import com.google.firebase.FirebaseApp;
import com.tkpd.remoteresourcerequest.task.ResourceDownloadManager;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.analytics.performance.fpi.FrameMetricsMonitoring;
import com.tokopedia.analytics.performance.perf.performanceTracing.AppPerformanceTrace;
import com.tokopedia.analytics.performance.perf.performanceTracing.config.DebugAppPerformanceConfig;
import com.tokopedia.analytics.performance.perf.performanceTracing.config.DefaultAppPerformanceConfig;
import com.tokopedia.analyticsdebugger.cassava.Cassava;
import com.tokopedia.analyticsdebugger.cassava.data.RemoteSpec;
import com.tokopedia.analyticsdebugger.debugger.FpmLogger;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.container.GTMAnalytics;
import com.tokopedia.core.analytics.container.MoengageAnalytics;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.developer_options.mssdk.MsSdkOptionConst;
import com.tokopedia.developer_options.mssdk.MsSdkPersistentKt;
import com.tokopedia.developer_options.notification.DevOptNotificationManager;
import com.tokopedia.developer_options.presentation.activity.MsSdkOptionActivity;
import com.tokopedia.developer_options.presentation.model.MsSdkUiModel;
import com.tokopedia.developer_options.presentation.viewholder.MsSdkViewHolder;
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.util.GqlActivityCallback;
import com.tokopedia.interceptors.authenticator.TkpdAuthenticatorGql;
import com.tokopedia.interceptors.refreshtoken.RefreshTokenGql;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.interfaces.LinkerRouter;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.network.ttnet.TTNetHelper;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfigInstance;
import com.tokopedia.remoteconfig.libra.LibraAbTest;
import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.tkpd.R;
import com.tokopedia.tokochat.config.util.TokoChatConnection;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.interfaces.ContextAnalytics;
import com.tokopedia.translator.manager.TranslatorManager;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSession;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by hendry on 25/06/18.
 */

public class MyApplication extends BaseMainApplication
        implements AbstractionRouter,
        NetworkRouter,
        ApplinkRouter,
        TkpdCoreRouter, LinkerRouter {

    // Used to loadWishlist the 'native-lib' library on application startup.
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onCreate() {
        setVersionCode();
        initFileDirConfig();

        TokopediaUrl.Companion.init(this); // generate base url

        GlobalConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
        GlobalConfig.PACKAGE_APPLICATION = getApplicationInfo().packageName;
        GlobalConfig.DEBUG = BuildConfig.DEBUG;
        GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        GlobalConfig.ENABLE_MACROBENCHMARK_UTIL = BuildConfig.ENABLE_MACROBENCHMARK_UTIL;

        com.tokopedia.config.GlobalConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
        com.tokopedia.config.GlobalConfig.PACKAGE_APPLICATION = getApplicationInfo().packageName;
        com.tokopedia.config.GlobalConfig.DEBUG = BuildConfig.DEBUG;
        com.tokopedia.config.GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;

        upgradeSecurityProvider();

        GraphqlClient.init(this, getAuthenticator());
        GraphqlClient.setContextData(getApplicationContext());
        

        initTranslator();
        NetworkClient.init(this);
        registerActivityLifecycleCallbacks(new GqlActivityCallback());
        registerActivityLifecycleCallbacks(new FrameMetricsMonitoring(this, true));
        registerActivityLifecycleCallbacks(new AppLogActivityLifecycleCallback());

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            new Cassava.Builder(this)
                    .setRemoteValidator(new RemoteSpec() {
                        @NonNull
                        @Override
                        public String getUrl() {
                            return TokopediaUrl.getInstance().getAPI();
                        }

                        @NonNull
                        @Override
                        public String getToken() {
                            return  getString(com.tokopedia.keys.R.string.thanos_token_key);
                        }
                    })
                    .setLocalRootPath("tracker")
                    .initialize();
            AppPerformanceTrace.Companion.init(
                    this,
                    new DebugAppPerformanceConfig(),
                    new Function1<Activity, Unit>() {
                        @Override
                        public Unit invoke(Activity activity) {
                            if (activity != null && activity instanceof TouchListenerActivity) {
                                ((TouchListenerActivity) activity).addListener(
                                        new DispatchTouchListener() {
                                            @Override
                    
                                            public void onDispatchTouch(MotionEvent ev) {
                                                AppPerformanceTrace.Companion.cancelPerformanceTracing(
                                                        new Error("err: User Touch. Performance trace cancelled"),
                                                        activity
                                                );
                                            }
                                        }
                                );
                            }
                            if (FrameMetricsMonitoring.Companion.getPerfWindow() != null) {
                                FrameMetricsMonitoring.Companion.getPerfWindow().updatePerformanceInfo();
                            }
                            return null;
                        }
                    },
                    new Function0<Unit>() {
                        @Override
                        public Unit invoke() {
                            if (FrameMetricsMonitoring.Companion.getPerfWindow() != null) {
                                FrameMetricsMonitoring.Companion.getPerfWindow().updatePerformanceInfo();
                            }
                            return null;
                        }
                    }
            );
        } else {
            AppPerformanceTrace.Companion.init(
                    this,
                    new DefaultAppPerformanceConfig(),
                    new Function1<Activity, Unit>() {
                        @Override
                        public Unit invoke(Activity activity) {
                            return null;
                        }
                    },
                    new Function0<Unit>() {
                        @Override
                        public Unit invoke() {
                            return null;
                        }
                    }
            );
        }
        TrackApp.initTrackApp(this);
        TrackApp.getInstance().registerImplementation(TrackApp.GTM, GTMAnalytics.class);
        // apps flyer is dummy
        TrackApp.getInstance().registerImplementation(TrackApp.APPSFLYER, AppsflyerAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.MOENGAGE, MoengageAnalytics.class);
        TrackApp.getInstance().initializeAllApis();
        AppLogAnalytics.init(this);
        TTNetHelper.initTTNet(this);
        LibraAbTest.init(this);

        PersistentCacheManager.init(this);
        RemoteConfigInstance.initAbTestPlatform(this);
        FpmLogger.init(this);

        com.tokopedia.akamai_bot_lib.UtilsKt.initAkamaiBotManager(this);

        super.onCreate();

        ResourceDownloadManager
                .Companion.getManager()
                .setBaseAndRelativeUrl("http://dummy.dummy", "dummy")
                .initialize(this, R.raw.dummy_description);



        IrisAnalytics.Companion.getInstance(this).initialize();
        LinkerManager.initLinkerManager(getApplicationContext()).setGAClientId(TrackingUtils.getClientID(getApplicationContext()));
        FirebaseApp.initializeApp(this);

        new DevOptNotificationManager(this).start();
        TokoChatConnection.init(this, false);

        UserSession userSession = new UserSession(this);
        initMsSDK();
        initBTMSDK();
    }

    private void initBTMSDK(){
        InitBtmSdk.INSTANCE.init(this);
    }

    private void initMsSDK(){
        // prevent initiate mssdk as it will crash debug
        if(GlobalConfig.DEBUG)
            return;

        // 构建config的Builder对象
        String appID = "573733";

        // license需要在风控接入平台生成，license错误会触发crash 云安全平台配置安全SDK License/鉴权文件
        String licenseStr = this.getString(com.tokopedia.keys.R.string.mssdk_key);

        //特定模式初始化接口
        //MSConfig.Builder builder = new MSConfig.Builder(appID, licenseStr, COLLECT_MODE_XXX);

        // 海外初始化参考，比如是Tiktok的使用COLLECT_MODE_TIKTOK_INIT完成初始化，海外使用国内的mode 会触发crash，表现为 "MSConfig init error!"
        MSConfig.Builder builder = new MSConfig.Builder(appID, licenseStr, MSConfig.COLLECT_MODE_TIKTOK_INIT);


        String deviceID = AppLog.getDid();
        String sessionID = AppLog.getSessionId();
        String installID = AppLog.getIid();

        // 配置config参数，通过builder生成config对象. 如果暂时获取不到，可以通过延迟配置，见下面说明
        builder.setDeviceID(deviceID);                                                    // 必填项，如果当前获取不到，可以在后续配置,如果设置空串会触发crash。注意⚠️：applog在新增设备上刚开始的返回值为空串，注册完成后需要重新设置
        builder.setClientType(MSConfig.CLIENT_TYPE_INHOUSE);          // 必填项，CLIENT_TYPE_INHOUSE代表字节内的应用，注意不要填写错误
        builder.setChannel(GlobalConfig.isAllowDebuggingTools() ? "local_test" : GlobalConfig.STORE_CHANNEL);                                                  // 必填项，有助于业务作弊分析，反爬虫使用
        builder.setInstallID(installID);                  // 必填项，如果初始化时候获取不到，可以不调用该接口，后续请业务通过MSManager 传入

        // 对于海外版，必填项。MSConfig.OVREGION_TYPE_SG代表新加坡，MSConfig.OVREGION_TYPE_VA代表美东。
        builder.setOVRegionType(MSConfig.OVREGION_TYPE_SG);

        // 构建MSConfig配置对象
        MSConfig config = builder.build();

        // 初始化SDK, 需要在Application的onCretae中进行初始化调用
        MSManagerUtils.init(this, config);

        BDInstall.addOnDataObserver(new com.bytedance.bdinstall.IDataObserver(){

            @Override
            public void onIdLoaded(String did, String iid, String ssid) {

            }

            @Override
            public void onRemoteIdGet(boolean changed, String oldDid, String newDid, String oldIid, String newIid, String oldSsid, String newSsid){
                if(changed){
                    // 如果当前无法获取deviceID和installID，可以在SDK初始化之后适当时间配置，方式如下所示;同时需要监听deviceID、installID的变更，及时传递给mssdk,并调用report接口，见注意事项
                    // 具体可以参考 AppLog接入文档 第5条 获取设备相关信息一节
                    MSManager mgr = MSManagerUtils.get(appID);
                    mgr.setDeviceID(newIid);
                    mgr.setInstallID(newIid);
                    mgr.report("did-iid-update");
                }
            }
        });

        // always override set mode to last selected msssdk mode
        saveStringIntPairDefaultBlocking(this);
    }

    private TkpdAuthenticatorGql getAuthenticator() {
        return new TkpdAuthenticatorGql(this, this, new UserSession(this), new RefreshTokenGql());
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

    @Override
    public void sendAnalyticsAnomalyResponse(String s, String s1, String s2, String s3, String s4) {

    }

    @Override
    public void logRefreshTokenException(String error, String type, String path, String accessToken) {

    }

    @Override
    public IAppNotificationReceiver getAppNotificationReceiver() {
        return null;
    }

    @Override
    public void onAppsFlyerInit() {

    }

    @Override
    public void refreshFCMTokenFromBackgroundToCM(String token, boolean force) {

    }

    @Override
    public void refreshFCMFromInstantIdService(String token) {

    }

    @Override
    public void onForceLogoutV2(Activity activity, int redirectionType, String url) {

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
    public void sendRefreshTokenAnalytics(String errorMessage) {

    }


    @Override
    public void showForceLogoutTokenDialog(String path) {

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
    public CacheManager getPersistentCacheManager() {
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
    public void onNewIntent(Context context, Intent intent) {

    }


    @Override
    public FingerprintModel getFingerprintModel() {
        return FingerprintModelGenerator.generateFingerprintModel(this);
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

    public void initFileDirConfig(){
        GlobalConfig.INTERNAL_CACHE_DIR = this.getCacheDir().getAbsolutePath();
        GlobalConfig.INTERNAL_FILE_DIR = this.getFilesDir().getAbsolutePath();
        GlobalConfig.EXTERNAL_CACHE_DIR = this.getExternalCacheDir() != null ? this.getExternalCacheDir().getAbsolutePath() : "";
        GlobalConfig.EXTERNAL_FILE_DIR = this.getExternalFilesDir(null) != null ? this.getExternalFilesDir(null).getAbsolutePath() : "";
    }

    private void initTranslator() {
        TranslatorManager.init(this, "");
    }

    @Override
    public boolean getBooleanRemoteConfig(String key, boolean defaultValue) {
        FirebaseRemoteConfigImpl remoteConfig = new FirebaseRemoteConfigImpl(this);
        return remoteConfig.getBoolean(key, defaultValue);
    }

    public static class AppsflyerAnalytics extends DummyAnalytics {

        public AppsflyerAnalytics(Context context) {
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

    @Override
    public void connectTokoChat(Boolean isFromLoginFlow) {

    }

    @Override
    public void disconnectTokoChat() {

    }

    @Override
    public void onRefreshCM(String token) {
        refreshFCMFromInstantIdService(token);
    }
}