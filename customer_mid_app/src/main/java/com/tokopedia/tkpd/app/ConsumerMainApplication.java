package com.tokopedia.tkpd.app;

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
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.chuckerteam.chucker.api.Chucker;
import com.chuckerteam.chucker.api.ChuckerCollector;
import com.facebook.FacebookSdk;
import com.google.firebase.FirebaseApp;
import com.tokopedia.additional_check.subscriber.TwoFactorCheckerSubscriber;
import com.tokopedia.analytics.performance.util.SplashScreenPerformanceTracker;
import com.tokopedia.analyticsdebugger.debugger.FpmLogger;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalPromo;
import com.tokopedia.authentication.AuthHelper;
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
import com.tokopedia.encryption.security.AESEncryptorECB;
import com.tokopedia.keys.Keys;
import com.tokopedia.logger.LogManager;
import com.tokopedia.logger.LoggerProxy;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.moengage_wrapper.MoengageInteractor;
import com.tokopedia.moengage_wrapper.interfaces.CustomPushDataListener;
import com.tokopedia.moengage_wrapper.interfaces.MoengageInAppListener;
import com.tokopedia.moengage_wrapper.interfaces.MoengagePushListener;
import com.tokopedia.moengage_wrapper.util.NotificationBroadcast;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.media.common.Loader;
import com.tokopedia.media.common.common.ToasterActivityLifecycle;
import com.tokopedia.notifications.data.AmplificationDataSource;
import com.tokopedia.notifications.inApp.CMInAppManager;
import com.tokopedia.prereleaseinspector.ViewInspectorSubscriber;
import com.tokopedia.promotionstarget.presentation.subscriber.GratificationSubscriber;
import com.tokopedia.remoteconfig.RemoteConfigInstance;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.remoteconfig.abtest.AbTestPlatform;
import com.tokopedia.shakedetect.ShakeDetectManager;
import com.tokopedia.shakedetect.ShakeSubscriber;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;
import com.tokopedia.tkpd.fcm.ApplinkResetReceiver;
import com.tokopedia.tkpd.nfc.NFCSubscriber;
import com.tokopedia.tkpd.timber.LoggerActivityLifecycleCallbacks;
import com.tokopedia.tkpd.utils.NewRelicConstants;
import com.tokopedia.track.TrackApp;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import kotlin.Pair;
import kotlin.jvm.functions.Function1;
import timber.log.Timber;

import static android.os.Process.killProcess;
import static com.tokopedia.unifyprinciples.GetTypefaceKt.getTypeface;

/**
 * Created by ricoharisin on 11/11/16.
 */

public abstract class ConsumerMainApplication extends ConsumerRouterApplication implements
        MoengageInAppListener, MoengagePushListener, CustomPushDataListener {

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
    private static final String REMOTE_CONFIG_NEW_RELIC_KEY_LOG = "android_customerapp_log_config_new_relic";

    GratificationSubscriber gratificationSubscriber;

    @Override
    public void onCreate() {
        SplashScreenPerformanceTracker.isColdStart = true;
        initConfigValues();
        initializeSdk();
        initRemoteConfig();
        TokopediaUrl.Companion.init(this); // generate base url
        initCacheManager();

        TrackApp.initTrackApp(this);
        TrackApp.getInstance().registerImplementation(TrackApp.GTM, GTMAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.APPSFLYER, AppsflyerAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.MOENGAGE, MoengageAnalytics.class);
        TrackApp.getInstance().initializeAllApis();
        com.tokopedia.akamai_bot_lib.UtilsKt.initAkamaiBotManager(ConsumerMainApplication.this);
        createAndCallPreSeq();
        super.onCreate();
        createAndCallPostSeq();
        initializeAbTestVariant();
        createAndCallFontLoad();

        registerActivityLifecycleCallbacks();
        checkAppSignatureAsync();

        Loader.init(this);
    }

    private void checkAppSignatureAsync() {
        WeaveInterface checkAppSignatureWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                if (!checkAppSignature()) {
                    killProcess(android.os.Process.myPid());
                }
                if (!checkPackageName()) {
                    killProcess(android.os.Process.myPid());
                }
                return true;
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(checkAppSignatureWeave, RemoteConfigKey.ENABLE_ASYNC_CHECKAPPSIGNATURE, this);
    }

    private boolean checkPackageName() {
        boolean packageNameValid = this.getPackageName().equals(getOriginalPackageApp());
        if (!packageNameValid) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("packageName", this.getPackageName());
            ServerLogger.log(Priority.P1, "APP_SIGNATURE_FAILED", messageMap);
        }
        return packageNameValid;
    }

    protected abstract String getOriginalPackageApp();

    private boolean checkAppSignature() {
        try {
            PackageInfo info;
            boolean signatureValid;
            byte[] rawCertJava = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);
                if (null != info && info.signingInfo.getApkContentsSigners().length > 0) {
                    rawCertJava = info.signingInfo.getApkContentsSigners()[0].toByteArray();
                }
            } else {
                info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
                if (null != info && info.signatures.length > 0) {
                    rawCertJava = info.signatures[0].toByteArray();
                }
            }
            byte[] rawCertNative = getJniBytes();
            // handle if the library is failing
            if (rawCertNative == null) {
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put("rawCertNative", "null");
                ServerLogger.log(Priority.P1, "APP_SIGNATURE_FAILED", messageMap);
                return true;
            } else if (rawCertJava == null) {
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put("rawCertJava", "null");
                ServerLogger.log(Priority.P1, "APP_SIGNATURE_FAILED", messageMap);
                return true;
            } else {
                signatureValid = getInfoFromBytes(rawCertJava).equals(getInfoFromBytes(rawCertNative));
            }
            if (!signatureValid) {
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put("certJava", "!=certNative");
                ServerLogger.log(Priority.P1, "APP_SIGNATURE_FAILED", messageMap);
            }
            return signatureValid;
        } catch (PackageManager.NameNotFoundException e) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "PackageManager.NameNotFoundException");
            ServerLogger.log(Priority.P1, "APP_SIGNATURE_FAILED", messageMap);
            return false;
        }
    }

    protected abstract byte[] getJniBytes();

    private String getInfoFromBytes(byte[] bytes) {
        if (null == bytes) {
            return "null";
        }

        /*
         * Get the X.509 certificate.
         */
        InputStream certStream = new ByteArrayInputStream(bytes);
        StringBuilder sb = new StringBuilder();
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X509");
            X509Certificate x509Cert = (X509Certificate) certFactory.generateCertificate(certStream);

            sb.append("Certificate subject: ").append(x509Cert.getSubjectDN()).append("\n");
            sb.append("Certificate issuer: ").append(x509Cert.getIssuerDN()).append("\n");
            sb.append("Certificate serial number: ").append(x509Cert.getSerialNumber()).append("\n");
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
                md.update(bytes);
                byte[] byteArray = md.digest();
                sb.append("MD5: ").append(bytesToString(byteArray)).append("\n");
                md.reset();
                md = MessageDigest.getInstance("SHA");
                md.update(bytes);
                byteArray = md.digest();
                sb.append("SHA1: ").append(bytesToString(byteArray)).append("\n");
                md.reset();
                md = MessageDigest.getInstance("SHA256");
                md.update(bytes);
                byteArray = md.digest();
                sb.append("SHA256: ").append(bytesToString(byteArray)).append("\n");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }


            sb.append("\n");
        } catch (CertificateException e) {

        }
        return sb.toString();
    }


    private String bytesToString(byte[] bytes) {
        StringBuilder md5StrBuff = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & bytes[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
            }
            if (bytes.length - 1 != i) {
                md5StrBuff.append(":");
            }
        }
        return md5StrBuff.toString();
    }

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
        registerActivityLifecycleCallbacks(new LoggerActivityLifecycleCallbacks());
        registerActivityLifecycleCallbacks(new NFCSubscriber());
        registerActivityLifecycleCallbacks(new SessionActivityLifecycleCallbacks());
        if (GlobalConfig.isAllowDebuggingTools()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                registerActivityLifecycleCallbacks(new JankyFrameActivityLifecycleCallbacks.Builder().build());
            }
            registerActivityLifecycleCallbacks(new ViewInspectorSubscriber());
            registerActivityLifecycleCallbacks(new DevOptsSubscriber());
        }
        registerActivityLifecycleCallbacks(new TwoFactorCheckerSubscriber());
        registerActivityLifecycleCallbacks(new ToasterActivityLifecycle(this));
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
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(preWeave, RemoteConfigKey.ENABLE_SEQ1_ASYNC, context);
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
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(postWeave, RemoteConfigKey.ENABLE_SEQ2_ASYNC, context);
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
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(fontWeave, RemoteConfigKey.ENABLE_SEQ5_ASYNC, context);
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

        if (!version.equalsIgnoreCase(AuthHelper.ERROR)) {
            GlobalConfig.VERSION_NAME = version;
            com.tokopedia.config.GlobalConfig.VERSION_NAME = version;
            com.tokopedia.config.GlobalConfig.VERSION_NAME_SUFFIX = suffixVersion;
        } else {
            GlobalConfig.VERSION_NAME = versionName();
            com.tokopedia.config.GlobalConfig.VERSION_NAME = versionName();
        }
        com.tokopedia.config.GlobalConfig.RAW_VERSION_NAME = versionName();// save raw version name
    }

    /**
     * cannot reference BuildConfig of an app.
     *
     * @return
     */
    @NonNull
    public abstract String versionName();

    public abstract int versionCode();

    public abstract void initConfigValues();

    @NotNull
    private Boolean executePostCreateSequence() {
        StethoUtil.initStetho(ConsumerMainApplication.this);
        MoengageInteractor.INSTANCE.setPushListener(ConsumerMainApplication.this);
        MoengageInteractor.INSTANCE.setInAppListener(ConsumerMainApplication.this);
        IntentFilter intentFilter1 = new IntentFilter(Constants.ACTION_BC_RESET_APPLINK);
        LocalBroadcastManager.getInstance(ConsumerMainApplication.this).registerReceiver(new ApplinkResetReceiver(), intentFilter1);
        createCustomSoundNotificationChannel();
        MoengageInteractor.INSTANCE.setMessageListener(this);

        initLogManager();
        DevMonitoring devMonitoring = new DevMonitoring(ConsumerMainApplication.this);
        devMonitoring.initCrashMonitoring();
        devMonitoring.initANRWatcher();
        devMonitoring.initTooLargeTool(ConsumerMainApplication.this);
        devMonitoring.initBlockCanary();

        gratificationSubscriber = new GratificationSubscriber(getApplicationContext());
        registerActivityLifecycleCallbacks(gratificationSubscriber);
        getAmplificationPushData();
        return true;
    }

    private void initLogManager() {
        LogManager.init(ConsumerMainApplication.this, new LoggerProxy() {
            final AESEncryptorECB encryptor = new AESEncryptorECB();
            final SecretKey secretKey = encryptor.generateKey(NewRelicConstants.ENCRYPTION_KEY);

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

    private void getAmplificationPushData() {
        /*
         * Amplification of push notification.
         * fetch all of cm_push_notification's
         * push notification data that aren't rendered yet.
         * then, put all of push_data into local storage.
         * */
        if (getAmplificationRemoteConfig()) {
            try {
                AmplificationDataSource.invoke(ConsumerMainApplication.this);
            } catch (Exception e) {
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put("type", "exception");
                messageMap.put("err", Log.getStackTraceString
                        (e).substring(0, (Math.min(Log.getStackTraceString(e).length(), CMConstant.TimberTags.MAX_LIMIT))));
                messageMap.put("data", "");
                ServerLogger.log(Priority.P2, "CM_VALIDATION", messageMap);
            }
        }
    }

    private Boolean getAmplificationRemoteConfig() {
        return remoteConfig.getBoolean(RemoteConfigKey.ENABLE_AMPLIFICATION, true);
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
            FacebookSdk.sdkInitialize(this);
        } catch (Exception e) {

        }
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

    protected void setVersionCode() {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            GlobalConfig.VERSION_CODE = pInfo.versionCode;
            com.tokopedia.config.GlobalConfig.VERSION_CODE = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            GlobalConfig.VERSION_CODE = versionCode();
            com.tokopedia.config.GlobalConfig.VERSION_CODE = versionCode();
        }
    }

    public abstract void generateConsumerAppNetworkKeys();

    @Override
    public boolean onClick(@Nullable String screenName, @Nullable Bundle extras, @Nullable Uri deepLinkUri) {
        Timber.d("GAv4 MOE NGGAGE on notif click " + deepLinkUri + " bundle " + extras);
        return handleClick(screenName, extras, deepLinkUri);
    }

    @Override
    public boolean onInAppClick(@Nullable String screenName, @Nullable Bundle extras, @Nullable Uri deepLinkUri) {
        return handleClick(screenName, extras, deepLinkUri);
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

    public Class<?> getDeeplinkClass() {
        return DeepLinkActivity.class;
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

    @NotNull
    @Override
    public Intent getPersistentNotificationIntent() {
        return new Intent(ConsumerMainApplication.this, MainParentActivity.class);
    }

    @NotNull
    @Override
    public Intent getNotificationBroadcastIntent() {
        return new Intent(ConsumerMainApplication.this, NotificationBroadcast.class);
    }

    @Override
    public int getIcStatNotifyWhiteDrawable() {
        return R.drawable.ic_stat_notify_white;
    }
}
