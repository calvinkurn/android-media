package com.tokopedia.sellerapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.moengage.inapp.InAppManager;
import com.moengage.inapp.InAppMessage;
import com.moengage.inapp.InAppTracker;
import com.moengage.pushbase.push.MoEPushCallBacks;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.ProductDraftGeneratedDatabaseHolder;
import com.raizlabs.android.dbflow.config.TkpdCacheApiGeneratedDatabaseHolder;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.constant.AbstractionBaseURL;
import com.tokopedia.attachproduct.data.source.url.AttachProductUrl;
import com.tokopedia.cacheapi.domain.interactor.CacheApiWhiteListUseCase;
import com.tokopedia.cacheapi.util.CacheApiLoggingUtils;
import com.tokopedia.changepassword.data.ChangePasswordUrl;
import com.tokopedia.chat_common.network.ChatUrl;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.HockeyAppHelper;
import com.tokopedia.digital.common.constant.DigitalUrl;
import com.tokopedia.gm.common.constant.GMCommonUrl;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.source.cloud.api.GraphqlUrl;
import com.tokopedia.imageuploader.data.ImageUploaderUrl;
import com.tokopedia.loginregister.common.data.LoginRegisterUrl;
import com.tokopedia.logout.data.LogoutUrl;
import com.tokopedia.mitratoppers.common.constant.MitraToppersBaseURL;
import com.tokopedia.network.SessionUrl;
import com.tokopedia.otp.cotp.data.CotpUrl;
import com.tokopedia.otp.cotp.data.SQLoginUrl;
import com.tokopedia.payment.fingerprint.util.PaymentFingerprintConstant;
import com.tokopedia.payment.setting.util.PaymentSettingUrlKt;
import com.tokopedia.product.manage.item.imagepicker.util.CatalogConstant;
import com.tokopedia.pushnotif.PushNotification;
import com.tokopedia.reputation.common.constant.ReputationCommonUrl;
import com.tokopedia.sellerapp.deeplink.DeepLinkActivity;
import com.tokopedia.sellerapp.deeplink.DeepLinkHandlerActivity;
import com.tokopedia.sellerapp.utils.CacheApiWhiteList;
import com.tokopedia.sessioncommon.data.SessionCommonUrl;
import com.tokopedia.settingbank.banklist.data.SettingBankUrl;
import com.tokopedia.settingbank.choosebank.data.BankListUrl;
import com.tokopedia.shop.common.constant.ShopCommonUrl;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.talk.common.data.TalkUrl;
import com.tokopedia.topads.common.constant.TopAdsCommonConstant;
import com.tokopedia.transaction.orders.orderlist.view.activity.SellerOrderListActivity;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class SellerMainApplication extends SellerRouterApplication implements MoEPushCallBacks.OnMoEPushNavigationAction,
        InAppManager.InAppMessageListener {

    public static final int SELLER_APPLICATION = 2;

    public static SellerMainApplication get(Context context) {
        return (SellerMainApplication) context.getApplicationContext();
    }

    @Override
    public void onInAppShown(InAppMessage message) {
        InAppTracker.getInstance(this).trackInAppClicked(message);
    }

    @Override
    public boolean showInAppMessage(InAppMessage message) {
        InAppTracker.getInstance(this).trackInAppClicked(message);
        return false;
    }

    @Override
    public void onInAppClosed(InAppMessage message) {

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

        CommonUtils.dumper("FCM moengage SELLER clicked " + deepLinkUri.toString());

        if (deepLinkUri != null) {

            if (deepLinkUri.getScheme().equals(Constants.Schemes.HTTP) || deepLinkUri.getScheme().equals(Constants.Schemes.HTTPS)) {
                Intent intent = new Intent(this, DeepLinkActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse(deepLinkUri.toString()));
                startActivity(intent);

            } else if (deepLinkUri.getScheme().equals(Constants.Schemes.APPLINKS_SELLER)) {
                Intent intent = new Intent(this, DeepLinkHandlerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse(deepLinkUri.toString()));
                startActivity(intent);

            } else {
                CommonUtils.dumper("FCM entered no one");
            }

            return true;
        } else {
            return false;
        }

    }

    @Override
    public int getApplicationType() {
        return SELLER_APPLICATION;
    }

    @Override
    public void onCreate() {
        HockeyAppHelper.setEnableDistribution(BuildConfig.ENABLE_DISTRIBUTION);
        HockeyAppHelper.setAllowAnonymousLogin(true);
        HockeyAppHelper.setHockeyappKey(HockeyAppHelper.KEY_SELLERAPP);
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION;
        GlobalConfig.PACKAGE_APPLICATION = GlobalConfig.PACKAGE_SELLER_APP;
        setVersionCode();
        GlobalConfig.DEBUG = BuildConfig.DEBUG;
        GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        com.tokopedia.config.GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION;
        com.tokopedia.config.GlobalConfig.PACKAGE_APPLICATION = GlobalConfig.PACKAGE_SELLER_APP;
        com.tokopedia.config.GlobalConfig.DEBUG = BuildConfig.DEBUG;
        com.tokopedia.config.GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        setVersionCode();
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            com.tokopedia.core.util.GlobalConfig.VERSION_NAME = pInfo.versionName;
            com.tokopedia.config.GlobalConfig.VERSION_NAME = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        generateSellerAppBaseUrl();
        generateSellerAppNetworkKeys();
        super.onCreate();

        MoEPushCallBacks.getInstance().setOnMoEPushNavigationAction(this);
        InAppManager.getInstance().setInAppListener(this);
        initCacheApi();
        GraphqlClient.init(getApplicationContext());
        InstabugInitalize.init(this);
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

    private void generateSellerAppBaseUrl() {
        TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL = SellerAppBaseUrl.BASE_TOKOPEDIA_WEBSITE;
        TkpdBaseURL.BASE_DOMAIN = SellerAppBaseUrl.BASE_DOMAIN;
        TkpdBaseURL.ACE_DOMAIN = SellerAppBaseUrl.BASE_ACE_DOMAIN;
        TkpdBaseURL.BASE_API_DOMAIN = SellerAppBaseUrl.BASE_API_DOMAIN;
        TkpdBaseURL.TOPADS_DOMAIN = SellerAppBaseUrl.BASE_TOPADS_DOMAIN;
        TopAdsCommonConstant.BASE_DOMAIN_URL = SellerAppBaseUrl.BASE_TOPADS_DOMAIN;
        TkpdBaseURL.MOJITO_DOMAIN = SellerAppBaseUrl.BASE_MOJITO_DOMAIN;
        TkpdBaseURL.HADES_DOMAIN = SellerAppBaseUrl.BASE_HADES_DOMAIN;
        TkpdBaseURL.DIGITAL_API_DOMAIN = SellerAppBaseUrl.BASE_DIGITAL_API_DOMAIN;
        TkpdBaseURL.DIGITAL_WEBSITE_DOMAIN = SellerAppBaseUrl.BASE_DIGITAL_WEBSITE_DOMAIN;
        TkpdBaseURL.ACCOUNTS_DOMAIN = SellerAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        TkpdBaseURL.INBOX_DOMAIN = SellerAppBaseUrl.BASE_INBOX_DOMAIN;
        TkpdBaseURL.JS_DOMAIN = SellerAppBaseUrl.BASE_JS_DOMAIN;
        TkpdBaseURL.KERO_DOMAIN = SellerAppBaseUrl.BASE_KERO_DOMAIN;
        TkpdBaseURL.JAHE_DOMAIN = SellerAppBaseUrl.BASE_JAHE_DOMAIN;
        TkpdBaseURL.PULSA_WEB_DOMAIN = SellerAppBaseUrl.BASE_PULSA_WEB_DOMAIN;
        TkpdBaseURL.GOLD_MERCHANT_DOMAIN = SellerAppBaseUrl.BASE_GOLD_MERCHANT_DOMAIN;
        TkpdBaseURL.TOKOPEDIA_CART_DOMAIN = SellerAppBaseUrl.TOKOPEDIA_CART_DOMAIN;
        TkpdBaseURL.WEB_DOMAIN = SellerAppBaseUrl.BASE_WEB_DOMAIN;
        TkpdBaseURL.MOBILE_DOMAIN = SellerAppBaseUrl.BASE_MOBILE_DOMAIN;
        TkpdBaseURL.BASE_CONTACT_US = SellerAppBaseUrl.BASE_WEB_DOMAIN + "contact-us";
        TkpdBaseURL.TOME_DOMAIN = SellerAppBaseUrl.BASE_TOME_DOMAIN;
        TkpdBaseURL.SCROOGE_DOMAIN = SellerAppBaseUrl.BASE_PAYMENT_URL_DOMAIN;
        TkpdBaseURL.SCROOGE_CREDIT_CARD_DOMAIN = SellerAppBaseUrl.BASE_SCROOGE_CREDIT_CARD_DOMAIN;
        TkpdBaseURL.CHAT_DOMAIN = SellerAppBaseUrl.CHAT_DOMAIN;
        TkpdBaseURL.CHAT_WEBSOCKET_DOMAIN = SellerAppBaseUrl.CHAT_WEBSOCKET_DOMAIN;
        TkpdBaseURL.WALLET_DOMAIN = SellerAppBaseUrl.BASE_WALLET;
        SessionUrl.ACCOUNTS_DOMAIN = SellerAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        SessionUrl.BASE_DOMAIN = SellerAppBaseUrl.BASE_DOMAIN;
        ShopUrl.BASE_ACE_URL = SellerAppBaseUrl.BASE_ACE_DOMAIN;
        ShopCommonUrl.BASE_URL = SellerAppBaseUrl.BASE_TOME_DOMAIN;
        ShopCommonUrl.BASE_WS_URL = SellerAppBaseUrl.BASE_DOMAIN;
        ReputationCommonUrl.BASE_URL = SellerAppBaseUrl.BASE_DOMAIN;
        AbstractionBaseURL.JS_DOMAIN = SellerAppBaseUrl.BASE_JS_DOMAIN;
        CatalogConstant.URL_HADES = SellerAppBaseUrl.BASE_HADES_DOMAIN;

        MitraToppersBaseURL.WEB_DOMAIN = SellerAppBaseUrl.BASE_WEB_DOMAIN;
        MitraToppersBaseURL.PATH_MITRA_TOPPERS = SellerAppBaseUrl.PATH_MITRA_TOPPERS;
        DigitalUrl.WEB_DOMAIN = SellerAppBaseUrl.BASE_WEB_DOMAIN;
        PaymentFingerprintConstant.ACCOUNTS_DOMAIN = SellerAppBaseUrl.ACCOUNTS_DOMAIN;
        PaymentFingerprintConstant.TOP_PAY_DOMAIN = SellerAppBaseUrl.TOP_PAY_DOMAIN;
        TkpdBaseURL.HOME_DATA_BASE_URL = SellerAppBaseUrl.HOME_DATA_BASE_URL;
        CotpUrl.BASE_URL = SellerAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        GMCommonUrl.BASE_URL = SellerAppBaseUrl.BASE_GOLD_MERCHANT_DOMAIN;
        SQLoginUrl.BASE_URL = SellerAppBaseUrl.BASE_DOMAIN;
        SessionUrl.CHANGE_PHONE_DOMAIN = SellerAppBaseUrl.CHANGE_PHONE_DOMAIN;
        GraphqlUrl.BASE_URL = SellerAppBaseUrl.GRAPHQL_DOMAIN;
        ImageUploaderUrl.BASE_URL = SellerAppBaseUrl.BASE_DOMAIN;
        LogoutUrl.Companion.setBASE_URL(SellerAppBaseUrl.BASE_DOMAIN);
        SettingBankUrl.Companion.setBASE_URL(SellerAppBaseUrl.ACCOUNTS_DOMAIN);
        BankListUrl.Companion.setBASE_URL(SellerAppBaseUrl.ACCOUNTS_DOMAIN);
        ChangePasswordUrl.Companion.setBASE_URL(SellerAppBaseUrl.BASE_ACCOUNTS_DOMAIN);
        PaymentSettingUrlKt.setPAYMENT_SETTING_URL(SellerAppBaseUrl.PAYMENT_DOMAIN);
        AttachProductUrl.URL = SellerAppBaseUrl.BASE_ACE_DOMAIN;
        TalkUrl.Companion.setBASE_URL(SellerAppBaseUrl.BASE_INBOX_DOMAIN);
        LoginRegisterUrl.BASE_DOMAIN = SellerAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        SessionCommonUrl.BASE_DOMAIN = SellerAppBaseUrl.BASE_ACCOUNTS_DOMAIN;
        SessionCommonUrl.BASE_WS_DOMAIN = SellerAppBaseUrl.BASE_DOMAIN;
        ChatUrl.Companion.setTOPCHAT(SellerAppBaseUrl.CHAT_DOMAIN);

    }

    private void generateSellerAppNetworkKeys() {
        AuthUtil.KEY.KEY_CREDIT_CARD_VAULT = SellerAppNetworkKeys.CREDIT_CARD_VAULT_AUTH_KEY;
        AuthUtil.KEY.ZEUS_WHITELIST = SellerAppNetworkKeys.ZEUS_WHITELIST;
    }

    public void initDbFlow() {
        super.initDbFlow();
        try {
            FlowManager.getConfig();
        } catch (IllegalStateException e) {
            FlowManager.init(new FlowConfig.Builder(getApplicationContext()).build());
        }
        FlowManager.initModule(ProductDraftGeneratedDatabaseHolder.class);
        FlowManager.initModule(TkpdCacheApiGeneratedDatabaseHolder.class);
        PushNotification.initDatabase(getApplicationContext());
    }

    private void initCacheApi() {
        CacheApiLoggingUtils.setLogEnabled(GlobalConfig.isAllowDebuggingTools());
        new CacheApiWhiteListUseCase().executeSync(CacheApiWhiteListUseCase.createParams(
                CacheApiWhiteList.getWhiteList(),
                String.valueOf(getCurrentVersion(getApplicationContext()))));
    }

    @Override
    public Class<?> getDeeplinkClass() {
        return DeepLinkActivity.class;
    }
}