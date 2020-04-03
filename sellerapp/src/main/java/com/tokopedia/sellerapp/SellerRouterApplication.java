package com.tokopedia.sellerapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.applink.internal.ApplinkConstInternalPayment;
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds;
import com.tokopedia.broadcast.message.BroadcastMessageInternalRouter;
import com.tokopedia.broadcast.message.common.BroadcastMessageRouter;
import com.tokopedia.broadcast.message.common.constant.BroadcastMessageConstant;
import com.tokopedia.cacheapi.domain.interactor.CacheApiClearAllUseCase;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.contactus.ContactUsModuleRouter;
import com.tokopedia.contactus.createticket.activity.ContactUsActivity;
import com.tokopedia.core.MaintenancePage;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.subscriber.ProfileCompletionSubscriber;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.home.SimpleWebViewWithFilePickerActivity;
import com.tokopedia.core.network.CoreNetworkRouter;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.share.DefaultShare;
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
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.loginregister.login.view.activity.LoginActivity;
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomActivity;
import com.tokopedia.logisticaddaddress.features.manage.ManagePeopleAddressActivity;
import com.tokopedia.logisticaddaddress.features.pinpoint.GeolocationActivity;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.merchantvoucher.MerchantVoucherModuleRouter;
import com.tokopedia.mitratoppers.MitraToppersRouter;
import com.tokopedia.mitratoppers.MitraToppersRouterInternal;
import com.tokopedia.mlp.router.MLPRouter;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.payment.router.IPaymentModuleRouter;
import com.tokopedia.phoneverification.PhoneVerificationRouter;
import com.tokopedia.phoneverification.view.activity.PhoneVerificationActivationActivity;
import com.tokopedia.product.manage.item.common.di.component.DaggerProductComponent;
import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.common.di.module.ProductModule;
import com.tokopedia.product.manage.item.main.base.data.model.ProductPictureViewModel;
import com.tokopedia.product.manage.item.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.product.manage.item.variant.data.model.variantbyprd.ProductVariantViewModel;
import com.tokopedia.product.manage.feature.list.view.activity.ProductManageActivity;
import com.tokopedia.profile.view.activity.ProfileActivity;
import com.tokopedia.profilecompletion.data.factory.ProfileSourceFactory;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.profilecompletion.data.repository.ProfileRepositoryImpl;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.seller.LogisticRouter;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.TkpdSeller;
import com.tokopedia.seller.common.logout.TkpdSellerLogout;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;
import com.tokopedia.seller.product.etalase.utils.EtalaseUtils;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantDashboardActivity;
import com.tokopedia.seller.purchase.detail.activity.OrderDetailActivity;
import com.tokopedia.seller.purchase.detail.activity.OrderHistoryActivity;
import com.tokopedia.seller.reputation.view.fragment.SellerReputationFragment;
import com.tokopedia.seller.shop.common.di.component.DaggerShopComponent;
import com.tokopedia.seller.shop.common.di.component.ShopComponent;
import com.tokopedia.seller.shop.common.di.module.ShopModule;
import com.tokopedia.sellerapp.deeplink.DeepLinkActivity;
import com.tokopedia.sellerapp.deeplink.DeepLinkDelegate;
import com.tokopedia.sellerapp.deeplink.DeepLinkHandlerActivity;
import com.tokopedia.sellerapp.drawer.DrawerSellerHelper;
import com.tokopedia.sellerapp.utils.DeferredResourceInitializer;
import com.tokopedia.sellerapp.utils.FingerprintModelGenerator;
import com.tokopedia.sellerapp.welcome.WelcomeActivity;
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.ShopPageInternalRouter;
import com.tokopedia.talk.inboxtalk.view.activity.InboxTalkActivity;
import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;
import com.tokopedia.tkpd.tkpdreputation.TkpdReputationInternalRouter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationActivity;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.ReviewShopFragment;
import com.tokopedia.topads.TopAdsComponentInstance;
import com.tokopedia.topads.TopAdsManagementRouter;
import com.tokopedia.topads.TopAdsModuleRouter;
import com.tokopedia.topads.common.TopAdsWebViewRouter;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.domain.interactor.GetDepositTopAdsUseCase;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity;
import com.tokopedia.topchat.attachproduct.view.activity.BroadcastMessageAttachProductActivity;
import com.tokopedia.track.TrackApp;
import com.tokopedia.transaction.common.TransactionRouter;
import com.tokopedia.transaction.orders.UnifiedOrderListRouter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.withdraw.WithdrawRouter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Response;
import rx.Observable;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;
import static com.tokopedia.remoteconfig.RemoteConfigKey.APP_ENABLE_SALDO_SPLIT_FOR_SELLER_APP;
import static com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_OLD_PRODUCT_MANAGE;

/**
 * Created by normansyahputa on 12/15/16.
 */

public abstract class SellerRouterApplication extends MainApplication
        implements TkpdCoreRouter, SellerModuleRouter, PdpRouter, GMModuleRouter, TopAdsModuleRouter,
        IPaymentModuleRouter, IDigitalModuleRouter, TkpdInboxRouter, TransactionRouter,
        ReputationRouter, LogisticRouter,
        MitraToppersRouter, AbstractionRouter, ShopModuleRouter,
        ApplinkRouter,
        NetworkRouter, TopAdsWebViewRouter, ContactUsModuleRouter, WithdrawRouter,
        PhoneVerificationRouter,
        TopAdsManagementRouter,
        BroadcastMessageRouter,
        MerchantVoucherModuleRouter,
        UnifiedOrderListRouter,
        CoreNetworkRouter,
        FlashSaleRouter,
        LinkerRouter,
        ResolutionRouter,
        MLPRouter {

    protected RemoteConfig remoteConfig;
    private DaggerProductComponent.Builder daggerProductBuilder;
    private ProductComponent productComponent;
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
        daggerProductBuilder = DaggerProductComponent.builder().productModule(new ProductModule());
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
    public void goToManageProduct(Context context) {
        Intent intent = new Intent(context, ProductManageActivity.class);

        if(remoteConfig.getBoolean(ENABLE_OLD_PRODUCT_MANAGE)) {
            intent = new Intent(context, com.tokopedia.product.manage.oldlist.view.activity.ProductManageActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void clearEtalaseCache() {
        EtalaseUtils.clearEtalaseCache(getApplicationContext());
    }

    @Override
    public void resetAddProductCache(Context context) {
        EtalaseUtils.clearEtalaseCache(context);
        EtalaseUtils.clearDepartementCache(context);
    }

    public Intent getMitraToppersActivityIntent(Context context) {
        return MitraToppersRouterInternal.getMitraToppersActivityIntent(context);
    }

    @Override
    public void actionApplink(Activity activity, String linkUrl) {

    }

    @Override
    public void actionOpenGeneralWebView(Activity activity, String mobileUrl) {

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
    public void getUserInfo(RequestParams empty, ProfileCompletionSubscriber profileSubscriber) {
        Bundle bundle = new Bundle();
        SessionHandler sessionHandler = new SessionHandler(this);
        String authKey = sessionHandler.getAccessToken(this);
        authKey = sessionHandler.getTokenType(this) + " " + authKey;
        bundle.putString(AccountsService.AUTH_KEY, authKey);

        AccountsService accountsService = new AccountsService(bundle);

        ProfileSourceFactory profileSourceFactory =
                new ProfileSourceFactory(
                        this,
                        accountsService,
                        new GetUserInfoMapper(),
                        null,
                        sessionHandler
                );

        GetUserInfoUseCase getUserInfoUseCase = new GetUserInfoUseCase(
                new ProfileRepositoryImpl(profileSourceFactory)
        );

        getUserInfoUseCase.execute(GetUserInfoUseCase.generateParam(), profileSubscriber);
    }

    @Override
    public Intent getInboxReputationIntent(Context context) {
        return TkpdReputationInternalRouter.getInboxReputationActivityIntent(context);
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
    public DrawerHelper getDrawer(AppCompatActivity activity,
                                  SessionHandler sessionHandler,
                                  LocalCacheHandler drawerCache,
                                  GlobalCacheManager globalCacheManager) {
        return DrawerSellerHelper.createInstance(activity, sessionHandler, drawerCache);
    }

    @Override
    public void onLogout(AppComponent appComponent) {
        new CacheApiClearAllUseCase(this).executeSync();

        TkpdSellerLogout.onLogOut(appComponent, this);
    }

    @Override
    public Intent getRegisterIntent(Context context) {
        Intent intent = RegisterInitialActivity.getCallingIntent(context);
        return intent;
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
    public Class getDeepLinkClass() {
        return DeepLinkActivity.class;
    }

    @Override
    public android.app.Fragment getFragmentShopSettings() {
        return TkpdSeller.getFragmentShopSettings();
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
    public void goToGMSubscribe(@NonNull Activity activity) {
    }

    @Override
    public String getFlavor() {
        return BuildConfig.FLAVOR;
    }

    @Override
    public Intent goToOrderDetail(Context context, String orderId) {
        return OrderDetailActivity.createSellerInstance(context, orderId);
    }

    @Override
    public void goToUserPaymentList(Activity activity) {
        RouteManager.route(activity, ApplinkConstInternalPayment.PAYMENT_SETTING);
    }

    @Override
    public ApplinkUnsupported getApplinkUnsupported(Activity activity) {
        return null;
    }

    @Override
    public Intent getInboxMessageIntent(Context context) {
        return RouteManager.getIntent(context, ApplinkConst.TOPCHAT_IDLESS);
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

    public Intent getKolFollowingPageIntent(Context context, int userId) {
        return null;
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

    public UserSession getSession() {
        return new UserSession(this);
    }

    @Override
    public void goToShareShop(Activity activity, String shopId, String shopUrl, String shareLabel) {
        LinkerData shareData = LinkerData.Builder.getLinkerBuilder()
                .setType(LinkerData.SHOP_TYPE)
                .setName(getString(R.string.message_share_shop))
                .setTextContent(shareLabel)
                .setUri(shopUrl)
                .setId(shopId)
                .build();
        new DefaultShare(activity, shareData).show();
    }

    @Override
    public Fragment getReviewFragment(Activity activity, String shopId, String shopDomain) {
        return ReviewShopFragment.createInstance(shopId, shopDomain);
    }

    @Override
    public Intent getShopPageIntent(Context context, String shopId) {
        return ShopPageInternalRouter.getShopPageIntent(context, shopId);
    }

    @Override
    public void startSaldoDepositIntent(Context context) {
        if (remoteConfig.getBoolean(APP_ENABLE_SALDO_SPLIT_FOR_SELLER_APP, false)) {
            RouteManager.route(context, ApplinkConstInternalGlobal.SALDO_DEPOSIT);
        } else {
            Intent intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW, ApplinkConst.WebViewUrl.SALDO_DETAIL);
            context.startActivity(intent);
        }
    }

    @Override
    public Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId) {
        return ShopPageInternalRouter.getShoProductListIntent(context, shopId, keyword, etalaseId);
    }

    public Intent getGroupChatIntent(Context context, String channelUrl) {
        return null;
    }

    @Override
    public void logInvalidGrant(Response response) {
        AnalyticsLog.logInvalidGrant(this, legacyGCMHandler(), legacySessionHandler(), response.request().url().toString());

    }

    @Override
    public Intent getTopProfileIntent(Context context, String userId) {
        return ProfileActivity.Companion.createIntent(context, userId);
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
    public Intent getSellerWebViewIntent(Context context, String webviewUrl) {
        return RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW, webviewUrl);
    }

    @Override
    public Intent getCartIntent(Activity activity) {
        return null;
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
    public Intent getChatBotIntent(Context context, String messageId) {
        return RouteManager.getIntent(context, ApplinkConst.CHATBOT
                .replace(String.format("{%s}", ApplinkConst.Chat.MESSAGE_ID), messageId));
    }

    public Intent createIntentProductVariant(Context context, ArrayList<ProductVariantByCatModel> productVariantByCatModelList,
                                             ProductVariantViewModel productVariant, int productPriceCurrency, double productPrice,
                                             int productStock, boolean officialStore, String productSku,
                                             boolean needRetainImage, ProductPictureViewModel productSizeChart, boolean hasOriginalVariantLevel1,
                                             boolean hasOriginalVariantLevel2, boolean hasWholesale, boolean isAddStatus) {
        return ProductVariantDashboardActivity.getIntent(context, productVariantByCatModelList, productVariant,
                productPriceCurrency, productPrice, productStock, officialStore, productSku, needRetainImage, productSizeChart,
                hasOriginalVariantLevel1, hasOriginalVariantLevel2, hasWholesale,isAddStatus);
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
    public Intent getHelpUsIntent(Context context) {
        return new Intent(context, ContactUsActivity.class);
    }

    @Override
    public Intent getWebviewActivityWithIntent(Context context, String url, String title) {
        return SimpleWebViewWithFilePickerActivity.getIntentWithTitle(context, url, title);
    }

    @Override
    public Intent getWebviewActivityWithIntent(Context context, String url) {
        return SimpleWebViewWithFilePickerActivity.getIntent(context,
                url);
    }

    @Override
    public Intent getInboxTalkCallingIntent(@NonNull Context context) {
        return InboxTalkActivity.Companion.createIntent(context);
    }

    @Override
    public Intent getManageAdressIntent(Context context) {
        return new Intent(context, ManagePeopleAddressActivity.class);
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

    public void onLoginSuccess() {
    }

    public String getDeviceId(Context context) {
        return "";
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
    public Intent getLoginIntent(Context context) {
        return LoginActivity.DeepLinkIntents.getCallingIntent(context);
    }

    @Override
    public void showAppFeedbackRatingDialog(FragmentManager fragmentManager, Context context, BottomSheets.BottomSheetDismissListener listener) {

    }

    @Override
    public void onNewIntent(Context context, Intent intent) {
        //no op
    }
}