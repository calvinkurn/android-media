package com.tokopedia.sellerapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.legacy.AnalyticsLog;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkDelegate;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.ApplinkUnsupported;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds;
import com.tokopedia.broadcast.message.BroadcastMessageInternalRouter;
import com.tokopedia.broadcast.message.common.BroadcastMessageRouter;
import com.tokopedia.broadcast.message.common.constant.BroadcastMessageConstant;
import com.tokopedia.cacheapi.domain.interactor.CacheApiClearAllUseCase;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.MaintenancePage;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.network.CoreNetworkRouter;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.util.AccessTokenRefresh;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.SessionRefresh;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity;
import com.tokopedia.fingerprint.util.FingerprintConstant;
import com.tokopedia.flashsale.management.router.FlashSaleInternalRouter;
import com.tokopedia.flashsale.management.router.FlashSaleRouter;
import com.tokopedia.gm.GMModuleRouter;
import com.tokopedia.gm.common.di.component.DaggerGMComponent;
import com.tokopedia.gm.common.di.component.GMComponent;
import com.tokopedia.gm.common.di.module.GMModule;
import com.tokopedia.inbox.common.ResolutionRouter;
import com.tokopedia.inbox.rescenter.create.activity.CreateResCenterActivity;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.DetailResChatActivity;
import com.tokopedia.inbox.rescenter.inboxv2.view.activity.ResoInboxActivity;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.linker.interfaces.LinkerRouter;
import com.tokopedia.loginregister.login.view.activity.LoginActivity;
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomActivity;
import com.tokopedia.logisticaddaddress.features.pinpoint.GeolocationActivity;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.payment.router.IPaymentModuleRouter;
import com.tokopedia.phoneverification.PhoneVerificationRouter;
import com.tokopedia.phoneverification.view.activity.PhoneVerificationActivationActivity;
import com.tokopedia.product.manage.feature.list.view.fragment.ProductManageSellerFragment;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.seller.LogisticRouter;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.TkpdSeller;
import com.tokopedia.seller.common.logout.TkpdSellerLogout;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;
import com.tokopedia.seller.product.etalase.utils.EtalaseUtils;
import com.tokopedia.seller.purchase.detail.activity.OrderDetailActivity;
import com.tokopedia.seller.purchase.detail.activity.OrderHistoryActivity;
import com.tokopedia.seller.reputation.view.fragment.SellerReputationFragment;
import com.tokopedia.seller.shop.common.di.component.DaggerShopComponent;
import com.tokopedia.seller.shop.common.di.component.ShopComponent;
import com.tokopedia.seller.shop.common.di.module.ShopModule;
import com.tokopedia.sellerapp.deeplink.DeepLinkActivity;
import com.tokopedia.sellerapp.deeplink.DeepLinkDelegate;
import com.tokopedia.sellerapp.deeplink.DeepLinkHandlerActivity;
import com.tokopedia.sellerapp.utils.DeferredResourceInitializer;
import com.tokopedia.sellerapp.utils.FingerprintModelGenerator;
import com.tokopedia.sellerapp.welcome.WelcomeActivity;
import com.tokopedia.sellerhome.SellerHomeRouter;
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity;
import com.tokopedia.sellerorder.common.util.SomConsts;
import com.tokopedia.sellerorder.list.presentation.fragment.SomListFragment;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.talk.inboxtalk.view.activity.InboxTalkActivity;
import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;
import com.tokopedia.tkpd.tkpdreputation.TkpdReputationInternalRouter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationActivity;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.ReviewShopFragment;
import com.tokopedia.topads.TopAdsComponentInstance;
import com.tokopedia.topads.TopAdsManagementRouter;
import com.tokopedia.topads.TopAdsModuleRouter;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.domain.interactor.GetDepositTopAdsUseCase;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity;
import com.tokopedia.topchat.attachproduct.view.activity.BroadcastMessageAttachProductActivity;
import com.tokopedia.topchat.chatlist.fragment.ChatTabListFragment;
import com.tokopedia.track.TrackApp;
import com.tokopedia.transaction.common.TransactionRouter;
import com.tokopedia.transaction.orders.UnifiedOrderListRouter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Response;
import rx.Observable;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;

/**
 * Created by normansyahputa on 12/15/16.
 */

public abstract class SellerRouterApplication extends MainApplication
        implements TkpdCoreRouter, SellerModuleRouter, GMModuleRouter, TopAdsModuleRouter,
        IPaymentModuleRouter, IDigitalModuleRouter, TkpdInboxRouter, TransactionRouter,
        ReputationRouter, LogisticRouter,
        AbstractionRouter,
        ShopModuleRouter,
        ApplinkRouter,
        NetworkRouter,
        PhoneVerificationRouter,
        TopAdsManagementRouter,
        BroadcastMessageRouter,
        UnifiedOrderListRouter,
        CoreNetworkRouter,
        FlashSaleRouter,
        LinkerRouter,
        ResolutionRouter,
        SellerHomeRouter {

    protected RemoteConfig remoteConfig;
    private DaggerGMComponent.Builder daggerGMBuilder;
    private GMComponent gmComponent;
    private TopAdsComponent topAdsComponent;
    private DaggerShopComponent.Builder daggerShopBuilder;
    private ShopComponent shopComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeDagger();
        initializeRemoteConfig();
        initResourceDownloadManager();
        initIris();
    }

    private void initResourceDownloadManager() {
        (new DeferredResourceInitializer()).initializeResourceDownloadManager(context);
        initIris();
    }

    private void initIris() {
        IrisAnalytics.Companion.getInstance(this).initialize();
    }

    private void initializeRemoteConfig() {
        remoteConfig = new FirebaseRemoteConfigImpl(this);
    }

    private void initializeDagger() {
        daggerGMBuilder = DaggerGMComponent.builder().gMModule(new GMModule());
        daggerShopBuilder = DaggerShopComponent.builder().shopModule(new ShopModule());
    }

    public GMComponent getGMComponent() {
        if (gmComponent == null) {
            gmComponent = daggerGMBuilder.appComponent(getApplicationComponent()).build();
        }
        return gmComponent;
    }

    @Override
    public TopAdsComponent getTopAdsComponent() {
        if (topAdsComponent == null) {
            topAdsComponent = TopAdsComponentInstance.getComponent(this);
        }
        return topAdsComponent;
    }

    @Override
    public ShopComponent getShopComponent() {
        if (shopComponent == null) {
            shopComponent = daggerShopBuilder.appComponent(getApplicationComponent()).build();
        }
        return shopComponent;
    }

    @Override
    public void resetAddProductCache(Context context) {
        EtalaseUtils.clearEtalaseCache(context);
        EtalaseUtils.clearDepartementCache(context);
    }

    @Override
    public void goToCreateMerchantRedirect(Context context) {
        //no route to merchant redirect on seller, go to default
        goToDefaultRoute(context);
    }

    /**
     * User PersistentCacheManager Library directly
     */
    @Deprecated
    @Override
    public CacheManager getGlobalCacheManager() {
        return new GlobalCacheManager();
    }

    @Override
    public NotificationPass setNotificationPass(Context mContext, NotificationPass mNotificationPass, Bundle data, String notifTitle) {
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(getInboxReputationIntent(this));
        mNotificationPass.classParentStack = InboxReputationActivity.class;
        mNotificationPass.title = notifTitle;
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        return mNotificationPass;
    }

    private Intent getInboxReputationIntent(Context context) {
        return TkpdReputationInternalRouter.getInboxReputationActivityIntent(context);
    }

    @Override
    public Fragment getReputationHistoryFragment() {
        return SellerReputationFragment.createInstance();
    }

    @Override
    public Intent getHomeIntent(Context context) {
        Intent intent = new Intent(context, WelcomeActivity.class);
        if (SessionHandler.isV4Login(context)) {
            if (SessionHandler.isUserHasShop(context)) {
                return SellerHomeActivity.createIntent(context);
            } else {
                return intent;
            }
        } else {
            return intent;
        }
    }

    @Override
    public Intent getPhoneVerificationActivityIntent(Context context) {
        return PhoneVerificationActivationActivity.getIntent(context, true, false);
    }

    @Override
    public Class<?> getHomeClass(Context context) throws ClassNotFoundException {
        if (SessionHandler.isV4Login(context)) {
            return SellerHomeActivity.class;
        } else {
            return WelcomeActivity.class;
        }
    }

    @Override
    public void onLogout(AppComponent appComponent) {
        SessionHandler sessionHandler = new SessionHandler(this);
        sessionHandler.forceLogout();
        new CacheApiClearAllUseCase(this).executeSync();
        TkpdSellerLogout.onLogOut(appComponent, this);
    }

    @Override
    public Intent getRegisterIntent(Context context) {
        return RegisterInitialActivity.getCallingIntent(context);
    }

    private void goToDefaultRoute(Context context) {
        Intent intent = SellerHomeActivity.createIntent(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public boolean isSupportedDelegateDeepLink(String appLinks) {
        DeepLinkDelegate deepLinkDelegate = DeepLinkHandlerActivity.getDelegateInstance();
        return deepLinkDelegate.supportsUri(appLinks);
    }

    @Override
    public void actionNavigateByApplinksUrl(Activity activity, String applinks, Bundle bundle) {
        DeepLinkDelegate deepLinkDelegate = DeepLinkHandlerActivity.getDelegateInstance();
        Intent intent = activity.getIntent();
        intent.putExtras(bundle);
        intent.setData(Uri.parse(applinks));
        deepLinkDelegate.dispatchFrom(activity, intent);
    }

    @Override
    public String getBaseUrlDomainPayment() {
        return SellerAppBaseUrl.BASE_PAYMENT_URL_DOMAIN;
    }

    @Override
    public String getGeneratedOverrideRedirectUrlPayment(String originUrl) {
        Uri originUri = Uri.parse(originUrl);
        Uri.Builder uriBuilder = Uri.parse(originUrl).buildUpon();
        if (!TextUtils.isEmpty(originUri.getQueryParameter(AuthUtil.WEBVIEW_FLAG_PARAM_FLAG_APP))) {
            uriBuilder.appendQueryParameter(
                    AuthUtil.WEBVIEW_FLAG_PARAM_FLAG_APP,
                    AuthUtil.DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_FLAG_APP
            );
        }
        if (!TextUtils.isEmpty(originUri.getQueryParameter(AuthUtil.WEBVIEW_FLAG_PARAM_DEVICE))) {
            uriBuilder.appendQueryParameter(
                    AuthUtil.WEBVIEW_FLAG_PARAM_DEVICE,
                    AuthUtil.DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_DEVICE
            );
        }
        if (!TextUtils.isEmpty(originUri.getQueryParameter(AuthUtil.WEBVIEW_FLAG_PARAM_UTM_SOURCE))) {
            uriBuilder.appendQueryParameter(
                    AuthUtil.WEBVIEW_FLAG_PARAM_UTM_SOURCE,
                    AuthUtil.DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_UTM_SOURCE
            );
        }
        if (!TextUtils.isEmpty(originUri.getQueryParameter(AuthUtil.WEBVIEW_FLAG_PARAM_APP_VERSION))) {
            uriBuilder.appendQueryParameter(
                    AuthUtil.WEBVIEW_FLAG_PARAM_APP_VERSION, GlobalConfig.VERSION_NAME
            );
        }
        return uriBuilder.build().toString().trim();
    }

    @Override
    public Map<String, String> getGeneratedOverrideRedirectHeaderUrlPayment(String originUrl) {
        String urlQuery = Uri.parse(originUrl).getQuery();
        return AuthUtil.generateWebviewHeaders(
                Uri.parse(originUrl).getPath(),
                urlQuery != null ? urlQuery : "",
                "GET",
                AuthUtil.KEY.KEY_WSV4);
    }

    @Override
    public boolean getEnableFingerprintPayment() {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(this);
        return remoteConfig.getBoolean(FingerprintConstant.ENABLE_FINGERPRINT_SELLERAPP);
    }

    @Override
    public Intent getAskBuyerIntent(Context context, String toUserId, String customerName,
                                    String customSubject, String customMessage, String source,
                                    String avatar) {
        return RouteManager.getIntent(context,
                ApplinkConst.TOPCHAT_ASKBUYER,
                toUserId,
                customMessage,
                source,
                customerName,
                avatar);
    }


    @NonNull
    @Override
    public Intent getAskSellerIntent(@NonNull Context context, @NonNull String toShopId,
                                     @NonNull String shopName, @NonNull String customSubject,
                                     @NonNull String customMessage, @NonNull String source,
                                     @NonNull String avatar) {

        return RouteManager.getIntent(context,
                ApplinkConst.TOPCHAT_ASKSELLER,
                toShopId,
                customMessage,
                source,
                shopName,
                avatar);
    }

    @Override
    public Observable<DataDeposit> getDataDeposit(String shopId) {
        GetDepositTopAdsUseCase getDepositTopAdsUseCase = getTopAdsComponent().getDepositTopAdsUseCase();
        return getDepositTopAdsUseCase.getExecuteObservable(GetDepositTopAdsUseCase.createRequestParams(shopId));
    }

    @NonNull
    @Override
    public Intent getSplashScreenIntent(@NonNull Context context) {
        return new Intent(context, SplashScreenActivity.class);
    }

    @Override
    public Class getSellingActivityClass() {
        return TkpdSeller.getSellingActivityClass();
    }

    @Override
    public Intent getActivitySellingTransactionNewOrder(Context context) {
        return TkpdSeller.getActivitySellingTransactionNewOrder(context);
    }

    @Override
    public Intent getActivitySellingTransactionConfirmShipping(Context context) {
        return TkpdSeller.getActivitySellingTransactionConfirmShipping(context);
    }

    @Override
    public Intent getActivitySellingTransactionShippingStatus(Context context) {
        return TkpdSeller.getActivitySellingTransactionShippingStatus(context);
    }

    @Override
    public Intent getActivitySellingTransactionList(Context context) {
        return TkpdSeller.getActivitySellingTransactionList(context);
    }

    @Override
    public Intent getActivitySellingTransactionOpportunity(Context context, String query) {
        return TkpdSeller.getActivitySellingTransactionOpportunity(context, query);
    }

    @Override
    public void goToTopAdsDashboard(Activity activity) {
        Intent intent = new Intent(activity, TopAdsDashboardActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public Intent goToOrderDetail(Context context, String orderId) {
        return OrderDetailActivity.createSellerInstance(context, orderId);
    }

    @Override
    public ApplinkUnsupported getApplinkUnsupported(Activity activity) {
        return null;
    }

    @Override
    public Intent getResolutionCenterIntentSeller(Context context) {
        return ResoInboxActivity.newSellerInstance(context);
    }

    @Override
    public Intent getDetailResChatIntentBuyer(Context context, String resoId, String shopName) {
        return DetailResChatActivity.newBuyerInstance(context, resoId, shopName);
    }

    @Override
    public Intent getPhoneVerificationActivationIntent(Context context) {
        return PhoneVerificationActivationActivity.getCallingIntent(context);
    }

    @Override
    public Intent getLoginGoogleIntent(Context context) {
        return LoginActivity.DeepLinkIntents.getAutoLoginGoogle(context);
    }

    @Override
    public Intent getLoginFacebookIntent(Context context) {
        return LoginActivity.DeepLinkIntents.getAutoLoginFacebook(context);

    }

    @Override
    public Intent getLoginWebviewIntent(Context context, String name, String url) {
        return LoginActivity.DeepLinkIntents.getAutoLoginWebview(context, name, url);
    }

    @Override
    public Intent getDistrictRecommendationIntent(Activity activity, Token token) {
        return DiscomActivity.newInstance(activity, token);
    }

    @Override
    public void onForceLogout(Activity activity) {
        SessionHandler sessionHandler = new SessionHandler(activity);
        sessionHandler.forceLogout();
        Intent intent = SellerRouter.getActivitySplashScreenActivity(getBaseContext());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void showTimezoneErrorSnackbar() {
        ServerErrorHandler.showTimezoneErrorSnackbar();
    }

    @Override
    public void showMaintenancePage() {
        ServerErrorHandler.showMaintenancePage();
    }

    @Override
    public void showForceLogoutTokenDialog(String response) {
        ServerErrorHandler.showForceLogoutDialog();
        ServerErrorHandler.sendForceLogoutTokenAnalytics(response);
    }

    @Override
    public void showServerError(Response response) {
        ServerErrorHandler.sendErrorNetworkAnalytics(response.request().url().toString(), response.code());
    }

    @Override
    public void gcmUpdate() throws IOException {
        AccessTokenRefresh accessTokenRefresh = new AccessTokenRefresh();
        SessionRefresh sessionRefresh = new SessionRefresh(accessTokenRefresh.refreshToken());
        sessionRefresh.gcmUpdate();
    }

    @Override
    public void refreshToken() throws IOException {
        AccessTokenRefresh accessTokenRefresh = new AccessTokenRefresh();
        accessTokenRefresh.refreshToken();
    }

    @Override
    public Fragment getReviewFragment(Activity activity, String shopId, String shopDomain) {
        return ReviewShopFragment.createInstance(shopId, shopDomain);
    }

    @Override
    public void logInvalidGrant(Response response) {
        AnalyticsLog.logInvalidGrant(this, legacyGCMHandler(), legacySessionHandler(), response.request().url().toString());

    }

    @Override
    public Interceptor getChuckerInterceptor() {
        return getAppComponent().ChuckerInterceptor();
    }

    @Override
    public void goToApplinkActivity(Context context, String applink) {
        DeepLinkDelegate deepLinkDelegate = DeepLinkHandlerActivity.getDelegateInstance();
        Intent intent = new Intent(context, DeepLinkHandlerActivity.class);
        intent.setData(Uri.parse(applink));

        if (context instanceof Activity) {
            deepLinkDelegate.dispatchFrom((Activity) context, intent);
        } else {
            context.startActivity(intent);
        }
    }

    @Override
    public Intent getApplinkIntent(Context context, String applink) {
        Intent intent = new Intent(context, DeepLinkHandlerActivity.class);
        intent.setData(Uri.parse(applink));

        return intent;
    }

    @Override
    public FingerprintModel getFingerprintModel() {
        return FingerprintModelGenerator.generateFingerprintModel(this);
    }

    @Override
    public void doRelogin(String newAccessToken) {
        SessionRefresh sessionRefresh = new SessionRefresh(newAccessToken);
        try {
            sessionRefresh.gcmUpdate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void goToApplinkActivity(Activity activity, String applink, Bundle bundle) {
        if (activity != null) {
            DeepLinkDelegate deepLinkDelegate = DeepLinkHandlerActivity.getDelegateInstance();
            Intent intent = activity.getIntent();
            intent.setData(Uri.parse(applink));
            intent.putExtras(bundle);
            deepLinkDelegate.dispatchFrom(activity, intent);
        }
    }

    @Override
    public boolean isSupportApplink(String appLink) {
        DeepLinkDelegate deepLinkDelegate = DeepLinkHandlerActivity.getDelegateInstance();
        return deepLinkDelegate.supportsUri(appLink);
    }

    @Override
    public ApplinkDelegate applinkDelegate() {
        return null;
    }

    @Override
    public @NonNull
    Intent getFlashSaleDashboardIntent(@NonNull Context context) {
        return FlashSaleInternalRouter.getFlashSaleDashboardActivity(context);
    }

    @NonNull
    @Override
    public Intent getBroadcastMessageListIntent(@NonNull Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(BroadcastMessageConstant.VALUE_GTM_EVENT_NAME_INBOX,
                BroadcastMessageConstant.VALUE_GTM_EVENT_CATEGORY,
                BroadcastMessageConstant.VALUE_GTM_EVENT_ACTION_BM_CLICK, "");
        return BroadcastMessageInternalRouter.INSTANCE.getBroadcastMessageListIntent(context);
    }

    @NonNull
    @Override
    public Intent getBroadcastMessageAttachProductIntent(@NonNull Context context, @NonNull String shopId,
                                                         @NonNull String shopName, boolean isSeller,
                                                         @NonNull List<Integer> selectedIds,
                                                         @NonNull ArrayList<HashMap<String, String>> hashProducts) {
        return BroadcastMessageAttachProductActivity.createInstance(context, shopId, shopName, isSeller, selectedIds, hashProducts);
    }

    @Override
    public Fragment getFlightOrderListFragment() {
        return null;
    }

    @Override
    public Intent getInboxTalkCallingIntent(@NonNull Context context) {
        return InboxTalkActivity.Companion.createIntent(context);
    }

    @Override
    public void onAppsFlyerInit() {

    }

    @Override
    public void showSimpleAppRatingDialog(Activity activity) {

    }

    @Override
    public boolean isAllowLogOnChuckInterceptorNotification() {
        LocalCacheHandler cache = new LocalCacheHandler(this, DeveloperOptionActivity.CHUCK_ENABLED);
        return cache.getBoolean(DeveloperOptionActivity.IS_CHUCK_ENABLED, false);
    }

    @Override
    public Intent getGeoLocationActivityIntent(Context context, com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass locationPass) {
        return GeolocationActivity.createInstance(context, locationPass, false);
    }

    @Override
    @NonNull
    public Intent getTopAdsDashboardIntent(@NonNull Context context) {
        return RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL);
    }

    @Override
    @NonNull
    public Intent getTopAdsAddCreditIntent(@NonNull Context context) {
        return RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_BUY_CREDIT);
    }

    @Override
    public boolean getBooleanRemoteConfig(String key, boolean defaultValue) {
        return remoteConfig.getBoolean(key, defaultValue);
    }

    @Override
    public Class getDeeplinkClass() {
        return DeepLinkActivity.class;
    }

    @Override
    public Intent getMaintenancePageIntent() {
        return MaintenancePage.createIntentFromNetwork(getAppContext());
    }

    @Override
    public void refreshFCMTokenFromBackgroundToCM(String token, boolean force) {

    }

    @Override
    public void refreshFCMFromInstantIdService(String token) {

    }

    @Override
    public void refreshFCMTokenFromForegroundToCM() {

    }

    @Override
    public Intent getCreateResCenterActivityIntent(Context context, String orderId) {
        return CreateResCenterActivity.getCreateResCenterActivityIntent(context, orderId);
    }

    @Override
    public void sendForceLogoutAnalytics(Response response, boolean isInvalidToken,
                                         boolean isRequestDenied) {
        ServerErrorHandler.sendForceLogoutAnalytics(response.request().url().toString(), isInvalidToken, isRequestDenied);
    }

    @Override
    public boolean isToggleBuyAgainOn() {
        return remoteConfig.getBoolean(RemoteConfigKey.MAIN_APP_ENABLE_BUY_AGAIN, false);
    }

    @Override
    public Intent getOrderHistoryIntent(Context context, String orderId) {
        return OrderHistoryActivity.createInstance(context, orderId, 1);
    }

    @Override
    public void showAppFeedbackRatingDialog(FragmentManager fragmentManager, Context context, BottomSheets.BottomSheetDismissListener listener) {

    }

    @Override
    public void onNewIntent(Context context, Intent intent) {
        //no op
    }

    @NotNull
    @Override
    public Fragment getSomListFragment(String tabPage) {
        Bundle bundle = new Bundle();
        tabPage = (null == tabPage || "".equals(tabPage)) ? SomConsts.STATUS_ALL_ORDER : tabPage;
        bundle.putString(SomConsts.TAB_ACTIVE, tabPage);
        return SomListFragment.newInstance(bundle);
    }

    @NotNull
    @Override
    public Fragment getProductManageFragment(@NotNull ArrayList<String> filterOptions) {
        return ProductManageSellerFragment.newInstance(filterOptions);
    }

    @NotNull
    @Override
    public Fragment getChatListFragment() {
        return ChatTabListFragment.create();
    }

    @Override
    public void sendAnalyticsAnomalyResponse(String title,
                                             String accessToken, String refreshToken,
                                             String response, String request) {
    }
}