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
import com.raizlabs.android.dbflow.config.TkpdGMGeneratedDatabaseHolder;
import com.raizlabs.android.dbflow.config.TkpdSellerGeneratedDatabaseHolder;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.HockeyAppHelper;
import com.tokopedia.sellerapp.deeplink.DeepLinkActivity;
import com.tokopedia.sellerapp.deeplink.DeepLinkHandlerActivity;
import com.tokopedia.sellerapp.utils.WhitelistUtils;

import java.util.List;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class SellerMainApplication extends SellerRouterApplication implements MoEPushCallBacks.OnMoEPushNavigationAction, InAppManager.InAppMessageListener {

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

            } else if (deepLinkUri.getScheme().equals(Constants.Schemes.APPLINKS)) {
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
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION;
        GlobalConfig.PACKAGE_APPLICATION = GlobalConfig.PACKAGE_SELLER_APP;
        GlobalConfig.DEBUG = BuildConfig.DEBUG;
        GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        initializeDatabase();
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            com.tokopedia.core.util.GlobalConfig.VERSION_NAME = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        generateSellerAppBaseUrl();
        generateSellerAppNetworkKeys();
        super.onCreate();

        MoEPushCallBacks.getInstance().setOnMoEPushNavigationAction(this);
        InAppManager.getInstance().setInAppListener(this);
    }

    private void generateSellerAppBaseUrl() {
        TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL = SellerAppBaseUrl.BASE_TOKOPEDIA_WEBSITE;
        TkpdBaseURL.BASE_DOMAIN = SellerAppBaseUrl.BASE_DOMAIN;
        TkpdBaseURL.ACE_DOMAIN = SellerAppBaseUrl.BASE_ACE_DOMAIN;
        TkpdBaseURL.CLOVER_DOMAIN = SellerAppBaseUrl.BASE_CLOVER_DOMAIN;
        TkpdBaseURL.TOPADS_DOMAIN = SellerAppBaseUrl.BASE_TOPADS_DOMAIN;
        TkpdBaseURL.MOJITO_DOMAIN = SellerAppBaseUrl.BASE_MOJITO_DOMAIN;
        TkpdBaseURL.HADES_DOMAIN = SellerAppBaseUrl.BASE_HADES_DOMAIN;
        TkpdBaseURL.RECHARGE_API_DOMAIN = SellerAppBaseUrl.BASE_RECHARGE_API_DOMAIN;
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
    }

    private void generateSellerAppNetworkKeys() {
        AuthUtil.KEY.KEY_CREDIT_CARD_VAULT = SellerAppNetworkKeys.CREDIT_CARD_VAULT_AUTH_KEY;
        AuthUtil.KEY.ZEUS_WHITELIST = SellerAppNetworkKeys.ZEUS_WHITELIST;
    }

    public void initializeDatabase() {
        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseHolder(TkpdSellerGeneratedDatabaseHolder.class)
                .build());
        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseHolder(TkpdGMGeneratedDatabaseHolder.class)
                .build());
    }

    @Override
    protected List<CacheApiWhiteListDomain> getWhiteList() {
        return WhitelistUtils.getWhiteList();
    }

    @Override
    public Intent getAskSellerIntent(Context context, String toShopId, String shopName, String customSubject, String customMessage, String source, String avatarUrl) {
        return null;
    }
}
