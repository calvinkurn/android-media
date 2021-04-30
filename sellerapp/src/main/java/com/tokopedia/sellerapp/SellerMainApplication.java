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

import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;
import com.google.android.play.core.splitcompat.SplitCompat;
import com.tokopedia.abstraction.relic.NewRelicInteractionActCall;
import com.tokopedia.additional_check.subscriber.TwoFactorCheckerSubscriber;
import com.tokopedia.analyticsdebugger.debugger.FpmLogger;
import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.container.AppsflyerAnalytics;
import com.tokopedia.core.analytics.container.GTMAnalytics;
import com.tokopedia.core.analytics.container.MoengageAnalytics;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.developer_options.DevOptsSubscriber;
import com.tokopedia.device.info.DeviceInfo;
import com.tokopedia.encryption.security.AESEncryptorECB;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.keys.Keys;
import com.tokopedia.logger.LogManager;
import com.tokopedia.logger.LoggerProxy;
import com.tokopedia.moengage_wrapper.MoengageInteractor;
import com.tokopedia.moengage_wrapper.interfaces.MoengageInAppListener;
import com.tokopedia.moengage_wrapper.interfaces.MoengagePushListener;
import com.tokopedia.media.common.Loader;
import com.tokopedia.media.common.common.ToasterActivityLifecycle;
import com.tokopedia.pageinfopusher.PageInfoPusherSubscriber;
import com.tokopedia.prereleaseinspector.ViewInspectorSubscriber;
import com.tokopedia.remoteconfig.RemoteConfigInstance;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.remoteconfig.abtest.AbTestPlatform;
import com.tokopedia.sellerapp.deeplink.DeepLinkActivity;
import com.tokopedia.sellerapp.deeplink.DeepLinkHandlerActivity;
import com.tokopedia.sellerapp.fcm.AppNotificationReceiver;
import com.tokopedia.sellerapp.utils.SessionActivityLifecycleCallbacks;
import com.tokopedia.sellerapp.utils.timber.LoggerActivityLifecycleCallbacks;
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity;
import com.tokopedia.tokopatch.TokoPatch;
import com.tokopedia.track.TrackApp;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.utils.permission.SlicePermission;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import kotlin.Pair;
import kotlin.jvm.functions.Function1;
import timber.log.Timber;

import static com.tokopedia.utils.permission.SlicePermission.SELLER_ORDER_AUTHORITY;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class SellerMainApplication extends SellerRouterApplication implements
        MoengagePushListener, MoengageInAppListener {

    public static final String ANDROID_ROBUST_ENABLE = "android_sellerapp_robust_enable";
    private static final String ADD_BROTLI_INTERCEPTOR = "android_add_brotli_interceptor";
    private static final String REMOTE_CONFIG_SCALYR_KEY_LOG = "android_sellerapp_log_config_scalyr";
    private static final String REMOTE_CONFIG_NEW_RELIC_KEY_LOG = "android_sellerapp_log_config_new_relic";
    private static final String PARSER_SCALYR_SA = "android-seller-app-p%s";

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
        setVersionName();
        initFileDirConfig();
        FpmLogger.init(this);
        TokopediaUrl.Companion.init(this);
        generateSellerAppNetworkKeys();
        initRemoteConfig();
        initCacheManager();

        TrackApp.initTrackApp(this);

        TrackApp.getInstance().registerImplementation(TrackApp.GTM, GTMAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.APPSFLYER, AppsflyerAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.MOENGAGE, MoengageAnalytics.class);
        TrackApp.getInstance().initializeAllApis();

        super.onCreate();
        initLogManager();
        MoengageInteractor.INSTANCE.setPushListener(SellerMainApplication.this);
        MoengageInteractor.INSTANCE.setInAppListener(this);
        com.tokopedia.akamai_bot_lib.UtilsKt.initAkamaiBotManager(SellerMainApplication.this);
        GraphqlClient.setContextData(this);
        GraphqlClient.init(this, remoteConfig.getBoolean(ADD_BROTLI_INTERCEPTOR, false));
        NetworkClient.init(this);
        initializeAbTestVariant();

        initAppNotificationReceiver();
        registerActivityLifecycleCallbacks();
        initBlockCanary();
        TokoPatch.init(this);
        initSlicePermission();

        Loader.init(this);
    }

    private void initCacheManager() {
        PersistentCacheManager.init(this);
        cacheManager = PersistentCacheManager.instance;
    }

    private void initLogManager(){
        LogManager.init(SellerMainApplication.this, new LoggerProxy() {
            final AESEncryptorECB encryptor = new AESEncryptorECB();
            final SecretKey secretKey = encryptor.generateKey(com.tokopedia.sellerapp.utils.constants.Constants.ENCRYPTION_KEY);

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
        });
    }

    private void setVersionName() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            Pair<String, String> versions = AuthHelper.getVersionName(pInfo.versionName);
            String version = versions.getFirst();
            String suffixVersion = versions.getSecond();

            if (!version.equalsIgnoreCase(AuthHelper.ERROR)) {
                GlobalConfig.VERSION_NAME = version;
                com.tokopedia.config.GlobalConfig.VERSION_NAME = version;
                com.tokopedia.config.GlobalConfig.VERSION_NAME_SUFFIX = suffixVersion;
            } else {
                GlobalConfig.VERSION_NAME = pInfo.versionName;
                com.tokopedia.config.GlobalConfig.VERSION_NAME = pInfo.versionName;
            }
            com.tokopedia.config.GlobalConfig.RAW_VERSION_NAME = pInfo.versionName;// save raw version name
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initBlockCanary() {
        BlockCanary.install(context, new BlockCanaryContext()).start();
    }

    private void registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new NewRelicInteractionActCall());
        registerActivityLifecycleCallbacks(new LoggerActivityLifecycleCallbacks());
        registerActivityLifecycleCallbacks(new SessionActivityLifecycleCallbacks());
        if (GlobalConfig.isAllowDebuggingTools()) {
            registerActivityLifecycleCallbacks(new ViewInspectorSubscriber());
            registerActivityLifecycleCallbacks(new DevOptsSubscriber());
        }
        registerActivityLifecycleCallbacks(new TwoFactorCheckerSubscriber());
        registerActivityLifecycleCallbacks(new ToasterActivityLifecycle(this));
        registerActivityLifecycleCallbacks(new PageInfoPusherSubscriber());
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

    private void generateSellerAppNetworkKeys() {
        AuthUtil.KEY.KEY_CREDIT_CARD_VAULT = SellerAppNetworkKeys.CREDIT_CARD_VAULT_AUTH_KEY;
        AuthUtil.KEY.ZEUS_WHITELIST = SellerAppNetworkKeys.ZEUS_WHITELIST;
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

    private void initSlicePermission() {
        if (getSliceRemoteConfig()) {
            SlicePermission slicePermission = new SlicePermission();
            slicePermission.initPermission(this, SELLER_ORDER_AUTHORITY);
        }
    }

    private Boolean getSliceRemoteConfig() {
        return remoteConfig != null
                && remoteConfig.getBoolean(RemoteConfigKey.ENABLE_SLICE_ACTION_SELLER, false);
    }


    @Override
    public Class<?> getDeeplinkClass() {
        return DeepLinkActivity.class;
    }

}
