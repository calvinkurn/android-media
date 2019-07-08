package com.tokopedia.sellerapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.URLUtil;

import com.moengage.inapp.InAppManager;
import com.moengage.inapp.InAppMessage;
import com.moengage.inapp.InAppTracker;
import com.moengage.pushbase.push.MoEPushCallBacks;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.ProductDraftGeneratedDatabaseHolder;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.cacheapi.domain.interactor.CacheApiWhiteListUseCase;
import com.tokopedia.cacheapi.util.CacheApiLoggingUtils;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.config.url.TokopediaUrl;
import com.tokopedia.core.analytics.container.AppsflyerAnalytics;
import com.tokopedia.core.analytics.container.GTMAnalytics;
import com.tokopedia.core.analytics.container.MoengageAnalytics;
import com.tokopedia.core.common.category.CategoryDbFlow;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.pushnotif.PushNotification;
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
import com.tokopedia.reputation.common.constant.ReputationCommonUrl;
import com.tokopedia.sellerapp.dashboard.view.activity.DashboardActivity;
import com.tokopedia.sellerapp.deeplink.DeepLinkActivity;
import com.tokopedia.sellerapp.deeplink.DeepLinkHandlerActivity;
import com.tokopedia.sellerapp.utils.CacheApiWhiteList;
import com.tokopedia.track.TrackApp;

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
        if (deepLinkUri != null) {
            CommonUtils.dumper("FCM moengage SELLER clicked " + deepLinkUri.toString());
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
                CommonUtils.dumper("FCM entered no one");
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
        com.tokopedia.config.GlobalConfig.HOME_ACTIVITY_CLASS_NAME = DashboardActivity.class.getName();
        com.tokopedia.config.GlobalConfig.DEEPLINK_HANDLER_ACTIVITY_CLASS_NAME = DeepLinkHandlerActivity.class.getName();
        com.tokopedia.config.GlobalConfig.DEEPLINK_ACTIVITY_CLASS_NAME = DeepLinkActivity.class.getName();
        setVersionCode();
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            com.tokopedia.core.util.GlobalConfig.VERSION_NAME = pInfo.versionName;
            com.tokopedia.config.GlobalConfig.VERSION_NAME = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        TokopediaUrl.Companion.init(this);
        generateSellerAppNetworkKeys();

        TrackApp.initTrackApp(this);

        TrackApp.getInstance().registerImplementation(TrackApp.GTM, GTMAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.APPSFLYER, AppsflyerAnalytics.class);
        TrackApp.getInstance().registerImplementation(TrackApp.MOENGAGE, MoengageAnalytics.class);
        TrackApp.getInstance().initializeAllApis();

        super.onCreate();

        MoEPushCallBacks.getInstance().setOnMoEPushNavigationAction(this);
        InAppManager.getInstance().setInAppListener(this);
        initCacheApi();
        GraphqlClient.init(this);
        NetworkClient.init(this);
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
        CategoryDbFlow.initDatabase(getApplicationContext());
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


    @Override
    public Intent getCreateResCenterActivityIntent(Context context, String orderId) {
        return null;
    }

    @Override
    public Intent getCreateResCenterActivityIntent(Context context, String orderId, int troubleId, int solutionId) {
        return null;
    }

    //@Override
    public Intent getInboxTicketCallingIntent(Context context) {
        return null;
    }

}