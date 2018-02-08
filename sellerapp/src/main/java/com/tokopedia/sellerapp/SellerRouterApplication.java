package com.tokopedia.sellerapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.SessionRouter;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.cache.domain.interactor.CacheApiClearAllUseCase;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.subscriber.ProfileCompletionSubscriber;
import com.tokopedia.core.gcm.ApplinkUnsupported;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Token;
import com.tokopedia.core.manage.general.districtrecommendation.view.DistrictRecommendationActivity;
import com.tokopedia.core.manage.people.address.activity.ChooseAddressActivity;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.CustomerRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.AccessTokenRefresh;
import com.tokopedia.core.util.SessionRefresh;
import com.tokopedia.mitratoppers.MitraToppersRouter;
import com.tokopedia.mitratoppers.MitraToppersRouterInternal;
import com.tokopedia.digital.receiver.TokocashPendingDataBroadcastReceiver;
import com.tokopedia.seller.LogisticRouter;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.inbox.inboxchat.activity.ChatRoomActivity;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.DetailResChatActivity;
import com.tokopedia.inbox.rescenter.inbox.activity.InboxResCenterActivity;
import com.tokopedia.seller.product.manage.di.ProductManageComponent;
import com.tokopedia.seller.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.sellerapp.onboarding.activity.OnboardingSellerActivity;
import com.tokopedia.sellerapp.truecaller.TruecallerActivity;
import com.tokopedia.session.changephonenumber.view.activity.ChangePhoneNumberWarningActivity;
import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;
import com.tokopedia.core.router.transactionmodule.TransactionRouter;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.welcome.WelcomeActivity;
import com.tokopedia.digital.cart.activity.CartDigitalActivity;
import com.tokopedia.digital.product.activity.DigitalProductActivity;
import com.tokopedia.digital.product.activity.DigitalWebActivity;
import com.tokopedia.digital.widget.activity.DigitalCategoryListActivity;
import com.tokopedia.gm.GMModuleRouter;
import com.tokopedia.gm.cashback.domain.GetCashbackUseCase;
import com.tokopedia.gm.cashback.domain.SetCashbackUseCase;
import com.tokopedia.gm.common.di.component.DaggerGMComponent;
import com.tokopedia.gm.common.di.component.GMComponent;
import com.tokopedia.gm.common.di.module.GMModule;
import com.tokopedia.gm.common.logout.GMLogout;
import com.tokopedia.gm.featured.domain.interactor.GMFeaturedProductGetListUseCase;
import com.tokopedia.gm.subscribe.view.activity.GmSubscribeHomeActivity;
import com.tokopedia.inbox.inboxchat.activity.InboxChatActivity;
import com.tokopedia.inbox.inboxchat.activity.TimeMachineActivity;
import com.tokopedia.inbox.inboxmessageold.activity.InboxMessageActivity;
import com.tokopedia.inbox.inboxmessageold.activity.SendMessageActivityOld;
import com.tokopedia.otp.phoneverification.view.activity.PhoneVerificationActivationActivity;
import com.tokopedia.payment.router.IPaymentModuleRouter;
import com.tokopedia.profilecompletion.data.factory.ProfileSourceFactory;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.profilecompletion.data.repository.ProfileRepositoryImpl;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.TkpdSeller;
import com.tokopedia.seller.common.cashback.DataCashbackModel;
import com.tokopedia.seller.common.featuredproduct.GMFeaturedProductDomainModel;
import com.tokopedia.seller.common.logout.TkpdSellerLogout;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;
import com.tokopedia.seller.instoped.InstopedActivity;
import com.tokopedia.seller.instoped.presenter.InstagramMediaPresenterImpl;
import com.tokopedia.seller.product.common.di.component.DaggerProductComponent;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.common.di.module.ProductModule;
import com.tokopedia.seller.product.draft.view.activity.ProductDraftListActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductAddActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductEditActivity;
import com.tokopedia.seller.product.etalase.utils.EtalaseUtils;
import com.tokopedia.seller.product.manage.view.activity.ProductManageActivity;
import com.tokopedia.seller.reputation.view.fragment.SellerReputationFragment;
import com.tokopedia.seller.shop.common.di.component.DaggerShopComponent;
import com.tokopedia.seller.shop.common.di.component.ShopComponent;
import com.tokopedia.seller.shop.common.di.module.ShopModule;
import com.tokopedia.sellerapp.dashboard.view.activity.DashboardActivity;
import com.tokopedia.sellerapp.deeplink.DeepLinkActivity;
import com.tokopedia.sellerapp.deeplink.DeepLinkDelegate;
import com.tokopedia.sellerapp.deeplink.DeepLinkHandlerActivity;
import com.tokopedia.sellerapp.drawer.DrawerSellerHelper;
import com.tokopedia.session.forgotpassword.activity.ForgotPasswordActivity;
import com.tokopedia.session.session.activity.Login;
import com.tokopedia.tkpdpdp.PreviewProductImageDetail;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationActivity;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.activity.ReputationProduct;
import com.tokopedia.tkpd.tkpdreputation.shopreputation.ShopReputationList;
import com.tokopedia.tkpdpdp.ProductInfoActivity;
import com.tokopedia.topads.TopAdsModuleRouter;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsComponent;
import com.tokopedia.seller.product.manage.di.DaggerProductManageComponent;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsModule;
import com.tokopedia.topads.dashboard.domain.interactor.GetDepositTopAdsUseCase;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity;
import com.tokopedia.transaction.bcaoneklik.activity.ListPaymentTypeActivity;
import com.tokopedia.transaction.purchase.detail.activity.OrderHistoryActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Response;
import rx.Observable;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;
import static com.tokopedia.core.router.productdetail.ProductDetailRouter.ARG_FROM_DEEPLINK;
import static com.tokopedia.core.router.productdetail.ProductDetailRouter.ARG_PARAM_PRODUCT_PASS_DATA;

/**
 * Created by normansyahputa on 12/15/16.
 */

public abstract class SellerRouterApplication extends MainApplication
        implements TkpdCoreRouter, SellerModuleRouter, PdpRouter, GMModuleRouter, TopAdsModuleRouter,
        IPaymentModuleRouter, IDigitalModuleRouter, TkpdInboxRouter, TransactionRouter,
        ReputationRouter, LogisticRouter, SessionRouter,
        MitraToppersRouter, AbstractionRouter{

    private DaggerProductComponent.Builder daggerProductBuilder;
    private ProductComponent productComponent;

    private DaggerGMComponent.Builder daggerGMBuilder;
    private GMComponent gmComponent;

    private DaggerTopAdsComponent.Builder daggerTopAdsBuilder;
    private TopAdsComponent topAdsComponent;

    private DaggerShopComponent.Builder daggerShopBuilder;
    private ShopComponent shopComponent;

    protected RemoteConfig remoteConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeDagger();
        initializeRemoteConfig();
    }

    private void initializeRemoteConfig() {
        remoteConfig = new FirebaseRemoteConfigImpl(this);
    }

    private void initializeDagger() {
        daggerGMBuilder = DaggerGMComponent.builder().gMModule(new GMModule());
        daggerProductBuilder = DaggerProductComponent.builder().productModule(new ProductModule());
        daggerTopAdsBuilder = DaggerTopAdsComponent.builder().topAdsModule(new TopAdsModule());
        daggerShopBuilder = DaggerShopComponent.builder().shopModule(new ShopModule());
    }

    @Override
    public ProductComponent getProductComponent() {
        if (productComponent == null) {
            productComponent = daggerProductBuilder.appComponent(getApplicationComponent()).build();
        }
        return productComponent;
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
            topAdsComponent = daggerTopAdsBuilder.appComponent(getApplicationComponent()).build();
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
    public void startInstopedActivityForResult(Activity activity, int resultCode, int maxResult) {
        InstopedActivity.startInstopedActivityForResult(activity, resultCode, maxResult);
    }

    @Override
    public void startInstopedActivityForResult(Context context, Fragment fragment, int resultCode, int maxResult) {
        InstopedActivity.startInstopedActivityForResult(context, fragment, resultCode, maxResult);
    }

    @Override
    public void removeInstopedToken() {
        InstagramMediaPresenterImpl.removeToken();
    }

    @Override
    public void goToManageProduct(Context context) {
        Intent intent = new Intent(context, ProductManageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void goToDraftProductList(Context context) {
        Intent intent = new Intent(context, ProductDraftListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void clearEtalaseCache() {
        EtalaseUtils.clearEtalaseCache(getApplicationContext());
    }

    @Override
    public Intent goToEditProduct(Context context, boolean isEdit, String productId) {
        return ProductEditActivity.createInstance(context, productId);
    }

    @Override
    public void resetAddProductCache(Context context) {
        EtalaseUtils.clearEtalaseCache(context);
        EtalaseUtils.clearDepartementCache(context);
    }

    @Override
    public void goToWallet(Context context, String url) {
        //no route to wallet on seller, go to default
        goToDefaultRoute(context);
    }

    @Override
    public void goToMerchantRedirect(Context context) {
        Intent intent = GmSubscribeHomeActivity.getCallingIntent(context);
        context.startActivity(intent);
    }

    public Intent getMitraToppersActivityIntent(Context context){
        return MitraToppersRouterInternal.getMitraToppersActivityIntent(context);
    }

    @Override
    public void actionAppLink(Context context, String linkUrl) {

    }

    @Override
    public void actionApplink(Activity activity, String linkUrl) {

    }

    @Override
    public void actionApplinkFromActivity(Activity activity, String linkUrl) {

    }

    @Override
    public void actionApplink(Activity activity, String linkUrl, String extra) {

    }

    @Override
    public void actionOpenGeneralWebView(Activity activity, String mobileUrl) {

    }

    @Override
    public void goToCreateMerchantRedirect(Context context) {
        //no route to merchant redirect on seller, go to default
        goToDefaultRoute(context);
    }


    @Override
    public void onForceLogout(Activity activity) {
        SessionHandler sessionHandler = new SessionHandler(activity);
        sessionHandler.forceLogout();
        if (GlobalConfig.isSellerApp()) {
            Intent intent = SellerRouter.getActivitySplashScreenActivity(getBaseContext());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            invalidateCategoryMenuData();
            Intent intent = CustomerRouter.getSplashScreenIntent(getBaseContext());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
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
    public void showForceLogoutDialog() {
        ServerErrorHandler.showMaintenancePage();
    }

    @Override
    public void showServerError(Response response) {
        ServerErrorHandler.showServerErrorSnackbar();
        ServerErrorHandler.sendErrorNetworkAnalytics(response.request().url().toString(), response.code());
    }

    @Override
    public void refreshLogin() {
        AccessTokenRefresh accessTokenRefresh = new AccessTokenRefresh();
        try {
            SessionRefresh sessionRefresh = new SessionRefresh(accessTokenRefresh.refreshToken());
            sessionRefresh.refreshLogin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshToken() {
        AccessTokenRefresh accessTokenRefresh = new AccessTokenRefresh();
        try {
            accessTokenRefresh.refreshToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AnalyticTracker getAnalyticTracker() {
        return new AnalyticTracker() {
            @Override
            public void sendEventTracking(Map<String, Object> events) {

            }

            @Override
            public void sendEventTracking(String event, String category, String action, String label) {

            }

            @Override
            public void sendScreen(Activity activity, String screenName) {

            }
        };
    }

    @Override
    public UserSession getSession() {
        return new UserSessionImpl(this);
    }

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
                new JobExecutor(),
                new UIThread(),
                new ProfileRepositoryImpl(profileSourceFactory)
        );

        getUserInfoUseCase.execute(GetUserInfoUseCase.generateParam(), profileSubscriber);
    }

    @Override
    public Intent getHomeHotlistIntent(Context context) {
        return null;
    }

    @Override
    public Intent getInboxReputationIntent(Context context) {
        return InboxReputationActivity.getCallingIntent(context);
    }

    @Override
    public NotificationPass setNotificationPass(Context mContext, NotificationPass mNotificationPass, Bundle data, String notifTitle) {
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(
                ((ReputationRouter) MainApplication.getAppContext())
                        .getInboxReputationIntent(MainApplication.getAppContext())
        );
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
    public android.app.Fragment getShopReputationFragment() {
        return ShopReputationList.create();
    }

    @Override
    public Intent getProductReputationIntent(Context context) {
        return new Intent(context, ReputationProduct.class);
    }

    @Override
    public Intent getHomeIntent(Context context) {
        Intent intent = new Intent(context, WelcomeActivity.class);
        if (SessionHandler.isV4Login(context)) {
            if (SessionHandler.isUserHasShop(context)) {
                return DashboardActivity.createInstance(context);
            } else {
                return intent;
            }
        } else {
            return intent;
        }
    }

    @Override
    public Intent getOnBoardingActivityIntent(Context context) {
        return new Intent(context, OnboardingSellerActivity.class);
    }

    @Override
    public Intent getTrueCallerActivityIntent(Context context) {
        return new Intent(context, TruecallerActivity.class);
    }

    @Override
    public Intent getPhoneVerificationActivityIntent(Context context) {
        return PhoneVerificationActivationActivity.getIntent(context, true, false);
    }

    @Override
    public Class<?> getHomeClass(Context context) throws ClassNotFoundException {
        if (SessionHandler.isV4Login(context)) {
            return DashboardActivity.class;
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
        CacheApiClearAllUseCase cacheApiClearAllUseCase = appComponent.cacheApiClearAllUseCase();
        cacheApiClearAllUseCase.getExecuteObservable(RequestParams.EMPTY).toBlocking().first();

        TkpdSellerLogout.onLogOut(appComponent);
        GMLogout.onLogOut(appComponent);
    }

    @Override
    public void goToRegister(Context context) {
        Intent intent = Login.getSellerRegisterIntent(context);
        context.startActivity(intent);
    }

    @Override
    public Intent getLoginIntent(Context context) {
        Intent intent = Login.getCallingIntent(context);
        return intent;
    }

    @Override
    public Intent getRegisterIntent(Context context) {
        Intent intent = Login.getSellerRegisterIntent(context);
        return intent;
    }

    @Override
    public void goToHome(Context context) {
        Intent intent = getHomeIntent(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void gotToProductDetail(Context context) {
        Intent intent = ProductInfoActivity.createInstance(context);
        context.startActivity(intent);
    }

    @Override
    public void goToProductDetail(Context context, String productUrl) {
        DeepLinkChecker.openProduct(productUrl, context);
    }

    @Override
    public void goMultipleInstagramAddProduct(Context context, ArrayList<InstagramMediaModel> instagramMediaModelList) {
        ProductDraftListActivity.startInstagramSaveBulk(context, instagramMediaModelList);
    }

    @Override
    public void goToProductDetail(Context context, ProductPass productPass) {
        Intent intent = ProductInfoActivity.createInstance(context, productPass);
        context.startActivity(intent);
    }

    @Override
    public void goToProductDetail(Context context, ShareData shareData) {
        Intent intent = ProductInfoActivity.createInstance(context, shareData);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProductInfoActivity.SHARE_DATA, shareData);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void goToAddProductDetail(Context context) {
        Intent intent = ProductInfoActivity.createInstance(context);
        Bundle bundle = new Bundle();
        bundle.putBoolean(ProductInfoActivity.IS_ADDING_PRODUCT, true);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public Fragment getProductDetailInstanceDeeplink(
            Context context, @NonNull ProductPass productPass) {

        Fragment fragment = Fragment.instantiate(
                context, ProductDetailRouter.PRODUCT_DETAIL_FRAGMENT);
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_PRODUCT_PASS_DATA, productPass);
        args.putBoolean(ARG_FROM_DEEPLINK, true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void goToProductDetailForResult(Fragment fragment, String productId,
                                           int adapterPosition, int requestCode) {
        Intent intent = ProductInfoActivity.createInstance(fragment.getContext(), productId,
                adapterPosition);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    public void openImagePreview(Context context, ArrayList<String> images,
                                 ArrayList<String> imageDesc, int position) {
        Intent intent = PreviewProductImageDetail.getCallingIntent(context, images, imageDesc,
                position);
        context.startActivity(intent);
    }

    private void goToDefaultRoute(Context context) {
        Intent intent = DashboardActivity.createInstance(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public String getSchemeAppLinkCancelPayment() {
        return Constants.Applinks.PAYMENT_BACK_TO_DEFAULT;
    }

    @Override
    public Intent instanceIntentCartDigitalProduct(DigitalCheckoutPassData passData) {
        return CartDigitalActivity.newInstance(this, passData);
    }

    @Override
    public Intent instanceIntentCartDigitalProductWithBundle(Bundle bundle) {
        return CartDigitalActivity.newInstance(this, bundle);
    }

    @Override
    public Intent instanceIntentDigitalProduct(DigitalCategoryDetailPassData passData) {
        return DigitalProductActivity.newInstance(this, passData);
    }

    @Override
    public Intent instanceIntentDigitalCategoryList() {
        return DigitalCategoryListActivity.newInstance(this);
    }

    @Override
    public Intent instanceIntentDigitalWeb(String url) {
        return DigitalWebActivity.newInstance(this, url);
    }

    @Override
    public boolean isSupportedDelegateDeepLink(String appLinks) {
        DeepLinkDelegate deepLinkDelegate = DeepLinkHandlerActivity.getDelegateInstance();
        return deepLinkDelegate.supportsUri(appLinks);
    }

    @Override
    public Intent getIntentDeepLinkHandlerActivity() {
        return new Intent(this, DeepLinkHandlerActivity.class);
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
        return AuthUtil.generateHeaders(
                Uri.parse(originUrl).getPath(),
                urlQuery != null ? urlQuery : "",
                "GET",
                AuthUtil.KEY.KEY_WSV4);
    }

    @Override
    public Intent getAskBuyerIntent(Context context, String toUserId, String customerName,
                                    String customSubject, String customMessage, String source,
                                    String avatar) {
        if (remoteConfig.getBoolean(TkpdInboxRouter.ENABLE_TOPCHAT))
            return ChatRoomActivity.getAskBuyerIntent(context, toUserId, customerName,
                    customSubject, customMessage, source, avatar);
        else
            return SendMessageActivityOld.getAskBuyerIntent(context, toUserId, customerName,
                    customSubject, customMessage, source);
    }


    @Override
    public Intent getAskSellerIntent(Context context, String toShopId, String shopName, String customSubject, String customMessage, String source, String avatar) {
        if(remoteConfig.getBoolean(TkpdInboxRouter.ENABLE_TOPCHAT))
            return ChatRoomActivity.getAskSellerIntent(context, toShopId, shopName,
                    customSubject, customMessage, source, avatar);
        else
            return SendMessageActivityOld.getAskSellerIntent(context, toShopId, shopName,
                    customSubject, customMessage, source);
    }

    @Override
    public Intent getAskSellerIntent(Context context, String toShopId, String shopName, String
            source, String avatar) {
        if (remoteConfig.getBoolean(TkpdInboxRouter.ENABLE_TOPCHAT))
            return ChatRoomActivity.getAskSellerIntent(context, toShopId, shopName, source, avatar);
        else
            return SendMessageActivityOld.getAskSellerIntent(context, toShopId, shopName, source);


    }

    @Override
    public Intent getAskUserIntent(Context context, String userId, String userName, String
            source, String avatar) {
        if (remoteConfig.getBoolean(TkpdInboxRouter.ENABLE_TOPCHAT))
            return ChatRoomActivity.getAskUserIntent(context, userId, userName, source, avatar);
        else
            return SendMessageActivityOld.getAskUserIntent(context, userId, userName, source);


    }

    @Override
    public Observable<GMFeaturedProductDomainModel> getFeaturedProduct() {
        GMFeaturedProductGetListUseCase gmFeaturedProductGetListUseCase = getGMComponent().getFeaturedProductGetListUseCase();
        return gmFeaturedProductGetListUseCase.getExecuteObservableAsync(RequestParams.EMPTY);
    }

    @Override
    public Observable<DataDeposit> getDataDeposit(String shopId) {
        GetDepositTopAdsUseCase getDepositTopAdsUseCase = getTopAdsComponent().getDepositTopAdsUseCase();
        return getDepositTopAdsUseCase.getExecuteObservable(GetDepositTopAdsUseCase.createRequestParams(shopId));
    }

    @Override
    public Intent getIntentCreateShop(Context context) {
        Intent intent = TkpdSeller.getIntentCreateEditShop(context, true, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    public Intent getSplashScreenIntent(Context context) {
        return new Intent(context, SplashScreenActivity.class);
    }

    @Override
    public Class getDeepLinkClass() {
        return DeepLinkActivity.class;
    }

    @Override
    public Intent getIntentManageShop(Context context) {
        return TkpdSeller.getIntentManageShop(context);
    }

    @Override
    public android.app.Fragment getFragmentShopSettings() {
        return TkpdSeller.getFragmentShopSettings();
    }

    @Override
    public android.app.Fragment getFragmentSellingNewOrder() {
        return TkpdSeller.getFragmentSellingNewOrder();
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
    public Intent getActivitySellingTransactionOpportunity(Context context) {
        return TkpdSeller.getActivitySellingTransactionOpportunity(context);
    }

    @Override
    public void goToTopAdsDashboard(Activity activity) {
        Intent intent = new Intent(activity, TopAdsDashboardActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void goToGMSubscribe(Activity activity) {
        Intent intent = new Intent(activity, GmSubscribeHomeActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public String getFlavor() {
        return BuildConfig.FLAVOR;
    }

    public void actionAppLinkPaymentModule(Activity activity, String appLinkScheme) {
        if (appLinkScheme.equalsIgnoreCase(Constants.Applinks.HOME)
                || appLinkScheme.contains(Constants.Applinks.SellerApp.SELLER_APP_HOME)) {
            actionApplink(activity, Constants.Applinks.SellerApp.SELLER_APP_HOME);
        } else {
            actionApplink(activity, appLinkScheme);
        }

    }

    @Override
    public Observable<Boolean> setCashBack(String productId, int cashback) {
        SetCashbackUseCase setCashbackUseCase = getGMComponent().getSetCashbackUseCase();
        return setCashbackUseCase.getExecuteObservableAsync(SetCashbackUseCase.createRequestParams(productId, cashback));
    }

    @Override
    public Observable<List<DataCashbackModel>> getCashbackList(List<String> productIds) {
        GetCashbackUseCase getCashbackUseCase = getGMComponent().getCashbackUseCase();
        return getCashbackUseCase.getExecuteObservable(GetCashbackUseCase.createRequestParams(productIds));
    }

    public GetShopInfoUseCase getShopInfo(){
        return getShopComponent().getShopInfoUseCase();
    }

    public void goToAddProduct(Activity activity) {
        if (activity != null) {
            ProductAddActivity.start(activity);
        }
    }

    @Override
    public void goToOrderHistory(Context context, String orderId, int userMode) {
        Intent intent = OrderHistoryActivity.createInstance(context, orderId, userMode);
        context.startActivity(intent);
    }

    @Override
    public void goToUserPaymentList(Activity activity) {
        Intent intent = new Intent(activity, ListPaymentTypeActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public ApplinkUnsupported getApplinkUnsupported(Activity activity) {
        return null;
    }

    @Override
    public boolean isInMyShop(Context context, String shopId) {
        return context != null && new SessionHandler(context).getShopID().trim().equalsIgnoreCase(shopId.trim());
    }

    @Override
    public Intent getForgotPasswordIntent(Context context, String email) {
        return ForgotPasswordActivity.getCallingIntent(context, email);
    }

    @Override
    public Intent getTimeMachineIntent(Context context) {
        return TimeMachineActivity.getCallingIntent(context, TkpdBaseURL.User.URL_INBOX_MESSAGE_TIME_MACHINE);
    }

    @Override
    public Intent getInboxMessageIntent(Context context) {
        if (remoteConfig.getBoolean(TkpdInboxRouter.ENABLE_TOPCHAT))
            return InboxChatActivity.getCallingIntent(context);
        else
            return InboxMessageActivity.getCallingIntent(context);
    }

    @Override
    public void invalidateCategoryMenuData() {

    }

    @Override
    public Intent getResolutionCenterIntent(Context context) {
        return InboxResCenterActivity.createIntent(context);
    }

    @Override
    public Intent getDetailResChatIntentBuyer(Context context, String resoId, String shopName) {
        return DetailResChatActivity.newBuyerInstance(context, resoId, shopName);
    }

    @Override
    public String applink(Activity activity, String deeplink) {
        return null;
    }

    public Intent getKolFollowingPageIntent(Context context, int userId) {
        return null;
    }

    @Override
    public Intent getChangePhoneNumberIntent(Context context, String email, String phoneNumber) {
        return ChangePhoneNumberWarningActivity.newInstance(context, email, phoneNumber);
    }

    @Override
    public void navigateToChooseAddressActivityRequest(Fragment var1, Intent var2, int var3) {
        Intent instance = ChooseAddressActivity.createInstance(var1.getContext());
        var1.startActivityForResult(instance, var3);
    }

    @Override
    public Intent getTrueCallerIntent(Context context) {
        return TruecallerActivity.getCallingIntent(context);
    }

    @Override
    public void navigateToEditAddressActivityRequest(final Fragment fragment, final int requestCode, Token token) {
        fragment.startActivityForResult(DistrictRecommendationActivity.createInstance(fragment.getActivity(),
                token),
                requestCode);
    }

    @Override
    public void navigateToGeoLocationActivityRequest(final Fragment fragment, final int requestCode, final String generatedAddress, LocationPass locationPass) {
        Intent intent = GeolocationActivity.createInstance(fragment.getActivity(), locationPass);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    public void sendEventTracking(String event, String category, String action, String label) {
        UnifyTracking.sendGTMEvent(new EventTracking(event, category, action, label).getEvent());
    }

    @Override
    public BroadcastReceiver getBroadcastReceiverTokocashPending() {
        return new TokocashPendingDataBroadcastReceiver();
    }
}