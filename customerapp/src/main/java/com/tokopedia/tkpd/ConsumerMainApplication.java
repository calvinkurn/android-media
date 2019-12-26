package com.tokopedia.tkpd;

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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatDelegate;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.soloader.SoLoader;
import com.github.anrwatchdog.ANRError;
import com.github.anrwatchdog.ANRWatchDog;
import com.google.firebase.FirebaseApp;
import com.moengage.inapp.InAppManager;
import com.moengage.inapp.InAppMessage;
import com.moengage.inapp.InAppTracker;
import com.moengage.push.PushManager;
import com.moengage.pushbase.push.MoEPushCallBacks;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.cacheapi.domain.interactor.CacheApiWhiteListUseCase;
import com.tokopedia.cacheapi.util.CacheApiLoggingUtils;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.core.analytics.container.AppsflyerAnalytics;
import com.tokopedia.core.analytics.container.GTMAnalytics;
import com.tokopedia.core.analytics.container.MoengageAnalytics;
import com.tokopedia.core.database.CoreLegacyDbFlowDatabase;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.cpm.CharacterPerMinuteActivityLifecycleCallbacks;
import com.tokopedia.cpm.CharacterPerMinuteInterface;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.logger.LogWrapper;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.navigation_common.category.CategoryNavigationConfig;
import com.tokopedia.promotionstarget.presentation.subscriber.GratificationSubscriber;
import com.tokopedia.remoteconfig.RemoteConfigInstance;
import com.tokopedia.remoteconfig.abtest.AbTestPlatform;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;
import com.tokopedia.tkpd.fcm.ApplinkResetReceiver;
import com.tokopedia.tkpd.timber.TimberWrapper;
import com.tokopedia.tkpd.utils.CacheApiWhiteList;
import com.tokopedia.tkpd.utils.CustomPushListener;
import com.tokopedia.tkpd.utils.DeviceUtil;
import com.tokopedia.tkpd.utils.StethoUtil;
import com.tokopedia.tkpd.utils.UIBlockDebugger;
import com.tokopedia.track.TrackApp;
import com.tokopedia.url.TokopediaUrl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import kotlin.jvm.functions.Function1;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class ConsumerMainApplication extends ConsumerRouterApplication implements
        MoEPushCallBacks.OnMoEPushNavigationAction,
        InAppManager.InAppMessageListener,
        CharacterPerMinuteInterface {

    private final String NOTIFICATION_CHANNEL_NAME = "Promo";
    private final String NOTIFICATION_CHANNEL_NAME_BTS_ONE = "Promo BTS 1";
    private final String NOTIFICATION_CHANNEL_NAME_BTS_TWO = "Promo BTS 2";
    private final String NOTIFICATION_CHANNEL_ID = "custom_sound";
    private final String NOTIFICATION_CHANNEL_ID_BTS_ONE = "custom_sound_bts_one";
    private final String NOTIFICATION_CHANNEL_ID_BTS_TWO = "custom_sound_bts_two";
    private final String NOTIFICATION_CHANNEL_DESC = "notification channel for custom sound.";
    private final String NOTIFICATION_CHANNEL_DESC_BTS_ONE = "notification channel for custom sound with BTS tone";
    private final String NOTIFICATION_CHANNEL_DESC_BTS_TWO = "notification channel for custom sound with different BTS tone";

    CharacterPerMinuteActivityLifecycleCallbacks callback;

    // Used to load the 'native-lib' library on application startup.
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        System.loadLibrary("native-lib");
    }

    @Override
    public void onCreate() {
        UIBlockDebugger.init(this);
        com.tokopedia.akamai_bot_lib.UtilsKt.initAkamaiBotManager(this);
        setVersionCode();

        initializeSdk();

        GlobalConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
        GlobalConfig.DEBUG = BuildConfig.DEBUG;
        GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        com.tokopedia.config.GlobalConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
        com.tokopedia.config.GlobalConfig.DEBUG = BuildConfig.DEBUG;
        com.tokopedia.config.GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        com.tokopedia.config.GlobalConfig.IS_PREINSTALL = BuildConfig.IS_PREINSTALL;
        com.tokopedia.config.GlobalConfig.PREINSTALL_NAME = BuildConfig.PREINSTALL_NAME;
        com.tokopedia.config.GlobalConfig.PREINSTALL_DESC = BuildConfig.PREINSTALL_DESC;
        com.tokopedia.config.GlobalConfig.PREINSTALL_SITE = BuildConfig.PREINSTALL_SITE;
        com.tokopedia.config.GlobalConfig.APPLICATION_ID = BuildConfig.APPLICATION_ID;
        com.tokopedia.config.GlobalConfig.HOME_ACTIVITY_CLASS_NAME = MainParentActivity.class.getName();
        com.tokopedia.config.GlobalConfig.DEEPLINK_HANDLER_ACTIVITY_CLASS_NAME = DeeplinkHandlerActivity.class.getName();
        com.tokopedia.config.GlobalConfig.DEEPLINK_ACTIVITY_CLASS_NAME = DeepLinkActivity.class.getName();
        com.tokopedia.config.GlobalConfig.DEVICE_ID = DeviceUtil.getDeviceId(this);

        TokopediaUrl.Companion.init(this); // generate base url

        generateConsumerAppNetworkKeys();

        TrackApp.initTrackApp(this);

        TrackApp.getInstance().registerImplementation(TrackApp.GTM, GTMAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.APPSFLYER, AppsflyerAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.MOENGAGE, MoengageAnalytics.class);
        TrackApp.getInstance().initializeAllApis();

        PersistentCacheManager.init(this);
        initReact();

        super.onCreate();

        StethoUtil.initStetho(this);

        MoEPushCallBacks.getInstance().setOnMoEPushNavigationAction(this);
        InAppManager.getInstance().setInAppListener(this);

        IntentFilter intentFilter1 = new IntentFilter(Constants.ACTION_BC_RESET_APPLINK);
        LocalBroadcastManager.getInstance(this).registerReceiver(new ApplinkResetReceiver(), intentFilter1);
        initCacheApi();
        createCustomSoundNotificationChannel();
        PushManager.getInstance().setMessageListener(new CustomPushListener());
        GraphqlClient.init(getApplicationContext());
        NetworkClient.init(getApplicationContext());

        if (!com.tokopedia.config.GlobalConfig.DEBUG) {
            // do not replace with method reference "Crashlytics::logException", will not work in dynamic feature
            new ANRWatchDog().setANRListener(new ANRWatchDog.ANRListener() {
                @Override
                public void onAppNotResponding(ANRError anrError) {
                    Crashlytics.logException(anrError);
                }
            }).start();
        }

        if (callback == null) {
            callback = new CharacterPerMinuteActivityLifecycleCallbacks(this);
        }
        registerActivityLifecycleCallbacks(callback);

        LogWrapper.init(this);
        if (LogWrapper.instance != null) {
            LogWrapper.instance.setLogentriesToken(TimberWrapper.LOGENTRIES_TOKEN);
        }
        TimberWrapper.init(this);

        initializeAbTestVariant();

        GratificationSubscriber subscriber = new GratificationSubscriber(getApplicationContext());
        registerActivityLifecycleCallbacks(subscriber);
    }

    @Override
    public void onTerminate() {
        // this function is not reliable and will never be called in production
        super.onTerminate();
        TrackApp.getInstance().delete();
        TrackApp.deleteInstance();
        TokopediaUrl.Companion.deleteInstance();
        unregisterActivityLifecycleCallbacks(callback);
        CoreLegacyDbFlowDatabase.reset();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        unregisterActivityLifecycleCallbacks(callback);
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
            e.printStackTrace();
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

    private void generateConsumerAppNetworkKeys() {
        AuthUtil.KEY.KEY_CREDIT_CARD_VAULT = ConsumerAppNetworkKeys.CREDIT_CARD_VAULT_AUTH_KEY;
        AuthUtil.KEY.ZEUS_WHITELIST = ConsumerAppNetworkKeys.ZEUS_WHITELIST;
    }

    @Override
    public boolean onClick(@Nullable String screenName, @Nullable Bundle extras, @Nullable Uri deepLinkUri) {
        CommonUtils.dumper("GAv4 MOE NGGAGE on notif click " + deepLinkUri + " bundle " + extras);
        return handleClick(screenName, extras, deepLinkUri);
    }

    @Override
    public void onInAppShown(InAppMessage message) {
    }

    @Override
    public boolean showInAppMessage(InAppMessage message) {
        InAppTracker.getInstance(this).trackInAppClicked(message);
        return true;
    }

    @Override
    public void onInAppClosed(InAppMessage message) {

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
                CommonUtils.dumper("FCM entered no one");
            }

            return true;
        } else {
            return false;
        }
    }

    private void initReact() {
        SoLoader.init(this, false);
    }

    private void initCacheApi() {
        CacheApiLoggingUtils.setLogEnabled(GlobalConfig.isAllowDebuggingTools());
        new CacheApiWhiteListUseCase(this).executeSync(CacheApiWhiteListUseCase.createParams(
                CacheApiWhiteList.getWhiteList(),
                String.valueOf(getCurrentVersion(getApplicationContext()))));
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    private native byte[] bytesFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public boolean checkAppSignature() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (null != info && info.signatures.length > 0) {
            byte[] rawCertJava = info.signatures[0].toByteArray();
            byte[] rawCertNative = bytesFromJNI();

            return getInfoFromBytes(rawCertJava).equals(getInfoFromBytes(rawCertNative));
        } else {
            return false;
        }
    }

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
            // e.printStackTrace();
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

    public Class<?> getDeeplinkClass() {
        return DeepLinkActivity.class;
    }

    @Override
    public void saveCPM(@NonNull String cpm) {
        PersistentCacheManager.instance.put(CharacterPerMinuteInterface.KEY, cpm, TimeUnit.MINUTES.toMillis(1));
    }

    @Override
    public String getCPM() {
        return PersistentCacheManager.instance.getString(CharacterPerMinuteInterface.KEY);
    }

    @Override
    public boolean isEnable() {
        return getBooleanRemoteConfig("android_customer_typing_tracker_enabled", false);
    }


    @Override
    public void setCategoryAbTestingConfig() {
        CategoryNavigationConfig.INSTANCE.updateCategoryConfig(getApplicationContext(),
                // do not replace with method reference "this::openNewBelanja", will not work in dynamic feature
                new Function1<Context, Intent>() {
                    @Override
                    public Intent invoke(Context context) {
                        return ConsumerMainApplication.this.openNewBelanja(context);
                    }
                },
                // do not replace with method reference "this::openOldBelanja", will not work in dynamic feature
                new Function1<Context, Intent>() {
                    @Override
                    public Intent invoke(Context context) {
                        return ConsumerMainApplication.this.openOldBelanja(context);
                    }
                });
    }

    Intent openNewBelanja(Context context) {
        return null;
    }

    Intent openOldBelanja(Context context) {
        return null;
    }
}
