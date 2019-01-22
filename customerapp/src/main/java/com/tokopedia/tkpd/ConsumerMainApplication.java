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
import com.tokopedia.analytics.Analytics;
import com.tokopedia.attachproduct.data.source.url.AttachProductUrl;
import com.tokopedia.cacheapi.domain.interactor.CacheApiWhiteListUseCase;
import com.tokopedia.cacheapi.util.CacheApiLoggingUtils;
import com.tokopedia.changepassword.data.ChangePasswordUrl;
import com.tokopedia.changephonenumber.ChangePhoneNumberUrl;
import com.tokopedia.chat_common.network.ChatUrl;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.HockeyAppHelper;
import com.tokopedia.digital.common.constant.DigitalUrl;
import com.tokopedia.digital.newcart.data.DigitalDealsUrl;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.events.data.source.EventsUrl;
import com.tokopedia.feedplus.data.api.FeedUrl;
import com.tokopedia.discovery.newdiscovery.constant.DiscoveryBaseURL;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.flight_dbflow.TkpdFlight;
import com.tokopedia.gamification.GamificationUrl;
import com.tokopedia.gm.common.constant.GMCommonUrl;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.source.cloud.api.GraphqlUrl;
import com.tokopedia.groupchat.chatroom.data.ChatroomUrl;
import com.tokopedia.groupchat.common.data.GroupChatUrl;
import com.tokopedia.home.account.AccountHomeUrl;
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
import com.tokopedia.notifications.common.CMNotificationUtils;
import com.tokopedia.notifications.data.source.CMNotificationUrls;
import com.tokopedia.oms.data.source.OmsUrl;
import com.tokopedia.otp.cotp.data.CotpUrl;
import com.tokopedia.otp.cotp.data.SQLoginUrl;
import com.tokopedia.payment.fingerprint.util.PaymentFingerprintConstant;
import com.tokopedia.payment.setting.util.PaymentSettingUrlKt;
import com.tokopedia.phoneverification.PhoneVerificationConst;
import com.tokopedia.product.manage.item.imagepicker.util.CatalogConstant;
import com.tokopedia.profile.data.network.TopAdsConstantKt;
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
import com.tokopedia.train.common.constant.TrainUrl;
import com.tokopedia.train.common.util.TrainDatabase;
import com.tokopedia.transaction.network.TransactionUrl;
import com.tokopedia.transactiondata.constant.TransactionDataApiUrl;
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneURL;
import com.tokopedia.useridentification.KycUrl;
import com.tokopedia.user_identification_common.KycCommonUrl;
import com.tokopedia.vote.data.VoteUrl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import io.hansel.hanselsdk.Hansel;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class ConsumerMainApplication extends ConsumerRouterApplication implements
        MoEPushCallBacks.OnMoEPushNavigationAction,
        InAppManager.InAppMessageListener {

    private final String NOTIFICATION_CHANNEL_NAME = "Promo";
    private final String NOTIFICATION_CHANNEL_ID = "custom_sound";
    private final String NOTIFICATION_CHANNEL_DESC = "notification channel for custom sound.";

    private final String FLAVOR_LIVE = "live";
    private final String FLAVOR_STAGING = "staging";
    private final String FLAVOR_ALPHA = "alpha";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    public void onCreate() {
        HockeyAppHelper.setEnableDistribution(BuildConfig.ENABLE_DISTRIBUTION);
        HockeyAppHelper.setHockeyappKey(HockeyAppHelper.KEY_MAINAPP);
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
        generateConsumerAppBaseUrl();
        generateConsumerAppNetworkKeys();

        initializeDatabase();
        super.onCreate();
        initReact();

        MoEPushCallBacks.getInstance().setOnMoEPushNavigationAction(this);
        InAppManager.getInstance().setInAppListener(this);

        IntentFilter intentFilter1 = new IntentFilter(Constants.ACTION_BC_RESET_APPLINK);
        LocalBroadcastManager.getInstance(this).registerReceiver(new ApplinkResetReceiver(), intentFilter1);
        initCacheApi();
        createCustomSoundNotificationChannel();
        Hansel.init(this);
        PushManager.getInstance().setMessageListener(new CustomPushListener());
        GraphqlClient.init(getApplicationContext());
        NetworkClient.init(getApplicationContext());
        InstabugInitalize.init(this);

        if (!GlobalConfig.DEBUG) {
            new ANRWatchDog().setANRListener(Crashlytics::logException).start();
        }
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
        TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL = ConsumerAppBaseUrl.BASE_TOKOPEDIA_WEBSITE;
        InstantLoanUrl.BaseUrl.WEB_DOMAIN = ConsumerAppBaseUrl.BASE_INSTANT_LOAN_URL;
        TkpdBaseURL.BASE_DOMAIN = ConsumerAppBaseUrl.BASE_DOMAIN;
        TkpdBaseURL.BASE_API_DOMAIN = ConsumerAppBaseUrl.BASE_API_DOMAIN;
        TkpdBaseURL.ACE_DOMAIN = ConsumerAppBaseUrl.BASE_ACE_DOMAIN;
        TkpdBaseURL.TOME_DOMAIN = ConsumerAppBaseUrl.BASE_TOME_DOMAIN;
        TkpdBaseURL.TOPADS_DOMAIN = ConsumerAppBaseUrl.BASE_TOPADS_DOMAIN;
        TkpdBaseURL.MOJITO_DOMAIN = ConsumerAppBaseUrl.BASE_MOJITO_DOMAIN;
        TkpdBaseURL.HADES_DOMAIN = ConsumerAppBaseUrl.BASE_HADES_DOMAIN;
        TkpdBaseURL.ACCOUNTS_DOMAIN = ConsumerAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        TkpdBaseURL.INBOX_DOMAIN = ConsumerAppBaseUrl.BASE_INBOX_DOMAIN;
        TkpdBaseURL.JS_DOMAIN = ConsumerAppBaseUrl.BASE_JS_DOMAIN;
        TkpdBaseURL.KERO_DOMAIN = ConsumerAppBaseUrl.BASE_KERO_DOMAIN;
        TkpdBaseURL.KERO_RATES_DOMAIN = ConsumerAppBaseUrl.BASE_KERO_RATES_DOMAIN;
        TkpdBaseURL.JAHE_DOMAIN = ConsumerAppBaseUrl.BASE_JAHE_DOMAIN;
        TkpdBaseURL.PULSA_WEB_DOMAIN = ConsumerAppBaseUrl.BASE_PULSA_WEB_DOMAIN;
        TkpdBaseURL.GOLD_MERCHANT_DOMAIN = ConsumerAppBaseUrl.BASE_GOLD_MERCHANT_DOMAIN;
        TkpdBaseURL.WEB_DOMAIN = ConsumerAppBaseUrl.BASE_WEB_DOMAIN;
        TkpdBaseURL.MOBILE_DOMAIN = ConsumerAppBaseUrl.BASE_MOBILE_DOMAIN;
        TkpdBaseURL.BASE_CONTACT_US = ConsumerAppBaseUrl.BASE_WEB_DOMAIN + "contact-us";
        TkpdBaseURL.RIDE_DOMAIN = ConsumerAppBaseUrl.BASE_RIDE_DOMAIN;
        TkpdBaseURL.TOKO_CASH_DOMAIN = ConsumerAppBaseUrl.BASE_TOKO_CASH_DOMAIN;
        TkpdBaseURL.BASE_ACTION = ConsumerAppBaseUrl.BASE_DOMAIN + "v4/action/";
        TkpdBaseURL.DIGITAL_API_DOMAIN = ConsumerAppBaseUrl.BASE_DIGITAL_API_DOMAIN;
        TkpdBaseURL.DIGITAL_WEBSITE_DOMAIN = ConsumerAppBaseUrl.BASE_DIGITAL_WEBSITE_DOMAIN;
        TkpdBaseURL.GRAPHQL_DOMAIN = ConsumerAppBaseUrl.GRAPHQL_DOMAIN;
        TkpdBaseURL.HOME_DATA_BASE_URL = ConsumerAppBaseUrl.HOME_DATA_BASE_URL;
        TkpdBaseURL.SCROOGE_DOMAIN = ConsumerAppBaseUrl.SCROOGE_DOMAIN;
        TkpdBaseURL.SCROOGE_CREDIT_CARD_DOMAIN = ConsumerAppBaseUrl.SCROOGE_CREDIT_CARD_DOMAIN;
        TkpdBaseURL.PAYMENT_DOMAIN = ConsumerAppBaseUrl.PAYMENT_DOMAIN;
        TkpdBaseURL.GALADRIEL = ConsumerAppBaseUrl.GALADRIEL;
        TkpdBaseURL.CHAT_DOMAIN = ConsumerAppBaseUrl.CHAT_DOMAIN;
        TkpdBaseURL.CHAT_WEBSOCKET_DOMAIN = ConsumerAppBaseUrl.CHAT_WEBSOCKET_DOMAIN;
        TkpdBaseURL.MAPS_DOMAIN = ConsumerAppBaseUrl.MAPS_DOMAIN;
        TkpdBaseURL.WALLET_DOMAIN = ConsumerAppBaseUrl.BASE_WALLET;
        TkpdBaseURL.EVENTS_DOMAIN = ConsumerAppBaseUrl.EVENT_DOMAIN;
        TkpdBaseURL.TOKOPOINT_API_DOMAIN = ConsumerAppBaseUrl.TOKOPOINT_API_DOMAIN;
        FlightUrl.BASE_URL = ConsumerAppBaseUrl.BASE_API_DOMAIN;
        FlightUrl.WEB_DOMAIN = ConsumerAppBaseUrl.BASE_WEB_DOMAIN;
        AbstractionBaseURL.JS_DOMAIN = ConsumerAppBaseUrl.BASE_JS_DOMAIN;
        FlightUrl.ALL_PROMO_LINK = ConsumerAppBaseUrl.BASE_WEB_DOMAIN + FlightUrl.PROMO_PATH;
        FlightUrl.CONTACT_US = ConsumerAppBaseUrl.BASE_WEB_DOMAIN + FlightUrl.CONTACT_US_PATH;
        FlightUrl.CONTACT_US_FLIGHT = FlightUrl.CONTACT_US + FlightUrl.CONTACT_US_FLIGHT_HOME_PREFIX;
        FlightUrl.CONTACT_US_FLIGHT_PREFIX_GLOBAL = FlightUrl.CONTACT_US + FlightUrl.CONTACT_US_FLIGHT_PREFIX;
        TransactionUrl.BASE_URL = ConsumerAppBaseUrl.BASE_API_DOMAIN;
        WalletUrl.BaseUrl.ACCOUNTS_DOMAIN = ConsumerAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        WalletUrl.BaseUrl.WALLET_DOMAIN = ConsumerAppBaseUrl.BASE_WALLET;
        WalletUrl.BaseUrl.WEB_DOMAIN = ConsumerAppBaseUrl.BASE_WEB_DOMAIN;
        WalletUrl.BaseUrl.GQL_TOKOCASH_DOMAIN = ConsumerAppBaseUrl.GRAPHQL_DOMAIN;
        SessionUrl.ACCOUNTS_DOMAIN = ConsumerAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        UpdateInactivePhoneURL.ACCOUNTS_DOMAIN = ConsumerAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        InstantLoanUrl.BaseUrl.WEB_DOMAIN = ConsumerAppBaseUrl.BASE_WEB_DOMAIN;
        SessionUrl.BASE_DOMAIN = ConsumerAppBaseUrl.BASE_DOMAIN;
        ShopUrl.BASE_ACE_URL = ConsumerAppBaseUrl.BASE_ACE_DOMAIN;
        ShopCommonUrl.BASE_URL = ConsumerAppBaseUrl.BASE_TOME_DOMAIN;
        ShopCommonUrl.BASE_WS_URL = ConsumerAppBaseUrl.BASE_DOMAIN;
        ReputationCommonUrl.BASE_URL = ConsumerAppBaseUrl.BASE_DOMAIN;
        KolUrl.BASE_URL = ConsumerAppBaseUrl.GRAPHQL_DOMAIN;
        DigitalUrl.WEB_DOMAIN = ConsumerAppBaseUrl.BASE_WEB_DOMAIN;
        GroupChatUrl.BASE_URL = ConsumerAppBaseUrl.CHAT_DOMAIN;
        VoteUrl.BASE_URL = ConsumerAppBaseUrl.CHAT_DOMAIN;
        GamificationUrl.GQL_BASE_URL = ConsumerAppBaseUrl.GAMIFICATION_BASE_URL;
        CotpUrl.BASE_URL = ConsumerAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        SQLoginUrl.BASE_URL = ConsumerAppBaseUrl.BASE_DOMAIN;
        PaymentFingerprintConstant.ACCOUNTS_DOMAIN = ConsumerAppBaseUrl.ACCOUNTS_DOMAIN;
        PaymentFingerprintConstant.TOP_PAY_DOMAIN = ConsumerAppBaseUrl.TOP_PAY_DOMAIN;
        FingerprintConstantRegister.ACCOUNTS_DOMAIN = ConsumerAppBaseUrl.ACCOUNTS_DOMAIN;
        FingerprintConstantRegister.TOP_PAY_DOMAIN = ConsumerAppBaseUrl.TOP_PAY_DOMAIN;
        OmsUrl.OMS_DOMAIN = ConsumerAppBaseUrl.OMS_DOMAIN;
        EventsUrl.EVENTS_DOMAIN = ConsumerAppBaseUrl.EVENT_DOMAIN;
        DealsUrl.DEALS_DOMAIN = ConsumerAppBaseUrl.DEALS_DOMAIN;
        LogisticDataConstantUrl.BASE_DOMAIN = ConsumerAppBaseUrl.BASE_DOMAIN;
        com.tokopedia.network.constant.TkpdBaseURL.DEFAULT_TOKOPEDIA_GQL_URL = ConsumerAppBaseUrl.BASE_TOKOPEDIA_GQL;
        GMCommonUrl.BASE_URL = ConsumerAppBaseUrl.BASE_GOLD_MERCHANT_DOMAIN;
        CatalogConstant.URL_HADES = ConsumerAppBaseUrl.BASE_HADES_DOMAIN;
        SessionUrl.CHANGE_PHONE_DOMAIN = ConsumerAppBaseUrl.CHANGE_PHONE_DOMAIN;
        GraphqlUrl.BASE_URL = ConsumerAppBaseUrl.GRAPHQL_DOMAIN;
        ImageUploaderUrl.BASE_URL = ConsumerAppBaseUrl.BASE_DOMAIN;
        LogoutUrl.Companion.setBASE_URL(ConsumerAppBaseUrl.BASE_DOMAIN);
        ResolutionUrl.BASE_URL = ConsumerAppBaseUrl.BASE_API_DOMAIN;
        ResolutionUrl.BASE_URL_IMAGE_SERVICE = ConsumerAppBaseUrl.BASE_DOMAIN;
        SettingBankUrl.Companion.setBASE_URL(ConsumerAppBaseUrl.ACCOUNTS_DOMAIN);
        BankListUrl.Companion.setBASE_URL(ConsumerAppBaseUrl.ACCOUNTS_DOMAIN);
        ChangePasswordUrl.Companion.setBASE_URL(ConsumerAppBaseUrl.BASE_ACCOUNTS_DOMAIN);
        TrainUrl.BASE_URL = ConsumerAppBaseUrl.GRAPHQL_DOMAIN;
        TrainUrl.BASE_WEB_DOMAIN = ConsumerAppBaseUrl.BASE_WEB_DOMAIN;
        TrainUrl.WEB_DOMAIN = ConsumerAppBaseUrl.KAI_WEB_DOMAIN;
        PaymentSettingUrlKt.setPAYMENT_SETTING_URL(ConsumerAppBaseUrl.PAYMENT_DOMAIN);
        AccountHomeUrl.WEB_DOMAIN = ConsumerAppBaseUrl.BASE_WEB_DOMAIN;
        AccountHomeUrl.BASE_MOBILE_DOMAIN = ConsumerAppBaseUrl.BASE_MOBILE_DOMAIN;
        ChangePhoneNumberUrl.BASE_URL = ConsumerAppBaseUrl.ACCOUNTS_DOMAIN;
        PhoneVerificationConst.BASE_URL = ConsumerAppBaseUrl.ACCOUNTS_DOMAIN;
        ProductDetailUrl.WS_DOMAIN = ConsumerAppBaseUrl.BASE_DOMAIN;
        TopAdsConstantKt.setTOPADS_BASE_URL(ConsumerAppBaseUrl.BASE_TOPADS_DOMAIN);
        TalkUrl.Companion.setBASE_URL(ConsumerAppBaseUrl.BASE_INBOX_DOMAIN);
        AttachProductUrl.URL = ConsumerAppBaseUrl.BASE_ACE_DOMAIN;
        FeedUrl.BASE_DOMAIN = ConsumerAppBaseUrl.BASE_DOMAIN;
        FeedUrl.GRAPHQL_DOMAIN = ConsumerAppBaseUrl.GRAPHQL_DOMAIN;
        FeedUrl.TOME_DOMAIN = ConsumerAppBaseUrl.BASE_TOME_DOMAIN;
        FeedUrl.MOBILE_DOMAIN = ConsumerAppBaseUrl.BASE_MOBILE_DOMAIN;
        ChatroomUrl.GROUP_CHAT_WEBSOCKET_DOMAIN = ConsumerAppBaseUrl.GROUP_CHAT_WEBSOCKET_DOMAIN;
        LoginRegisterUrl.BASE_DOMAIN = ConsumerAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        SessionCommonUrl.BASE_DOMAIN = ConsumerAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        SessionCommonUrl.BASE_WS_DOMAIN = ConsumerAppBaseUrl.BASE_DOMAIN;
        LoginRegisterPhoneUrl.BASE_DOMAIN = ConsumerAppBaseUrl.BASE_WALLET;
        CheckMsisdnUrl.BASE_DOMAIN = ConsumerAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        RecentViewUrl.MOJITO_DOMAIN = ConsumerAppBaseUrl.BASE_MOJITO_DOMAIN;
        com.tokopedia.network.constant.TkpdBaseURL.MOBILE_DOMAIN = ConsumerAppBaseUrl.BASE_MOBILE_DOMAIN;
        com.tokopedia.common_digital.common.constant.DigitalUrl.DIGITAL_API_DOMAIN = ConsumerAppBaseUrl.BASE_DIGITAL_API_DOMAIN;
        DigitalDealsUrl.BASE_URL = ConsumerAppBaseUrl.DEALS_DOMAIN;
        LogisticDataConstantUrl.KeroRates.BASE_URL = ConsumerAppBaseUrl.LOGISTIC_BASE_DOMAIN;
        TransactionDataApiUrl.Cart.BASE_URL = ConsumerAppBaseUrl.CART_BASE_DOMAIN;
        TransactionDataApiUrl.TransactionAction.BASE_URL = ConsumerAppBaseUrl.TRANSACTION_BASE_DOMAIN;
        com.tokopedia.network.constant.TkpdBaseURL.BASE_API_DOMAIN = ConsumerAppBaseUrl.BASE_API_DOMAIN;
        KycCommonUrl.BASE_URL = ConsumerAppBaseUrl.BASE_MOBILE_DOMAIN;
        DiscoveryBaseURL.Ace.ACE_DOMAIN = ConsumerAppBaseUrl.BASE_ACE_DOMAIN;
        CMNotificationUrls.CAMPAIGN_MANAGEMENT_DOMAIN = ConsumerAppBaseUrl.CAMPAIGN_MANAGEMENT_DOMAIN;
        Config.TOPADS_BASE_URL = ConsumerAppBaseUrl.BASE_TOPADS_DOMAIN;
        ChatUrl.Companion.setTOPCHAT(ConsumerAppBaseUrl.CHAT_DOMAIN);
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
        TrainDatabase.init(getApplicationContext());
        TkpdFlight.initDatabase(getApplicationContext());
        PushNotification.initDatabase(getApplicationContext());
        Analytics.initDB(getApplicationContext());
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
}
