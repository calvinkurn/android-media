package com.tokopedia.tkpd;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.crashlytics.android.Crashlytics;
import com.facebook.soloader.SoLoader;
import com.github.anrwatchdog.ANRWatchDog;
import com.moengage.inapp.InAppManager;
import com.moengage.inapp.InAppMessage;
import com.moengage.inapp.InAppTracker;
import com.moengage.push.PushManager;
import com.moengage.pushbase.push.MoEPushCallBacks;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.ProductDraftGeneratedDatabaseHolder;
import com.raizlabs.android.dbflow.config.TkpdCacheApiGeneratedDatabaseHolder;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.constant.AbstractionBaseURL;
import com.tokopedia.affiliatecommon.data.network.TopAdsConstantKt;
import com.tokopedia.analytics.Analytics;
import com.tokopedia.attachproduct.data.source.url.AttachProductUrl;
import com.tokopedia.cacheapi.domain.interactor.CacheApiWhiteListUseCase;
import com.tokopedia.cacheapi.util.CacheApiLoggingUtils;
import com.tokopedia.changepassword.data.ChangePasswordUrl;
import com.tokopedia.changephonenumber.ChangePhoneNumberUrl;
import com.tokopedia.chat_common.network.ChatUrl;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.config.url.Env;
import com.tokopedia.config.url.TokopediaUrl;
import com.tokopedia.config.url.Url;
import com.tokopedia.core.analytics.container.AppsflyerAnalytics;
import com.tokopedia.core.analytics.container.GTMAnalytics;
import com.tokopedia.core.analytics.container.MoengageAnalytics;
import com.tokopedia.core.common.category.CategoryDbFlow;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.cpm.CharacterPerMinuteActivityLifecycleCallbacks;
import com.tokopedia.cpm.CharacterPerMinuteInterface;
import com.tokopedia.digital.common.constant.DigitalUrl;
import com.tokopedia.digital.newcart.data.DigitalDealsUrl;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.discovery.newdiscovery.constant.DiscoveryBaseURL;
import com.tokopedia.events.data.source.EventsUrl;
import com.tokopedia.feedplus.data.api.FeedUrl;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.gamification.GamificationUrl;
import com.tokopedia.gm.common.constant.GMCommonUrl;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.source.cloud.api.GraphqlUrl;
import com.tokopedia.groupchat.chatroom.data.ChatroomUrl;
import com.tokopedia.groupchat.common.data.GroupChatUrl;
import com.tokopedia.home.account.AccountHomeUrl;
import com.tokopedia.home.constant.BerandaUrl;
import com.tokopedia.imageuploader.data.ImageUploaderUrl;
import com.tokopedia.inbox.rescenter.network.ResolutionUrl;
import com.tokopedia.instantloan.network.InstantLoanUrl;
import com.tokopedia.kol.common.network.KolUrl;
import com.tokopedia.loginphone.checkregisterphone.data.CheckMsisdnUrl;
import com.tokopedia.loginphone.common.data.LoginRegisterPhoneUrl;
import com.tokopedia.loginregister.common.data.LoginRegisterUrl;
import com.tokopedia.logisticdata.data.constant.LogisticDataConstantUrl;
import com.tokopedia.logout.data.LogoutUrl;
import com.tokopedia.network.SessionUrl;
import com.tokopedia.notifications.data.source.CMNotificationUrls;
import com.tokopedia.oms.data.source.OmsUrl;
import com.tokopedia.otp.cotp.data.CotpUrl;
import com.tokopedia.otp.cotp.data.SQLoginUrl;
import com.tokopedia.payment.fingerprint.util.PaymentFingerprintConstant;
import com.tokopedia.payment.setting.util.PaymentSettingUrlKt;
import com.tokopedia.phoneverification.PhoneVerificationConst;
import com.tokopedia.product.detail.data.util.ProductDetailConstant;
import com.tokopedia.product.manage.item.imagepicker.util.CatalogConstant;
import com.tokopedia.pushnotif.PushNotification;
import com.tokopedia.recentview.data.api.RecentViewUrl;
import com.tokopedia.reputation.common.constant.ReputationCommonUrl;
import com.tokopedia.sessioncommon.data.SessionCommonUrl;
import com.tokopedia.settingbank.banklist.data.SettingBankUrl;
import com.tokopedia.settingbank.choosebank.data.BankListUrl;
import com.tokopedia.shop.common.constant.ShopCommonUrl;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.talk.common.data.TalkUrl;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;
import com.tokopedia.tkpd.fcm.ApplinkResetReceiver;
import com.tokopedia.tkpd.utils.CacheApiWhiteList;
import com.tokopedia.tkpd.utils.CustomPushListener;
import com.tokopedia.tkpdpdp.ProductDetailUrl;
import com.tokopedia.tkpdreactnative.react.fingerprint.utils.FingerprintConstantRegister;
import com.tokopedia.tokocash.network.api.WalletUrl;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.track.TrackApp;
import com.tokopedia.train.common.constant.TrainUrl;
import com.tokopedia.transaction.network.TransactionUrl;
import com.tokopedia.transactiondata.constant.TransactionDataApiUrl;
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneURL;
import com.tokopedia.user_identification_common.KycCommonUrl;
import com.tokopedia.vote.data.VoteUrl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class ConsumerMainApplication extends ConsumerRouterApplication implements
        MoEPushCallBacks.OnMoEPushNavigationAction,
        InAppManager.InAppMessageListener,
        CharacterPerMinuteInterface
{

    private final String NOTIFICATION_CHANNEL_NAME = "Promo";
    private final String NOTIFICATION_CHANNEL_ID = "custom_sound";
    private final String NOTIFICATION_CHANNEL_DESC = "notification channel for custom sound.";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    public void onCreate() {
        com.example.akamai_bot_lib.UtilsKt.initAkamaiBotManager(this);
        setVersionCode();
        GlobalConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
        GlobalConfig.DEBUG = BuildConfig.DEBUG;
        GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        com.tokopedia.config.GlobalConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
        com.tokopedia.config.GlobalConfig.DEBUG = BuildConfig.DEBUG;
        com.tokopedia.config.GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.DEBUG;
        com.tokopedia.config.GlobalConfig.IS_PREINSTALL = BuildConfig.IS_PREINSTALL;
        com.tokopedia.config.GlobalConfig.PREINSTALL_NAME = BuildConfig.PREINSTALL_NAME;
        com.tokopedia.config.GlobalConfig.PREINSTALL_DESC = BuildConfig.PREINSTALL_DESC;
        com.tokopedia.config.GlobalConfig.PREINSTALL_SITE = BuildConfig.PREINSTALL_SITE;
        com.tokopedia.config.GlobalConfig.APPLICATION_ID = BuildConfig.APPLICATION_ID;
        generateConsumerAppBaseUrl();
        generateConsumerAppNetworkKeys();

        initializeDatabase();
        TrackApp.initTrackApp(this);

        TrackApp.getInstance().registerImplementation(TrackApp.GTM, GTMAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.APPSFLYER, AppsflyerAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.MOENGAGE, MoengageAnalytics.class);
        TrackApp.getInstance().initializeAllApis();

        super.onCreate();
        initReact();

        MoEPushCallBacks.getInstance().setOnMoEPushNavigationAction(this);
        InAppManager.getInstance().setInAppListener(this);

        IntentFilter intentFilter1 = new IntentFilter(Constants.ACTION_BC_RESET_APPLINK);
        LocalBroadcastManager.getInstance(this).registerReceiver(new ApplinkResetReceiver(), intentFilter1);
        initCacheApi();
        createCustomSoundNotificationChannel();
        PushManager.getInstance().setMessageListener(new CustomPushListener());
        GraphqlClient.init(getApplicationContext());
        NetworkClient.init(getApplicationContext());
        InstabugInitalize.init(this);

        if (!GlobalConfig.DEBUG) {
            new ANRWatchDog().setANRListener(Crashlytics::logException).start();
        }

        cacheManager = new GlobalCacheManager();
        cacheManager.setCacheDuration(600);
        if(callback == null) {
            callback = new CharacterPerMinuteActivityLifecycleCallbacks(this);
        }
        registerActivityLifecycleCallbacks(callback);
    }

    CharacterPerMinuteActivityLifecycleCallbacks callback;

    @Override
    public void onTerminate() {
        super.onTerminate();
        TrackApp.getInstance().delete();
        TrackApp.deleteInstance();
        unregisterActivityLifecycleCallbacks(callback);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        unregisterActivityLifecycleCallbacks(callback);
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
        }
    }

    private void setVersionCode() {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            GlobalConfig.VERSION_CODE = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            GlobalConfig.VERSION_CODE = BuildConfig.VERSION_CODE;
        }
    }

    private void generateConsumerAppBaseUrl() {
        TokopediaUrl.Companion.init(context);
//        TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL = baseUrl.getWEB();
//        InstantLoanUrl.BaseUrl.WEB_DOMAIN = baseUrl.getWEB();
//        TkpdBaseURL.BASE_DOMAIN = baseUrl.getWS();
//        TkpdBaseURL.BASE_API_DOMAIN = baseUrl.getAPI();
//        TkpdBaseURL.ACE_DOMAIN = baseUrl.getACE();
//        TkpdBaseURL.TOME_DOMAIN = baseUrl.getTOME();
//        TkpdBaseURL.TOPADS_DOMAIN = baseUrl.getTA();
//        TkpdBaseURL.MOJITO_DOMAIN = baseUrl.getMOJITO();
//        TkpdBaseURL.HADES_DOMAIN = baseUrl.getHADES();
//        TkpdBaseURL.ACCOUNTS_DOMAIN = baseUrl.getACCOUNTS();
//        com.tokopedia.network.constant.TkpdBaseURL.ACCOUNTS_DOMAIN = baseUrl.getACCOUNTS();
//        TkpdBaseURL.INBOX_DOMAIN = baseUrl.getINBOX();
//        TkpdBaseURL.JS_DOMAIN = baseUrl.getJS();
//        TkpdBaseURL.KERO_DOMAIN = baseUrl.getKERO();
//        TkpdBaseURL.KERO_RATES_DOMAIN = baseUrl.getGW();
//        TkpdBaseURL.JAHE_DOMAIN = baseUrl.getJAHE();
//        TkpdBaseURL.PULSA_WEB_DOMAIN = baseUrl.getPULSA();
//        TkpdBaseURL.GOLD_MERCHANT_DOMAIN = baseUrl.getGOLDMERCHANT();
//        TkpdBaseURL.WEB_DOMAIN = baseUrl.getWEB();
//        TkpdBaseURL.MOBILE_DOMAIN = baseUrl.getMOBILEWEB();
//        TkpdBaseURL.BASE_CONTACT_US = baseUrl.getWEB() + "contact-us";
//        TkpdBaseURL.TOKO_CASH_DOMAIN = baseUrl.getTOKOCASH();
//        TkpdBaseURL.BASE_ACTION = baseUrl.getWS() + "v4/action/";
//        TkpdBaseURL.DIGITAL_API_DOMAIN = ConsumerAppBaseUrl.BASE_DIGITAL_API_DOMAIN;
//        TkpdBaseURL.DIGITAL_WEBSITE_DOMAIN = baseUrl.getPULSA();
//        TkpdBaseURL.GRAPHQL_DOMAIN = baseUrl.getGQL();
//        TkpdBaseURL.HOME_DATA_BASE_URL = baseUrl.getGQL();
//        TkpdBaseURL.SCROOGE_DOMAIN = baseUrl.getPAY();
//        TkpdBaseURL.SCROOGE_CREDIT_CARD_DOMAIN = baseUrl.getPAY_ID();
//        TkpdBaseURL.PAYMENT_DOMAIN = baseUrl.getPAYMENT();
//        TkpdBaseURL.GALADRIEL = baseUrl.getGALADRIEL();
//        TkpdBaseURL.CHAT_DOMAIN = baseUrl.getCHAT();
//        TkpdBaseURL.CHAT_WEBSOCKET_DOMAIN = baseUrl.getWS_CHAT();
//        com.tokopedia.network.constant.TkpdBaseURL.CHAT_WEBSOCKET_DOMAIN = baseUrl.getWS_CHAT();
//        com.tokopedia.network.constant.TkpdBaseURL.GROUP_CHAT_WEBSOCKET_DOMAIN = baseUrl.getWS_GROUPCHAT();
//        TkpdBaseURL.MAPS_DOMAIN = baseUrl.getGW();
//        TkpdBaseURL.WALLET_DOMAIN = baseUrl.getTOKOCASH();
//        TkpdBaseURL.EVENTS_DOMAIN = baseUrl.getBOOKING();
//        TkpdBaseURL.TOKOPOINT_API_DOMAIN = baseUrl.getGW() + "tokopoints/api/";
//        FlightUrl.BASE_URL = baseUrl.getAPI();
//        FlightUrl.WEB_DOMAIN = baseUrl.getWEB();
//        AbstractionBaseURL.JS_DOMAIN = baseUrl.getJS();
//        FlightUrl.ALL_PROMO_LINK = baseUrl.getWEB() + FlightUrl.PROMO_PATH;
//        FlightUrl.CONTACT_US = baseUrl.getWEB() + FlightUrl.CONTACT_US_PATH;
//        FlightUrl.CONTACT_US_FLIGHT = FlightUrl.CONTACT_US + FlightUrl.CONTACT_US_FLIGHT_HOME_PREFIX;
//        FlightUrl.CONTACT_US_FLIGHT_PREFIX_GLOBAL = FlightUrl.CONTACT_US + FlightUrl.CONTACT_US_FLIGHT_PREFIX;
//        TransactionUrl.BASE_URL = baseUrl.getAPI();
//        WalletUrl.BaseUrl.ACCOUNTS_DOMAIN = baseUrl.getACCOUNTS();
//        WalletUrl.BaseUrl.WALLET_DOMAIN = baseUrl.getTOKOCASH();
//        WalletUrl.BaseUrl.WEB_DOMAIN = baseUrl.getWEB();
//        WalletUrl.BaseUrl.GQL_TOKOCASH_DOMAIN = baseUrl.getGQL();
//        SessionUrl.ACCOUNTS_DOMAIN = baseUrl.getACCOUNTS();
//        UpdateInactivePhoneURL.ACCOUNTS_DOMAIN = baseUrl.getACCOUNTS();
//        InstantLoanUrl.BaseUrl.WEB_DOMAIN = baseUrl.getWEB();
//        SessionUrl.BASE_DOMAIN = baseUrl.getWEB();
//        ShopUrl.BASE_ACE_URL = baseUrl.getACE();
//        ShopCommonUrl.BASE_URL = baseUrl.getTOME();
//        ShopCommonUrl.BASE_WS_URL = baseUrl.getWS();
//        ProductDetailConstant.BASE_REST_URL = baseUrl.getWS();
//        ReputationCommonUrl.BASE_URL = baseUrl.getWS();
//        KolUrl.BASE_URL = baseUrl.getGQL();
//        DigitalUrl.WEB_DOMAIN = baseUrl.getWEB();
//        GroupChatUrl.BASE_URL = baseUrl.getCHAT();
//        GroupChatUrl.BASE_GCP_URL = baseUrl.getGROUPCHAT();
//        VoteUrl.BASE_URL = baseUrl.getCHAT();
//        GamificationUrl.GQL_BASE_URL = baseUrl.getGQL();
//        CotpUrl.BASE_URL = baseUrl.getACCOUNTS();
//        SQLoginUrl.BASE_URL = baseUrl.getWS();
//        PaymentFingerprintConstant.ACCOUNTS_DOMAIN = baseUrl.getACCOUNTS();
//        PaymentFingerprintConstant.TOP_PAY_DOMAIN = baseUrl.getPAY_ID();
//        FingerprintConstantRegister.ACCOUNTS_DOMAIN = baseUrl.getACCOUNTS();
//        FingerprintConstantRegister.TOP_PAY_DOMAIN = baseUrl.getPAY_ID();
//        OmsUrl.OMS_DOMAIN = baseUrl.getOMSCART();
//        EventsUrl.EVENTS_DOMAIN = baseUrl.getBOOKING();
//        DealsUrl.DEALS_DOMAIN = baseUrl.getBOOKING();
//        LogisticDataConstantUrl.BASE_DOMAIN = baseUrl.getWS();
//        com.tokopedia.network.constant.TkpdBaseURL.DEFAULT_TOKOPEDIA_GQL_URL = baseUrl.getGQL();;
//        GMCommonUrl.BASE_URL = baseUrl.getGOLDMERCHANT();
//        CatalogConstant.URL_HADES = baseUrl.getHADES();
//        SessionUrl.CHANGE_PHONE_DOMAIN = baseUrl.getMOBILEWEB();
//        GraphqlUrl.BASE_URL = baseUrl.getGQL();
//        ImageUploaderUrl.BASE_URL = baseUrl.getWS();
//        LogoutUrl.Companion.setBASE_URL(baseUrl.getWS());
//        ResolutionUrl.BASE_URL = baseUrl.getAPI();
//        ResolutionUrl.BASE_URL_IMAGE_SERVICE = baseUrl.getWS();
//        SettingBankUrl.Companion.setBASE_URL(baseUrl.getACCOUNTS());
//        BankListUrl.Companion.setBASE_URL(baseUrl.getACCOUNTS());
//        ChangePasswordUrl.Companion.setBASE_URL(baseUrl.getACCOUNTS());
//        TrainUrl.BASE_URL = baseUrl.getGQL();
//        TrainUrl.BASE_WEB_DOMAIN = baseUrl.getWEB();
//        TrainUrl.WEB_DOMAIN = baseUrl.getTIKET();
//        PaymentSettingUrlKt.setPAYMENT_SETTING_URL(baseUrl.getPAY());
//        AccountHomeUrl.WEB_DOMAIN = baseUrl.getWEB();
//        AccountHomeUrl.BASE_MOBILE_DOMAIN = baseUrl.getMOBILEWEB();
//        ChangePhoneNumberUrl.BASE_URL = baseUrl.getACCOUNTS();
//        PhoneVerificationConst.BASE_URL = baseUrl.getACCOUNTS();
//        ProductDetailUrl.WS_DOMAIN = baseUrl.getWS();
//        TopAdsConstantKt.setTOPADS_BASE_URL(baseUrl.getTA());
//        TalkUrl.Companion.setBASE_URL(baseUrl.getINBOX());
//        AttachProductUrl.URL = baseUrl.getACE();
//        FeedUrl.BASE_DOMAIN = baseUrl.getWS();
//        FeedUrl.GRAPHQL_DOMAIN = baseUrl.getGQL();
//        FeedUrl.TOME_DOMAIN = baseUrl.getTOME();
//        FeedUrl.MOBILE_DOMAIN = baseUrl.getMOBILEWEB();
//        ChatroomUrl.GROUP_CHAT_WEBSOCKET_DOMAIN = baseUrl.getWS_GROUPCHAT();
//        LoginRegisterUrl.BASE_DOMAIN = baseUrl.getACCOUNTS();
//        SessionCommonUrl.BASE_DOMAIN = baseUrl.getACCOUNTS();
//        SessionCommonUrl.BASE_WS_DOMAIN = baseUrl.getWS();
//        LoginRegisterPhoneUrl.BASE_DOMAIN = baseUrl.getTOKOCASH();
//        CheckMsisdnUrl.BASE_DOMAIN = baseUrl.getACCOUNTS();
//        RecentViewUrl.MOJITO_DOMAIN = baseUrl.getMOJITO();
//        com.tokopedia.network.constant.TkpdBaseURL.MOBILE_DOMAIN = baseUrl.getMOBILEWEB();
//        com.tokopedia.common_digital.common.constant.DigitalUrl.INSTANCE.setDIGITAL_API_DOMAIN(ConsumerAppBaseUrl.BASE_DIGITAL_API_DOMAIN);
//        DigitalDealsUrl.BASE_URL = baseUrl.getBOOKING();
//        LogisticDataConstantUrl.KeroRates.BASE_URL = baseUrl.getGW();
//        TransactionDataApiUrl.Cart.BASE_URL = baseUrl.getAPI();
//        TransactionDataApiUrl.TransactionAction.BASE_URL = baseUrl.getWS();
//        com.tokopedia.network.constant.TkpdBaseURL.BASE_API_DOMAIN = baseUrl.getAPI();
//        KycCommonUrl.BASE_URL = baseUrl.getMOBILEWEB();
//        DiscoveryBaseURL.Ace.ACE_DOMAIN = baseUrl.getACE();
//        CMNotificationUrls.CAMPAIGN_MANAGEMENT_DOMAIN = baseUrl.getIMT();
//        Config.TOPADS_BASE_URL = baseUrl.getTA();
//        BerandaUrl.GRAPHQL_URL = baseUrl.getGQL();
//        BerandaUrl.DOMAIN_URL = baseUrl.getWEB();
//        ChatUrl.Companion.setTOPCHAT(baseUrl.getCHAT());
//        com.tokopedia.network.constant.TkpdBaseURL.ACCOUNTS_DOMAIN = baseUrl.getACCOUNTS();
//        CMNotificationUrls.CM_TOKEN_UPDATE = baseUrl.getIMT() + "api/v1/user/add";
//        tradein_common.Constants.LAKU6_BASEURL = baseUrl.getLAKU6();
//        com.tokopedia.inbox.common.ResolutionUrl.HOSTNAME = baseUrl.getMOBILEWEB();
    }


    private void generateConsumerAppNetworkKeys() {
        AuthUtil.KEY.KEY_CREDIT_CARD_VAULT = ConsumerAppNetworkKeys.CREDIT_CARD_VAULT_AUTH_KEY;
        AuthUtil.KEY.ZEUS_WHITELIST = ConsumerAppNetworkKeys.ZEUS_WHITELIST;
        WalletUrl.KeyHmac.HMAC_PENDING_CASHBACK = ConsumerAppNetworkKeys.HMAC_PENDING_CASHBACK;

    }

    public void initializeDatabase() {
        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseHolder(ProductDraftGeneratedDatabaseHolder.class)
                .build());
        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseHolder(TkpdCacheApiGeneratedDatabaseHolder.class)
                .build());
        PushNotification.initDatabase(getApplicationContext());
        Analytics.initDB(getApplicationContext());
        CategoryDbFlow.initDatabase(getApplicationContext());
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
        new CacheApiWhiteListUseCase().executeSync(CacheApiWhiteListUseCase.createParams(
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
                //String hash_key = new String(Base64.encode(md.digest(), 0));
                sb.append("MD5: ").append(bytesToString(byteArray)).append("\n");
                md.reset();
                md = MessageDigest.getInstance("SHA");
                md.update(bytes);
                byteArray = md.digest();
                //String hash_key = new String(Base64.encode(md.digest(), 0));
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


    public void goToTokoCash(String applinkUrl, String redirectUrl, Activity activity) {

    }

    public Class<?> getDeeplinkClass() {
        return DeepLinkActivity.class;
    }

    GlobalCacheManager cacheManager;

    @Override
    public void saveCPM(@NonNull String cpm) {
        cacheManager.save(CharacterPerMinuteInterface.KEY, cpm, 60);
    }

    @Override
    public String getCPM() {
        return cacheManager.get(CharacterPerMinuteInterface.KEY);
    }

    @Override
    public boolean isEnable() {
        return getBooleanRemoteConfig("android_customer_typing_tracker_enabled", false);
    }


}
