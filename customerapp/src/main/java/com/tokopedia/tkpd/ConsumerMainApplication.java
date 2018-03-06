package com.tokopedia.tkpd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatDelegate;

import com.facebook.soloader.SoLoader;
import com.moengage.inapp.InAppManager;
import com.moengage.inapp.InAppMessage;
import com.moengage.inapp.InAppTracker;
import com.moengage.pushbase.push.MoEPushCallBacks;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TkpdSellerGeneratedDatabaseHolder;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.constant.AbstractionBaseURL;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.HockeyAppHelper;
import com.tokopedia.network.SessionUrl;
import com.tokopedia.session.login.loginemail.view.activity.LoginActivity;
import com.tokopedia.flight.TkpdFlight;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;
import com.tokopedia.tkpd.fcm.ApplinkResetReceiver;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class ConsumerMainApplication extends ConsumerRouterApplication implements
        MoEPushCallBacks.OnMoEPushNavigationAction,
        InAppManager.InAppMessageListener {

    @Override
    public void onCreate() {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        HockeyAppHelper.setEnableDistribution(BuildConfig.ENABLE_DISTRIBUTION);
        HockeyAppHelper.setHockeyappKey(HockeyAppHelper.KEY_MAINAPP);
        GlobalConfig.VERSION_CODE = BuildConfig.VERSION_CODE;
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
    }

    private void generateConsumerAppBaseUrl() {
        TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL = ConsumerAppBaseUrl.BASE_TOKOPEDIA_WEBSITE;
        TkpdBaseURL.BASE_DOMAIN = ConsumerAppBaseUrl.BASE_DOMAIN;
        TkpdBaseURL.BASE_API_DOMAIN = ConsumerAppBaseUrl.BASE_API_DOMAIN;
        TkpdBaseURL.ACE_DOMAIN = ConsumerAppBaseUrl.BASE_ACE_DOMAIN;
        TkpdBaseURL.TOME_DOMAIN = ConsumerAppBaseUrl.BASE_TOME_DOMAIN;
        TkpdBaseURL.CLOVER_DOMAIN = ConsumerAppBaseUrl.BASE_CLOVER_DOMAIN;
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
        SessionUrl.ACCOUNTS_DOMAIN = ConsumerAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        SessionUrl.BASE_DOMAIN = ConsumerAppBaseUrl.BASE_DOMAIN;
    }

    private void generateConsumerAppNetworkKeys() {
        AuthUtil.KEY.KEY_CREDIT_CARD_VAULT = ConsumerAppNetworkKeys.CREDIT_CARD_VAULT_AUTH_KEY;
        AuthUtil.KEY.ZEUS_WHITELIST = ConsumerAppNetworkKeys.ZEUS_WHITELIST;
    }

    public void initializeDatabase() {
        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseHolder(TkpdSellerGeneratedDatabaseHolder.class)
                .build());
        TkpdFlight.initDatabase(getApplicationContext());
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

            } else if (deepLinkUri.getScheme().equals(Constants.Schemes.APPLINKS)) {
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

    @Override
    public Intent getSellerHomeIntent(Activity activity) {
        return null;
    }

    @Override
    public Intent getLoginGoogleIntent(Context context) {
        return LoginActivity.getAutoLoginGoogle(context);
    }

    @Override
    public Intent getLoginFacebookIntent(Context context) {
        return LoginActivity.getAutoLoginFacebook(context);

    }

    @Override
    public Intent getLoginWebviewIntent(Context context, String name, String url) {
        return LoginActivity.getAutoLoginWebview(context, name, url);
    }
}
