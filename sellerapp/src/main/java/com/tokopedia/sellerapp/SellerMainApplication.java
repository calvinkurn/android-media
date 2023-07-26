package com.tokopedia.sellerapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.work.Configuration;

import com.google.android.play.core.splitcompat.SplitCompat;
import com.tokopedia.abstraction.relic.NewRelicInteractionActCall;
import com.tokopedia.additional_check.subscriber.TwoFactorCheckerSubscriber;
import com.tokopedia.analytics.performance.fpi.FrameMetricsMonitoring;
import com.tokopedia.analytics.performance.util.EmbraceMonitoring;
import com.tokopedia.analyticsdebugger.cassava.Cassava;
import com.tokopedia.analyticsdebugger.cassava.data.RemoteSpec;
import com.tokopedia.analyticsdebugger.debugger.FpmLogger;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.container.AppsflyerAnalytics;
import com.tokopedia.core.analytics.container.GTMAnalytics;
import com.tokopedia.core.analytics.container.MoengageAnalytics;
import com.tokopedia.dev_monitoring_tools.DevMonitoring;
import com.tokopedia.developer_options.DevOptsSubscriber;
import com.tokopedia.developer_options.notification.DevOptNotificationManager;
import com.tokopedia.device.info.DeviceInfo;
import com.tokopedia.encryption.security.AESEncryptorECB;
import com.tokopedia.encryption.security.RSA;
import com.tokopedia.encryption.utils.RSAKeys;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.util.GqlActivityCallback;
import com.tokopedia.interceptors.authenticator.TkpdAuthenticatorGql;
import com.tokopedia.interceptors.refreshtoken.RefreshTokenGql;
import com.tokopedia.journeydebugger.JourneySubscriber;
import com.tokopedia.keys.Keys;
import com.tokopedia.logger.LogManager;
import com.tokopedia.logger.LoggerProxy;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.media.loader.internal.MediaLoaderActivityLifecycle;
import com.tokopedia.network.authentication.AuthHelper;
import com.tokopedia.notifications.settings.NotificationGeneralPromptLifecycleCallbacks;
import com.tokopedia.pageinfopusher.PageInfoPusherSubscriber;
import com.tokopedia.prereleaseinspector.ViewInspectorSubscriber;
import com.tokopedia.remoteconfig.RemoteConfigInstance;
import com.tokopedia.remoteconfig.abtest.AbTestPlatform;
import com.tokopedia.seller.active.common.features.sellerfeedback.SellerFeedbackScreenshot;
import com.tokopedia.sellerapp.deeplink.DeepLinkActivity;
import com.tokopedia.sellerapp.deeplink.DeepLinkHandlerActivity;
import com.tokopedia.sellerapp.fcm.AppNotificationReceiver;
import com.tokopedia.sellerapp.utils.SessionActivityLifecycleCallbacks;
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity;
import com.tokopedia.track.TrackApp;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSession;

import org.jetbrains.annotations.NotNull;

import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import io.embrace.android.embracesdk.Embrace;
import kotlin.Pair;
import kotlin.jvm.functions.Function1;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class SellerMainApplication extends SellerRouterApplication implements Configuration.Provider{

    private static final String ADD_BROTLI_INTERCEPTOR = "android_add_brotli_interceptor";
    private static final String REMOTE_CONFIG_SCALYR_KEY_LOG = "android_sellerapp_log_config_scalyr";
    private static final String REMOTE_CONFIG_NEW_RELIC_KEY_LOG = "android_sellerapp_log_config_v3_new_relic";
    private static final String REMOTE_CONFIG_EMBRACE_KEY_LOG = "android_sellerapp_log_config_embrace";
    private static final String PARSER_SCALYR_SA = "android-seller-app-p%s";
    private final String LEAK_CANARY_TOGGLE_SP_NAME = "mainapp_leakcanary_toggle";
    private final String LEAK_CANARY_TOGGLE_KEY = "key_leakcanary_toggle_seller";
    private final String STRICT_MODE_LEAK_PUBLISHER_TOGGLE_KEY = "key_strict_mode_leak_publisher_toggle_seller";
    private final boolean LEAK_CANARY_DEFAULT_TOGGLE = true;
    private final boolean STRICT_MODE_LEAK_PUBLISHER_DEFAULT_TOGGLE = false;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }


    @Override
    public void onCreate() {
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION;
        GlobalConfig.PACKAGE_APPLICATION = GlobalConfig.PACKAGE_SELLER_APP;
        setVersionCode();
        GlobalConfig.DEBUG = BuildConfig.DEBUG;
        GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        com.tokopedia.config.GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION;
        com.tokopedia.config.GlobalConfig.PACKAGE_APPLICATION = GlobalConfig.PACKAGE_SELLER_APP;
        GlobalConfig.LAUNCHER_ICON_RES_ID = R.mipmap.ic_launcher_sellerapp;
        com.tokopedia.config.GlobalConfig.DEBUG = BuildConfig.DEBUG;
        com.tokopedia.config.GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        com.tokopedia.config.GlobalConfig.APPLICATION_ID = BuildConfig.APPLICATION_ID;
        com.tokopedia.config.GlobalConfig.HOME_ACTIVITY_CLASS_NAME = SellerHomeActivity.class.getName();
        com.tokopedia.config.GlobalConfig.DEEPLINK_HANDLER_ACTIVITY_CLASS_NAME = DeepLinkHandlerActivity.class.getName();
        com.tokopedia.config.GlobalConfig.DEEPLINK_ACTIVITY_CLASS_NAME = DeepLinkActivity.class.getName();
        com.tokopedia.config.GlobalConfig.DEVICE_ID = DeviceInfo.getAndroidId(this);
        setVersionName();
        initFileDirConfig();
        FpmLogger.init(this);
        TokopediaUrl.Companion.init(this);
        initRemoteConfig();
        initCacheManager();
        initEmbrace();

        if (GlobalConfig.isAllowDebuggingTools()) {
            initCassava();
        }
        TrackApp.initTrackApp(this);

        TrackApp.getInstance().registerImplementation(TrackApp.GTM, GTMAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.APPSFLYER, AppsflyerAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.MOENGAGE, MoengageAnalytics.class);
        TrackApp.getInstance().initializeAllApis();

        super.onCreate();
        initLogManager();
        com.tokopedia.akamai_bot_lib.UtilsKt.initAkamaiBotManager(SellerMainApplication.this);
        GraphqlClient.setContextData(this);
        GraphqlClient.init(this, remoteConfig.getBoolean(ADD_BROTLI_INTERCEPTOR, false), getAuthenticator());
        NetworkClient.init(this);
        initializeAbTestVariant();

        initAppNotificationReceiver();
        registerActivityLifecycleCallbacks();

        setEmbraceUserId();
        EmbraceMonitoring.INSTANCE.setCarrierProperties(this);

        if (GlobalConfig.isAllowDebuggingTools()) {
            showDevOptNotification();
            initDevMonitoringTools();
        }
    }

    private void initCassava() {
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
    }

    private TkpdAuthenticatorGql getAuthenticator() {
        return new TkpdAuthenticatorGql(this, this, new UserSession(context), new RefreshTokenGql());
    }

    private void initCacheManager() {
        PersistentCacheManager.init(this);
        cacheManager = PersistentCacheManager.instance;
    }

    private void initLogManager(){
        LogManager.init(SellerMainApplication.this, new LoggerProxy() {
            final AESEncryptorECB encryptor = new AESEncryptorECB();
            final SecretKey secretKey = encryptor.generateKey(com.tokopedia.sellerapp.utils.constants.Constants.ENCRYPTION_KEY);

            final RSA encryptorRSA = new RSA();
            final RSAPrivateKey privateKeyRSA = encryptorRSA.stringToPrivateKey(RSAKeys.PRIVATE_RSA_KEY_STR);

            @Override
            public Function1<String, String> getDecrypt() {
                return new Function1<String, String>() {
                    @Override
                    public String invoke(String s) {
                        return encryptor.decrypt(s, secretKey);
                    }
                };
            }

            @Override
            public Function1<String, String> getEncrypt() {
                return new Function1<String, String>() {
                    @Override
                    public String invoke(String s) {
                        return encryptor.encrypt(s, secretKey);
                    }
                };
            }

            @Override
            public Function1<String, String> getDecryptNrKey() {
                return new Function1<String, String>() {
                    @Override
                    public String invoke(String s) {
                        return encryptorRSA.decrypt(s, privateKeyRSA, com.tokopedia.encryption.utils.Constants.RSA_OAEP_ALGORITHM);
                    }
                };
            }

            @NotNull
            @Override
            public String getVersionName() {
                return GlobalConfig.RAW_VERSION_NAME;
            }

            @Override
            public int getVersionCode() {
                return GlobalConfig.VERSION_CODE;
            }

            @NotNull
            @Override
            public String getScalyrToken() {
                return Keys.getAUTH_SCALYR_API_KEY();
            }

            @NotNull
            @Override
            public String getNewRelicToken() {
                return Keys.getAUTH_NEW_RELIC_API_KEY();
            }

            @NotNull
            @Override
            public String getNewRelicUserId() {
                return Keys.getAUTH_NEW_RELIC_USER_ID();
            }

            @Override
            public boolean isDebug() {
                return GlobalConfig.DEBUG;
            }

            @NotNull
            @Override
            public String getUserId() {
                return getUserSession().getUserId();
            }

            @NotNull
            @Override
            public String getParserScalyr() {
                return PARSER_SCALYR_SA;
            }

            @NotNull
            @Override
            public String getScalyrConfig() {
                return remoteConfig.getString(REMOTE_CONFIG_SCALYR_KEY_LOG);
            }

            @NotNull
            @Override
            public String getNewRelicConfig() {
                return remoteConfig.getString(REMOTE_CONFIG_NEW_RELIC_KEY_LOG);
            }

            @NotNull
            @Override
            public String getEmbraceConfig() {
                return remoteConfig.getString(REMOTE_CONFIG_EMBRACE_KEY_LOG);
            }
        });
    }

    private void initEmbrace() {
        Embrace.getInstance().start(this);
    }

    private void setVersionName() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            Pair<String, String> versions = AuthHelper.getVersionName(pInfo.versionName);
            String version = versions.getFirst();
            String suffixVersion = versions.getSecond();

            if (TextUtils.isEmpty(suffixVersion)) {
                GlobalConfig.VERSION_NAME = pInfo.versionName;
            } else {
                GlobalConfig.VERSION_NAME = version;
                GlobalConfig.VERSION_NAME_SUFFIX = suffixVersion;
            }
            GlobalConfig.RAW_VERSION_NAME = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new NewRelicInteractionActCall());
        registerActivityLifecycleCallbacks(new SessionActivityLifecycleCallbacks());
        if (GlobalConfig.isAllowDebuggingTools()) {
            registerActivityLifecycleCallbacks(new ViewInspectorSubscriber());
            registerActivityLifecycleCallbacks(new DevOptsSubscriber());
            registerActivityLifecycleCallbacks(new JourneySubscriber());
            registerActivityLifecycleCallbacks(new FrameMetricsMonitoring(this));
        }
        registerActivityLifecycleCallbacks(new TwoFactorCheckerSubscriber());
        registerActivityLifecycleCallbacks(new MediaLoaderActivityLifecycle(this));
        registerActivityLifecycleCallbacks(new PageInfoPusherSubscriber());
        registerActivityLifecycleCallbacks(new SellerFeedbackScreenshot(getApplicationContext()));
        registerActivityLifecycleCallbacks(new GqlActivityCallback());
        registerActivityLifecycleCallbacks(new NotificationGeneralPromptLifecycleCallbacks());
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

    protected AbTestPlatform.Listener getRemoteConfigListener() {
        return null;
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

    public int getCurrentVersion(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //Please do not delete this function to keep AppNotificationReceiver
    private void initAppNotificationReceiver() {
        AppNotificationReceiver appNotificationReceiver = new AppNotificationReceiver();
        String tag = appNotificationReceiver.getClass().getSimpleName();
        Log.d("Init %s", tag);
    }

    private void setEmbraceUserId() {
        if (getUserSession().isLoggedIn()) {
            Embrace.getInstance().setUserIdentifier(getUserSession().getUserId());
        }
    }

    @SuppressLint("RestrictedApi")
    @NonNull
    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder().setInitializationExceptionHandler(throwable -> {
            Map<String, String> map = new HashMap<>();
            map.put("type", "init");
            map.put("error", Log.getStackTraceString(throwable));
            ServerLogger.log(Priority.P1, "WORK_MANAGER", map);
        }).build();
    }

    private void showDevOptNotification() {
        new DevOptNotificationManager(this).start();
    }

    private void initDevMonitoringTools(){
        DevMonitoring devMonitoring = new DevMonitoring(SellerMainApplication.this);
        devMonitoring.initANRWatcher();
        devMonitoring.initLeakCanary(getLeakCanaryToggleValue(), getStrictModeLeakPublisherToggleValue(), this);
    }

    private boolean getLeakCanaryToggleValue() {
        return getSharedPreferences(LEAK_CANARY_TOGGLE_SP_NAME, MODE_PRIVATE).getBoolean(LEAK_CANARY_TOGGLE_KEY, LEAK_CANARY_DEFAULT_TOGGLE);
    }

    private boolean getStrictModeLeakPublisherToggleValue() {
        return getSharedPreferences(LEAK_CANARY_TOGGLE_SP_NAME, MODE_PRIVATE).getBoolean(STRICT_MODE_LEAK_PUBLISHER_TOGGLE_KEY, STRICT_MODE_LEAK_PUBLISHER_DEFAULT_TOGGLE);
    }
}
