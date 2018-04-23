package com.tokopedia.tkpd;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
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

import com.facebook.soloader.SoLoader;
import com.moengage.inapp.InAppManager;
import com.moengage.inapp.InAppMessage;
import com.moengage.inapp.InAppTracker;
import com.moengage.pushbase.push.MoEPushCallBacks;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TkpdCacheApiGeneratedDatabaseHolder;
import com.raizlabs.android.dbflow.config.TkpdSellerGeneratedDatabaseHolder;
import com.sendbird.android.SendBird;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.constant.AbstractionBaseURL;
import com.tokopedia.cacheapi.domain.interactor.CacheApiWhiteListUseCase;
import com.tokopedia.cacheapi.util.CacheApiLoggingUtils;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.HockeyAppHelper;
import com.tokopedia.digital.common.constant.DigitalUrl;
import com.tokopedia.flight.TkpdFlight;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.groupchat.common.data.GroupChatUrl;
import com.tokopedia.groupchat.common.data.SendbirdKey;
import com.tokopedia.inbox.inboxchat.data.network.ChatBotUrl;
import com.tokopedia.gamification.GamificationUrl;
import com.tokopedia.network.SessionUrl;
import com.tokopedia.payment.fingerprint.util.PaymentFingerprintConstant;
import com.tokopedia.pushnotif.PushNotification;
import com.tokopedia.profile.data.network.ProfileUrl;
import com.tokopedia.profile.view.activity.TopProfileActivity;
import com.tokopedia.reputation.common.constant.ReputationCommonUrl;
import com.tokopedia.session.login.loginemail.view.activity.LoginActivity;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;
import com.tokopedia.tkpd.fcm.ApplinkResetReceiver;
import com.tokopedia.tkpd.utils.CacheApiWhiteList;
import com.tokopedia.shop.common.constant.ShopCommonUrl;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.kol.common.network.KolUrl;
import com.tokopedia.tokocash.network.api.WalletUrl;
import com.tokopedia.transaction.network.TransactionUrl;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class ConsumerMainApplication extends ConsumerRouterApplication implements
        MoEPushCallBacks.OnMoEPushNavigationAction,
        InAppManager.InAppMessageListener {

    private final String NOTIFICATION_CHANNEL_NAME = "Promo";
    private final String NOTIFICATION_CHANNEL_ID = "custom_sound";
    private final String NOTIFICATION_CHANNEL_DESC = "notification channel for custom sound.";
    @Override
    public void onCreate() {
        HockeyAppHelper.setEnableDistribution(BuildConfig.ENABLE_DISTRIBUTION);
        HockeyAppHelper.setHockeyappKey(HockeyAppHelper.KEY_MAINAPP);
        setVersionCode();
        GlobalConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
        GlobalConfig.DEBUG = BuildConfig.DEBUG;
        GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
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
        initSendbird();
        createCustomSoundNotificationChannel();
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

    private void initSendbird() {
        SendBird.init(SendbirdKey.APP_ID, this);
    }

    private void generateConsumerAppBaseUrl() {
        TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL = ConsumerAppBaseUrl.BASE_TOKOPEDIA_WEBSITE;
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
        FlightUrl.PULSA_BASE_URL = ConsumerAppBaseUrl.BASE_DIGITAL_API_DOMAIN;
        AbstractionBaseURL.JS_DOMAIN = ConsumerAppBaseUrl.BASE_JS_DOMAIN;
        FlightUrl.BANNER_PATH = ConsumerAppBaseUrl.BASE_DIGITAL_API_DOMAIN + FlightUrl.FLIGHT_BANNER_VERSION;
        FlightUrl.ALL_PROMO_LINK = ConsumerAppBaseUrl.BASE_WEB_DOMAIN + FlightUrl.PROMO_PATH;
        FlightUrl.CONTACT_US = ConsumerAppBaseUrl.BASE_WEB_DOMAIN + FlightUrl.CONTACT_US_PATH;
        FlightUrl.CONTACT_US_FLIGHT_PREFIX_GLOBAL = FlightUrl.CONTACT_US + FlightUrl.CONTACT_US_FLIGHT_PREFIX;
        TransactionUrl.BASE_URL = ConsumerAppBaseUrl.BASE_API_DOMAIN;
        WalletUrl.BaseUrl.ACCOUNTS_DOMAIN = ConsumerAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        WalletUrl.BaseUrl.WALLET_DOMAIN = ConsumerAppBaseUrl.BASE_WALLET;
        SessionUrl.ACCOUNTS_DOMAIN = ConsumerAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        SessionUrl.BASE_DOMAIN = ConsumerAppBaseUrl.BASE_DOMAIN;
        ShopUrl.BASE_ACE_URL = ConsumerAppBaseUrl.BASE_ACE_DOMAIN;
        ShopCommonUrl.BASE_URL = ConsumerAppBaseUrl.BASE_TOME_DOMAIN;
        ShopCommonUrl.BASE_WS_URL = ConsumerAppBaseUrl.BASE_DOMAIN;
        ReputationCommonUrl.BASE_URL = ConsumerAppBaseUrl.BASE_DOMAIN;
        KolUrl.BASE_URL = ConsumerAppBaseUrl.GRAPHQL_DOMAIN;
        ProfileUrl.BASE_URL = ConsumerAppBaseUrl.TOPPROFILE_DOMAIN;
        DigitalUrl.WEB_DOMAIN = ConsumerAppBaseUrl.BASE_WEB_DOMAIN;
        GroupChatUrl.BASE_URL = ConsumerAppBaseUrl.CHAT_DOMAIN;
        GamificationUrl.GQL_BASE_URL = ConsumerAppBaseUrl.GAMIFICATION_BASE_URL;
        ChatBotUrl.BASE_URL = ConsumerAppBaseUrl.CHATBOT_DOMAIN;
        PaymentFingerprintConstant.ACCOUNTS_DOMAIN = ConsumerAppBaseUrl.ACCOUNTS_DOMAIN;
        PaymentFingerprintConstant.TOP_PAY_DOMAIN = ConsumerAppBaseUrl.TOP_PAY_DOMAIN;
    }

    private void generateConsumerAppNetworkKeys() {
        AuthUtil.KEY.KEY_CREDIT_CARD_VAULT = ConsumerAppNetworkKeys.CREDIT_CARD_VAULT_AUTH_KEY;
        AuthUtil.KEY.ZEUS_WHITELIST = ConsumerAppNetworkKeys.ZEUS_WHITELIST;
        WalletUrl.KeyHmac.HMAC_PENDING_CASHBACK = ConsumerAppNetworkKeys.HMAC_PENDING_CASHBACK;
        SendbirdKey.APP_ID = ConsumerAppNetworkKeys.SENDBIRD_APP_ID;

    }

    public void initializeDatabase() {
        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseHolder(TkpdSellerGeneratedDatabaseHolder.class)
                .build());
        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseHolder(TkpdCacheApiGeneratedDatabaseHolder.class)
                .build());
        TkpdFlight.initDatabase(getApplicationContext());
        PushNotification.initDatabase(getApplicationContext());
    }

    @Override
    public boolean onClick(@Nullable String screenName, @Nullable Bundle extras, @Nullable Uri deepLinkUri) {
        CommonUtils.dumper("GAv4 MOE NGGAGE on notif click " + deepLinkUri + " bundle " + extras);
        return handleClick(screenName, extras, deepLinkUri);
    }

    @Override
    public void onInAppShown(InAppMessage message) {
        InAppTracker.getInstance(this).trackInAppClicked(message);
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
}
