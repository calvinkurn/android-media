package com.tokopedia.tkpd.app;

import static android.os.Process.killProcess;
import static com.tokopedia.unifyprinciples.GetTypefaceKt.getTypeface;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.chuckerteam.chucker.api.Chucker;
import com.chuckerteam.chucker.api.ChuckerCollector;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.newrelic.agent.android.NewRelic;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.base.view.appupdate.AppUpdateDialogBuilder;
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate;
import com.tokopedia.abstraction.base.view.appupdate.FirebaseRemoteAppForceUpdate;
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate;
import com.tokopedia.abstraction.base.view.model.InAppCallback;
import com.tokopedia.abstraction.newrelic.NewRelicInteractionActCall;
import com.tokopedia.additional_check.subscriber.TwoFactorCheckerSubscriber;
import com.tokopedia.analytics.mapper.model.EmbraceConfig;
import com.tokopedia.analytics.performance.util.EmbraceMonitoring;
import com.tokopedia.analyticsdebugger.cassava.Cassava;
import com.tokopedia.analyticsdebugger.cassava.data.RemoteSpec;
import com.tokopedia.analyticsdebugger.debugger.FpmLogger;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalPromo;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.container.AppsflyerAnalytics;
import com.tokopedia.core.analytics.container.GTMAnalytics;
import com.tokopedia.core.analytics.container.MoengageAnalytics;
import com.tokopedia.core.database.CoreLegacyDbFlowDatabase;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.customer_mid_app.R;
import com.tokopedia.dev_monitoring_tools.DevMonitoring;
import com.tokopedia.dev_monitoring_tools.beta.BetaSignActivityLifecycleCallbacks;
import com.tokopedia.dev_monitoring_tools.session.SessionActivityLifecycleCallbacks;
import com.tokopedia.dev_monitoring_tools.ui.JankyFrameActivityLifecycleCallbacks;
import com.tokopedia.developer_options.DevOptsSubscriber;
import com.tokopedia.developer_options.stetho.StethoUtil;
import com.tokopedia.device.info.DeviceInfo;
import com.tokopedia.devicefingerprint.datavisor.lifecyclecallback.DataVisorLifecycleCallbacks;
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator;
import com.tokopedia.encryption.security.AESEncryptorECB;
import com.tokopedia.encryption.security.RSA;
import com.tokopedia.encryption.utils.RSAKeys;
import com.tokopedia.graphql.util.GqlActivityCallback;
import com.tokopedia.inappupdate.InAppUpdateLifecycleCallback;
import com.tokopedia.journeydebugger.JourneySubscriber;
import com.tokopedia.keys.Keys;
import com.tokopedia.logger.LogManager;
import com.tokopedia.logger.LoggerProxy;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.media.common.Loader;
import com.tokopedia.media.common.common.MediaLoaderActivityLifecycle;
import com.tokopedia.network.authentication.AuthHelper;
import com.tokopedia.notifications.inApp.CMInAppManager;
import com.tokopedia.notifications.settings.NotificationGeneralPromptLifecycleCallbacks;
import com.tokopedia.pageinfopusher.PageInfoPusherSubscriber;
import com.tokopedia.prereleaseinspector.ViewInspectorSubscriber;
import com.tokopedia.promotionstarget.presentation.subscriber.GratificationSubscriber;
import com.tokopedia.remoteconfig.RemoteConfigInstance;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.remoteconfig.abtest.AbTestPlatform;
import com.tokopedia.shakedetect.ShakeDetectManager;
import com.tokopedia.shakedetect.ShakeSubscriber;
import com.tokopedia.telemetry.TelemetryActLifecycleCallback;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;
import com.tokopedia.tkpd.fcm.ApplinkResetReceiver;
import com.tokopedia.tkpd.nfc.NFCSubscriber;
import com.tokopedia.tkpd.utils.NewRelicConstants;
import com.tokopedia.track.TrackApp;
import com.tokopedia.unifyprinciples.Typography;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;
import com.tokopedia.tokopatch.TokoPatch;

import org.jetbrains.annotations.NotNull;

import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import io.embrace.android.embracesdk.Embrace;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import timber.log.Timber;

import com.tokopedia.developer_options.notification.DevOptNotificationManager;

/**
 * Created by ricoharisin on 11/11/16.
 */

public abstract class ConsumerMainApplication extends ConsumerRouterApplication {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private final String NOTIFICATION_CHANNEL_NAME = "Promo";
    private final String NOTIFICATION_CHANNEL_NAME_BTS_ONE = "Promo BTS 1";
    private final String NOTIFICATION_CHANNEL_NAME_BTS_TWO = "Promo BTS 2";
    private final String NOTIFICATION_CHANNEL_ID = "custom_sound";
    private final String NOTIFICATION_CHANNEL_ID_BTS_ONE = "custom_sound_bts_one";
    private final String NOTIFICATION_CHANNEL_ID_BTS_TWO = "custom_sound_bts_two";
    private final String NOTIFICATION_CHANNEL_DESC = "notification channel for custom sound.";
    private final String NOTIFICATION_CHANNEL_DESC_BTS_ONE = "notification channel for custom sound with BTS tone";
    private final String NOTIFICATION_CHANNEL_DESC_BTS_TWO = "notification channel for custom sound with different BTS tone";
    private static final String REMOTE_CONFIG_SCALYR_KEY_LOG = "android_customerapp_log_config_scalyr";
    private static final String REMOTE_CONFIG_NEW_RELIC_KEY_LOG = "android_customerapp_log_config_v3_new_relic";
    private static final String REMOTE_CONFIG_EMBRACE_KEY_LOG = "android_customerapp_log_config_embrace";
    private static final String REMOTE_CONFIG_TELEMETRY_ENABLED = "android_telemetry_enabled";
    private static final String PARSER_SCALYR_MA = "android-main-app-p%s";
    private static final String ENABLE_ASYNC_AB_TEST = "android_enable_async_abtest";
    private final String LEAK_CANARY_TOGGLE_SP_NAME = "mainapp_leakcanary_toggle";
    private final String LEAK_CANARY_TOGGLE_KEY = "key_leakcanary_toggle";
    private final String STRICT_MODE_LEAK_PUBLISHER_TOGGLE_KEY = "key_strict_mode_leak_publisher_toggle";
    private final boolean LEAK_CANARY_DEFAULT_TOGGLE = true;
    private final boolean STRICT_MODE_LEAK_PUBLISHER_DEFAULT_TOGGLE = false;

    GratificationSubscriber gratificationSubscriber;

    @Override
    public void onCreate() {
        initConfigValues();
        initializeSdk();
        initRemoteConfig();
        TokopediaUrl.Companion.init(this); // generate base url
        initCacheManager();

        if (GlobalConfig.isAllowDebuggingTools()) {
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
        TrackApp.initTrackApp(this);
        TrackApp.getInstance().registerImplementation(TrackApp.GTM, GTMAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.APPSFLYER, AppsflyerAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.MOENGAGE, MoengageAnalytics.class);
        TrackApp.getInstance().initializeAllApis();
        com.tokopedia.akamai_bot_lib.UtilsKt.initAkamaiBotManager(ConsumerMainApplication.this);
        createAndCallPreSeq();
        super.onCreate();
        initRobust();
        createAndCallPostSeq();
        initializeAbTestVariant();
        createAndCallFetchAbTest();
        createAndCallFontLoad();

        registerActivityLifecycleCallbacks();
        checkAppPackageNameAsync();

        Loader.init(this);
        initializationNewRelic();
        if (getUserSession().isLoggedIn()) {
            Embrace.getInstance().setUserIdentifier(getUserSession().getUserId());
        }
        EmbraceMonitoring.INSTANCE.setCarrierProperties(this);

        Typography.Companion.setFontTypeOpenSauceOne(true);

        showDevOptNotification();
    }

    private void initRobust() {
        if(remoteConfig.getBoolean(RemoteConfigKey.CUSTOMER_ENABLE_ROBUST, false)) {
            TokoPatch.init(this);
        }
    }

    private void initializationNewRelic() {
        if (!remoteConfig.getBoolean(RemoteConfigKey.ENABLE_INIT_NR_IN_ACTIVITY)) {
            WeaveInterface initNrWeave = new WeaveInterface() {
                @NotNull
                @Override
                public Object execute() {
                    NewRelic.withApplicationToken(Keys.NEW_RELIC_TOKEN_MA).start(ConsumerMainApplication.this);
                    return true;
                }
            };
            Weaver.Companion.executeWeaveCoRoutineNow(initNrWeave);
        }
    }

    private void checkAppPackageNameAsync() {
        WeaveInterface checkAppPackageNameWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                if (!isPackageNameValid() || !isVersionNameValid()) {
                    killProcess(android.os.Process.myPid());
                }
                return true;
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(checkAppPackageNameWeave, RemoteConfigKey.ENABLE_ASYNC_CHECKAPPSIGNATURE, this, true);
    }

    private boolean isPackageNameValid() {
        boolean packageNameValid = this.getPackageName().equals(getOriginalPackageApp());
        if (!packageNameValid) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("packageName", this.getPackageName());
            ServerLogger.log(Priority.P1, "APP_SIGNATURE_FAILED", messageMap);
        }
        return packageNameValid;
    }

    private boolean isVersionNameValid() {
        String numberRegex = ".*[0-9].*";
        return com.tokopedia.config.GlobalConfig.VERSION_NAME.matches(numberRegex) &&
                com.tokopedia.config.GlobalConfig.RAW_VERSION_NAME.matches(numberRegex);
    }

    protected abstract String getOriginalPackageApp();

    private void initCacheManager() {
        PersistentCacheManager.init(this);
        cacheManager = PersistentCacheManager.instance;
    }

    public void registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ShakeSubscriber(getApplicationContext(), new ShakeDetectManager.Callback() {
            @Override
            public void onShakeDetected(boolean isLongShake) {
                openShakeDetectCampaignPage(isLongShake);
            }
        }));

        registerActivityLifecycleCallbacks(new BetaSignActivityLifecycleCallbacks());
        registerActivityLifecycleCallbacks(new NFCSubscriber());
        registerActivityLifecycleCallbacks(new SessionActivityLifecycleCallbacks());
        if (GlobalConfig.isAllowDebuggingTools()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                registerActivityLifecycleCallbacks(new JankyFrameActivityLifecycleCallbacks.Builder().build());
            }
            registerActivityLifecycleCallbacks(new ViewInspectorSubscriber());
            registerActivityLifecycleCallbacks(new DevOptsSubscriber());
            registerActivityLifecycleCallbacks(new JourneySubscriber());
        }
        registerActivityLifecycleCallbacks(new TwoFactorCheckerSubscriber());
        registerActivityLifecycleCallbacks(new MediaLoaderActivityLifecycle(this));
        registerActivityLifecycleCallbacks(new PageInfoPusherSubscriber());
        registerActivityLifecycleCallbacks(new NewRelicInteractionActCall(getUserSession()));
        registerActivityLifecycleCallbacks(new GqlActivityCallback());
        registerActivityLifecycleCallbacks(new DataVisorLifecycleCallbacks());
        registerActivityLifecycleCallbacks(new TelemetryActLifecycleCallback(new Function0<Boolean>() {
            @Override
            public Boolean invoke() {
                return remoteConfig.getBoolean(REMOTE_CONFIG_TELEMETRY_ENABLED, true);
            }
        }));
        registerActivityLifecycleCallbacks(new InAppUpdateLifecycleCallback(new Function2<Activity, Function1<? super Boolean, Unit>, Unit>() {
            @Override
            public Unit invoke(Activity activity, Function1<? super Boolean, Unit> onSuccessCheckAppListener) {
                onCheckAppUpdateRemoteConfig(activity, onSuccessCheckAppListener);
                return null;
            }
        }));
        registerActivityLifecycleCallbacks(new NotificationGeneralPromptLifecycleCallbacks());
    }

    private void onCheckAppUpdateRemoteConfig(Activity activity, Function1<? super Boolean, Unit> onSuccessCheckAppListener) {
        ApplicationUpdate appUpdate = new FirebaseRemoteAppForceUpdate(activity);
        InAppCallback inAppCallback = null;
        if (activity instanceof InAppCallback) {
            inAppCallback = (InAppCallback) activity;
        }
        InAppCallback finalInAppCallback = inAppCallback;
        appUpdate.checkApplicationUpdate(new ApplicationUpdate.OnUpdateListener() {
            @Override
            public void onNeedUpdate(DetailUpdate detail) {
                AppUpdateDialogBuilder appUpdateDialogBuilder = new AppUpdateDialogBuilder(
                        activity,
                        detail, new AppUpdateDialogBuilder.Listener() {
                    @Override
                    public void onPositiveButtonClicked(DetailUpdate detail) {
                        if (finalInAppCallback != null) {
                            finalInAppCallback.onPositiveButtonInAppClicked(detail);
                        }
                    }

                    @Override
                    public void onNegativeButtonClicked(DetailUpdate detail) {
                        if (finalInAppCallback != null) {
                            finalInAppCallback.onNegativeButtonInAppClicked(detail);
                        }
                    }
                });
                if (!activity.isFinishing() && !activity.isDestroyed()) {
                    appUpdateDialogBuilder.getAlertDialog().show();
                    if (finalInAppCallback != null) {
                        finalInAppCallback.onNeedUpdateInApp(detail);
                    }
                    onSuccessCheckAppListener.invoke(true);
                }
            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onNotNeedUpdate() {
                if (finalInAppCallback != null) {
                    finalInAppCallback.onNotNeedUpdateInApp();
                }
                onSuccessCheckAppListener.invoke(false);
            }
        });
    }

    private void createAndCallPreSeq() {
        //don't convert to lambda does not work in kit kat
        WeaveInterface preWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Boolean execute() {
                return executePreCreateSequence();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(preWeave, RemoteConfigKey.ENABLE_SEQ1_ASYNC, context, true);
    }

    private void createAndCallPostSeq() {
        //don't convert to lambda does not work in kit kat
        WeaveInterface postWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Boolean execute() {
                return executePostCreateSequence();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(postWeave, RemoteConfigKey.ENABLE_SEQ2_ASYNC, context, true);
    }

    private void createAndCallFontLoad() {
        //don't convert to lambda does not work in kit kat
        WeaveInterface fontWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Boolean execute() {
                return loadFontsInBg();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(fontWeave, RemoteConfigKey.ENABLE_SEQ5_ASYNC, context, true);
    }

    @NotNull
    private Boolean loadFontsInBg() {
        getTypeface(context, "NunitoSansExtraBold.ttf");
        getTypeface(context, "RobotoRegular.ttf");
        getTypeface(context, "RobotoBold.ttf");
        return true;
    }

    @NotNull
    private Boolean executePreCreateSequence() {
        Chucker.registerDefaultCrashHandler(new ChuckerCollector(ConsumerMainApplication.this, false));
        FpmLogger.init(ConsumerMainApplication.this);
        return true;
    }

    protected void setVersionName() {
        Pair<String, String> versions = AuthHelper.getVersionName(versionName());
        String version = versions.getFirst();
        String suffixVersion = versions.getSecond();

        if (TextUtils.isEmpty(suffixVersion)) {
            GlobalConfig.VERSION_NAME = versionName();
        } else {
            GlobalConfig.VERSION_NAME = version;
            GlobalConfig.VERSION_NAME_SUFFIX = suffixVersion;
        }
        GlobalConfig.RAW_VERSION_NAME = versionName();
    }

    /**
     * cannot reference BuildConfig of an app.
     *
     */
    @NonNull
    public abstract String versionName();

    public abstract int versionCode();

    public abstract void initConfigValues();

    @NotNull
    private Boolean executePostCreateSequence() {
        StethoUtil.initStetho(ConsumerMainApplication.this);
        IntentFilter intentFilter1 = new IntentFilter(Constants.ACTION_BC_RESET_APPLINK);
        LocalBroadcastManager.getInstance(ConsumerMainApplication.this).registerReceiver(new ApplinkResetReceiver(), intentFilter1);
        createCustomSoundNotificationChannel();

        initLogManager();
        initEmbraceConfig();
        DevMonitoring devMonitoring = new DevMonitoring(ConsumerMainApplication.this);
        devMonitoring.initCrashMonitoring();
        devMonitoring.initANRWatcher();
        devMonitoring.initTooLargeTool(ConsumerMainApplication.this);
        devMonitoring.initLeakCanary(getLeakCanaryToggleValue(), getStrictModeLeakPublisherToggleValue(), this);

        DeviceInfo.getAdsIdSuspend(ConsumerMainApplication.this, new Function1<String, Unit>() {
            @Override
            public Unit invoke(String s) {
                FingerprintModelGenerator.INSTANCE.expireFingerprint();
                return Unit.INSTANCE;
            }
        });

        gratificationSubscriber = new GratificationSubscriber(getApplicationContext());
        registerActivityLifecycleCallbacks(gratificationSubscriber);
        return true;
    }

    private boolean getLeakCanaryToggleValue() {
        return getSharedPreferences(LEAK_CANARY_TOGGLE_SP_NAME, MODE_PRIVATE).getBoolean(LEAK_CANARY_TOGGLE_KEY, LEAK_CANARY_DEFAULT_TOGGLE);
    }

    private boolean getStrictModeLeakPublisherToggleValue() {
        return getSharedPreferences(LEAK_CANARY_TOGGLE_SP_NAME, MODE_PRIVATE).getBoolean(STRICT_MODE_LEAK_PUBLISHER_TOGGLE_KEY, STRICT_MODE_LEAK_PUBLISHER_DEFAULT_TOGGLE);
    }

    private void initLogManager() {
        LogManager.init(ConsumerMainApplication.this, new LoggerProxy() {
            final AESEncryptorECB encryptor = new AESEncryptorECB();
            final SecretKey secretKey = encryptor.generateKey(NewRelicConstants.ENCRYPTION_KEY);

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
                return PARSER_SCALYR_MA;
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

    private void initEmbraceConfig() {
        String logEmbraceConfigString = remoteConfig.getString(RemoteConfigKey.ANDROID_EMBRACE_CONFIG);
        if (!TextUtils.isEmpty(logEmbraceConfigString)) {
            EmbraceConfig dataLogConfigEmbrace = new Gson().fromJson(logEmbraceConfigString, EmbraceConfig.class);

            EmbraceMonitoring.INSTANCE.getALLOW_EMBRACE_MOMENTS().clear();
            EmbraceMonitoring.INSTANCE.getALLOW_EMBRACE_MOMENTS().addAll(dataLogConfigEmbrace.getAllowedMoments());
        }
    }

    private void openShakeDetectCampaignPage(boolean isLongShake) {
        Intent intent = RouteManager.getIntent(getApplicationContext(), ApplinkConstInternalPromo.PROMO_CAMPAIGN_SHAKE_LANDING, Boolean.toString(isLongShake));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    @Override
    public void onTerminate() {
        // this function is not reliable and will never be called in production
        super.onTerminate();
        TrackApp.getInstance().delete();
        TrackApp.deleteInstance();
        TokopediaUrl.Companion.deleteInstance();
        CoreLegacyDbFlowDatabase.reset();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        CoreLegacyDbFlowDatabase.reset();
    }


    private void createCustomSoundNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setDescription(NOTIFICATION_CHANNEL_DESC);
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            mChannel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" +
                    R.raw.tokopedia_endtune), att);
            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);

            // Create the NotificationChannel
            mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID_BTS_ONE,
                    NOTIFICATION_CHANNEL_NAME_BTS_ONE, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setDescription(NOTIFICATION_CHANNEL_DESC_BTS_ONE);
            att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            mChannel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" +
                    R.raw.tokopedia_bts_one), att);
            notificationManager.createNotificationChannel(mChannel);

            // Create the NotificationChannel
            mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID_BTS_TWO,
                    NOTIFICATION_CHANNEL_NAME_BTS_TWO, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setDescription(NOTIFICATION_CHANNEL_DESC_BTS_TWO);
            att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            mChannel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" +
                    R.raw.tokopedia_bts_two), att);
            notificationManager.createNotificationChannel(mChannel);
        }
    }

    private void initializeSdk() {
        try {
            FirebaseApp.initializeApp(this);
        } catch (Exception e) {

        }
    }

    private void initializeAbTestVariant() {
        RemoteConfigInstance.initAbTestPlatform(this);
    }

    private void createAndCallFetchAbTest() {
        //don't convert to lambda does not work in kit kat
        WeaveInterface weave = new WeaveInterface() {
            @NotNull
            @Override
            public Boolean execute() {
                fetchAbTestVariant();
                return true;
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(weave, ENABLE_ASYNC_AB_TEST, context, false);
    }

    private void fetchAbTestVariant() {
        SharedPreferences sharedPreferences = getSharedPreferences(AbTestPlatform.Companion.getSHARED_PREFERENCE_AB_TEST_PLATFORM(), Context.MODE_PRIVATE);
        long timestampAbTest = sharedPreferences.getLong(AbTestPlatform.Companion.getKEY_SP_TIMESTAMP_AB_TEST(), 0);
        long current = new Date().getTime();
        if (current >= timestampAbTest + TimeUnit.HOURS.toMillis(1)) {
            RemoteConfigInstance.getInstance().getABTestPlatform().fetch(getRemoteConfigListener());
        }
    }

    protected AbTestPlatform.Listener getRemoteConfigListener() {
        return null;
    }

    protected void setVersionCode() {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            GlobalConfig.VERSION_CODE = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            GlobalConfig.VERSION_CODE = versionCode();
            com.tokopedia.config.GlobalConfig.VERSION_CODE = versionCode();
        }
    }

    private boolean handleClick(@Nullable String screenName, @Nullable Bundle extras, @Nullable Uri deepLinkUri) {
        if (deepLinkUri != null) {

            if (deepLinkUri.getScheme().equals(Constants.Schemes.HTTP) || deepLinkUri.getScheme().equals(Constants.Schemes.HTTPS)) {
                Intent intent = new Intent(this, DeepLinkActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse(deepLinkUri.toString()));
                if (extras != null) intent.putExtras(extras);

                startActivity(intent);

            } else if (deepLinkUri.getScheme().equals(Constants.Schemes.APPLINKS)
                    || deepLinkUri.getScheme().equals(Constants.Schemes.APPLINKS_SELLER)) {
                Intent intent = new Intent(this, DeeplinkHandlerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse(deepLinkUri.toString()));
                if (extras != null) intent.putExtras(extras);

                startActivity(intent);

            } else {
                Timber.d("FCM entered no one");
            }

            return true;
        } else {
            return false;
        }
    }

    private void showDevOptNotification() {
        new DevOptNotificationManager(this).start();
    }

    @Override
    public void onNewIntent(Context context, Intent intent) {
        super.onNewIntent(context, intent);
        if (gratificationSubscriber != null) {
            if (context instanceof Activity) {
                gratificationSubscriber.onNewIntent((Activity) context, intent);
                if (CMInAppManager.getInstance() != null) {
                    CMInAppManager.getInstance().activityLifecycleHandler.onNewIntent((Activity) context, intent);
                }
            }
        }
    }
}
