package com.tokopedia.tkpd;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.tkpd.library.utils.AnalyticsLog;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.SessionRouter;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.cacheapi.domain.interactor.CacheApiClearAllUseCase;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.subscriber.ProfileCompletionSubscriber;
import com.tokopedia.core.gcm.ApplinkUnsupported;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.home.SimpleWebViewActivity;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Token;
import com.tokopedia.core.manage.general.districtrecommendation.view.DistrictRecommendationActivity;
import com.tokopedia.core.manage.people.address.activity.ChooseAddressActivity;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.onboarding.OnboardingActivity;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.CustomerRouter;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.OtpRouter;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.router.digitalmodule.sellermodule.PeriodRangeModelCore;
import com.tokopedia.core.router.digitalmodule.sellermodule.TokoCashRouter;
import com.tokopedia.core.router.loyaltytokopoint.ILoyaltyRouter;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.router.reactnative.IReactNativeRouter;
import com.tokopedia.core.router.transactionmodule.TransactionRouter;
import com.tokopedia.core.router.wallet.IWalletRouter;
import com.tokopedia.core.util.AccessTokenRefresh;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.SessionRefresh;
import com.tokopedia.design.utils.DateLabelUtils;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.di.SessionComponent;
import com.tokopedia.di.SessionModule;
import com.tokopedia.digital.DigitalRouter;
import com.tokopedia.digital.cart.activity.CartDigitalActivity;
import com.tokopedia.digital.categorylist.view.activity.DigitalCategoryListActivity;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.product.view.activity.DigitalProductActivity;
import com.tokopedia.digital.product.view.activity.DigitalWebActivity;
import com.tokopedia.digital.receiver.TokocashPendingDataBroadcastReceiver;
import com.tokopedia.digital.tokocash.model.CashBackData;
import com.tokopedia.digital.tokocash.topup.TopupTokoCashFragment;
import com.tokopedia.discovery.DiscoveryRouter;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.TkpdFlight;
import com.tokopedia.flight.booking.domain.subscriber.model.ProfileInfo;
import com.tokopedia.flight.contactus.model.FlightContactUsPassData;
import com.tokopedia.flight.review.view.model.FlightCheckoutViewModel;
import com.tokopedia.home.IHomeRouter;
import com.tokopedia.inbox.contactus.activity.ContactUsActivity;
import com.tokopedia.inbox.contactus.activity.ContactUsCreateTicketActivity;
import com.tokopedia.inbox.inboxchat.activity.ChatRoomActivity;
import com.tokopedia.inbox.inboxchat.activity.InboxChatActivity;
import com.tokopedia.inbox.inboxchat.activity.TimeMachineActivity;
import com.tokopedia.inbox.inboxmessageold.activity.InboxMessageActivity;
import com.tokopedia.inbox.inboxmessageold.activity.SendMessageActivityOld;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.DetailResChatActivity;
import com.tokopedia.inbox.rescenter.inbox.activity.InboxResCenterActivity;
import com.tokopedia.inbox.rescenter.inboxv2.view.activity.ResoInboxActivity;
import com.tokopedia.loyalty.broadcastreceiver.TokoPointDrawerBroadcastReceiver;
import com.tokopedia.loyalty.view.activity.TokoPointWebviewActivity;
import com.tokopedia.loyalty.view.fragment.LoyaltyNotifFragmentDialog;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.otp.phoneverification.view.activity.PhoneVerificationActivationActivity;
import com.tokopedia.otp.phoneverification.view.activity.PhoneVerificationProfileActivity;
import com.tokopedia.otp.phoneverification.view.activity.ReferralPhoneNumberVerificationActivity;
import com.tokopedia.otp.phoneverification.view.activity.RidePhoneNumberVerificationActivity;
import com.tokopedia.payment.activity.TopPayActivity;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.payment.router.IPaymentModuleRouter;
import com.tokopedia.profilecompletion.data.factory.ProfileSourceFactory;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.profilecompletion.data.repository.ProfileRepositoryImpl;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.ride.RideModuleRouter;
import com.tokopedia.seller.LogisticRouter;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.TkpdSeller;
import com.tokopedia.seller.common.cashback.DataCashbackModel;
import com.tokopedia.seller.common.datepicker.view.model.PeriodRangeModel;
import com.tokopedia.seller.common.featuredproduct.GMFeaturedProductDomainModel;
import com.tokopedia.seller.common.imageeditor.GalleryCropWatermarkActivity;
import com.tokopedia.seller.common.logout.TkpdSellerLogout;
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
import com.tokopedia.seller.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.session.changephonenumber.view.activity.ChangePhoneNumberWarningActivity;
import com.tokopedia.session.forgotpassword.activity.ForgotPasswordActivity;
import com.tokopedia.session.login.loginemail.view.activity.LoginActivity;
import com.tokopedia.session.register.view.activity.RegisterInitialActivity;
import com.tokopedia.tkpd.applink.AppLinkWebsiteActivity;
import com.tokopedia.tkpd.applink.ApplinkUnsupportedImpl;
import com.tokopedia.tkpd.deeplink.DeepLinkDelegate;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;
import com.tokopedia.tkpd.deeplink.data.repository.DeeplinkRepository;
import com.tokopedia.tkpd.deeplink.data.repository.DeeplinkRepositoryImpl;
import com.tokopedia.tkpd.deeplink.domain.interactor.MapUrlUseCase;
import com.tokopedia.tkpd.drawer.DrawerBuyerHelper;
import com.tokopedia.tkpd.flight.FlightGetProfileInfoData;
import com.tokopedia.tkpd.flight.di.DaggerFlightConsumerComponent;
import com.tokopedia.tkpd.flight.di.FlightConsumerComponent;
import com.tokopedia.tkpd.flight.presentation.FlightPhoneVerificationActivity;
import com.tokopedia.tkpd.goldmerchant.GoldMerchantRedirectActivity;
import com.tokopedia.tkpd.home.ParentIndexHome;
import com.tokopedia.tkpd.home.ReactNativeOfficialStoreActivity;
import com.tokopedia.tkpd.react.DaggerReactNativeComponent;
import com.tokopedia.tkpd.react.ReactNativeComponent;
import com.tokopedia.tkpd.redirect.RedirectCreateShopActivity;
import com.tokopedia.tkpd.tkpdfeed.feedplus.FeedModuleRouter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity.KolFollowingListActivity;
import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;
import com.tokopedia.tkpd.tkpdreputation.TkpdReputationInternalRouter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationActivity;
import com.tokopedia.tkpd.tokocash.GetBalanceTokoCashWrapper;
import com.tokopedia.tkpd.tokocash.TokoCashPendingCashbackMapper;
import com.tokopedia.tkpd.tokocash.datepicker.DatePickerUtil;
import com.tokopedia.tkpdpdp.PreviewProductImageDetail;
import com.tokopedia.tkpdpdp.ProductInfoActivity;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.ReactUtils;
import com.tokopedia.tkpdreactnative.react.di.ReactNativeModule;
import com.tokopedia.tokocash.WalletUserSession;
import com.tokopedia.tokocash.di.DaggerTokoCashComponent;
import com.tokopedia.tokocash.di.TokoCashComponent;
import com.tokopedia.transaction.bcaoneklik.activity.ListPaymentTypeActivity;
import com.tokopedia.transaction.purchase.detail.activity.OrderDetailActivity;
import com.tokopedia.transaction.purchase.detail.activity.OrderHistoryActivity;
import com.tokopedia.transaction.wallet.WalletActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Response;
import rx.Observable;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;
import static com.tokopedia.core.router.productdetail.ProductDetailRouter.ARG_FROM_DEEPLINK;
import static com.tokopedia.core.router.productdetail.ProductDetailRouter.ARG_PARAM_PRODUCT_PASS_DATA;
import static com.tokopedia.core.router.productdetail.ProductDetailRouter.SHARE_DATA;

/**
 * @author normansyahputa on 12/15/16.
 */

public abstract class ConsumerRouterApplication extends MainApplication implements
        TkpdCoreRouter, SellerModuleRouter, IDigitalModuleRouter, PdpRouter,
        OtpRouter, IPaymentModuleRouter, TransactionRouter, IReactNativeRouter, ReactApplication, TkpdInboxRouter,
        TokoCashRouter, IWalletRouter, ILoyaltyRouter, ReputationRouter, SessionRouter,
        AbstractionRouter, FlightModuleRouter, LogisticRouter, FeedModuleRouter, IHomeRouter,
        DiscoveryRouter, RideModuleRouter, DigitalModuleRouter, com.tokopedia.tokocash.TokoCashRouter,
        DigitalRouter {

    @Inject
    ReactNativeHost reactNativeHost;
    @Inject
    ReactUtils reactUtils;

    private DaggerProductComponent.Builder daggerProductBuilder;
    private DaggerReactNativeComponent.Builder daggerReactNativeBuilder;
    private DaggerFlightConsumerComponent.Builder daggerFlightBuilder;
    private FlightConsumerComponent flightConsumerComponent;
    private ProductComponent productComponent;
    private DaggerShopComponent.Builder daggerShopBuilder;
    private ShopComponent shopComponent;
    private ReactNativeComponent reactNativeComponent;
    private RemoteConfig remoteConfig;
    private TokoCashComponent tokoCashComponent;

    private CacheManager cacheManager;
    private UserSession userSession;

    public static List<PeriodRangeModel> convert(List<PeriodRangeModelCore> periodRangeModelCores) {
        List<PeriodRangeModel> periodRangeModels = new ArrayList<>();
        for (PeriodRangeModelCore periodRangeModelCore : periodRangeModelCores) {
            periodRangeModels.add(new PeriodRangeModel(
                    periodRangeModelCore.getStartDate(),
                    periodRangeModelCore.getEndDate(),
                    periodRangeModelCore.getLabel()
            ));
        }
        return periodRangeModels;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeDagger();
        initDaggerInjector();
        initRemoteConfig();
    }

    private void initDaggerInjector() {
        getReactNativeComponent().inject(this);
    }

    private FlightConsumerComponent getFlightConsumerComponent() {
        if (flightConsumerComponent == null) {
            flightConsumerComponent = daggerFlightBuilder.build();
        }
        return flightConsumerComponent;
    }

    private void initializeDagger() {
        daggerProductBuilder = DaggerProductComponent.builder().productModule(new ProductModule());
        daggerReactNativeBuilder = DaggerReactNativeComponent.builder()
                .appComponent(getApplicationComponent())
                .reactNativeModule(new ReactNativeModule(this));
        daggerShopBuilder = DaggerShopComponent.builder().shopModule(new ShopModule());

        SessionComponent sessionComponent =
                DaggerSessionComponent.builder()
                        .appComponent(getApplicationComponent())
                        .sessionModule(new SessionModule())
                        .build();
        daggerFlightBuilder = DaggerFlightConsumerComponent.builder()
                .sessionComponent(sessionComponent);

        tokoCashComponent = DaggerTokoCashComponent.builder()
                .baseAppComponent((this).getBaseAppComponent())
                .build();
    }

    private void initRemoteConfig() {
        remoteConfig = new FirebaseRemoteConfigImpl(this);
    }

    @Override
    public ProductComponent getProductComponent() {
        if (productComponent == null) {
            productComponent = daggerProductBuilder.appComponent(getApplicationComponent()).build();
        }
        return productComponent;
    }

    @Override
    public ShopComponent getShopComponent() {
        if (shopComponent == null) {
            shopComponent = daggerShopBuilder.appComponent(getApplicationComponent()).build();
        }
        return shopComponent;
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
    public Intent getIntentCreateShop(Context context) {
        return TkpdSeller.getIntentCreateEditShop(context, true, false);
    }

    @Override
    public Intent getSplashScreenIntent(Context context) {
        return new Intent(context, ConsumerSplashScreen.class);
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
    public void goToProductDetail(Context context, String productUrl) {
        DeepLinkChecker.openProduct(productUrl, context);
    }

    @Override
    public Class getSellingActivityClass() {
        return TkpdSeller.getSellingActivityClass();
    }

    @Override
    public Intent getActivitySellingTransactionNewOrder(Context context) {
        return TkpdSeller.getActivitySellingTransactionNewOrder(context);
    }

    public Intent getActivitySellingTransactionConfirmShipping(Context context) {
        return TkpdSeller.getActivitySellingTransactionConfirmShipping(context);
    }

    public Intent getActivitySellingTransactionShippingStatus(Context context) {
        return TkpdSeller.getActivitySellingTransactionShippingStatus(context);
    }

    public Intent getActivitySellingTransactionList(Context context) {
        return TkpdSeller.getActivitySellingTransactionList(context);
    }

    public Intent getActivitySellingTransactionOpportunity(Context context, String query) {
        return TkpdSeller.getActivitySellingTransactionOpportunity(context, query);
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
        bundle.putParcelable(SHARE_DATA, shareData);
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
    public Fragment getProductDetailInstanceDeeplink(Context context,
                                                     @NonNull ProductPass productPass) {
        Fragment fragment = Fragment.instantiate(
                context, ProductDetailRouter.PRODUCT_DETAIL_FRAGMENT);
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_PRODUCT_PASS_DATA, productPass);
        args.putBoolean(ARG_FROM_DEEPLINK, true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void goMultipleInstagramAddProduct(Context context,
                                              ArrayList<InstagramMediaModel> instagramMediaModelList) {
        ProductDraftListActivity.startInstagramSaveBulk(context, instagramMediaModelList);
    }

    @Override
    public void goToProductDetailForResult(Fragment fragment, String productId,
                                           int adapterPosition,
                                           int requestCode) {
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

    @Override
    public void openImagePreviewFromChat(Context context, ArrayList<String> images, ArrayList<String> imageDesc, String title, String date) {
        Intent intent = PreviewProductImageDetail.getCallingIntentChat(context, images, imageDesc,
                title, date);
        context.startActivity(intent);
    }

    @Override
    public boolean isSupportedDelegateDeepLink(String appLinks) {
        DeepLinkDelegate deepLinkDelegate = DeeplinkHandlerActivity.getDelegateInstance();
        return deepLinkDelegate.supportsUri(appLinks);
    }

    @Override
    public Intent getIntentDeepLinkHandlerActivity() {
        return new Intent(this, DeeplinkHandlerActivity.class);
    }

    @Override
    public void actionNavigateByApplinksUrl(Activity activity, String applinks, Bundle bundle) {
        DeepLinkDelegate deepLinkDelegate = DeeplinkHandlerActivity.getDelegateInstance();
        Intent intent = activity.getIntent();
        intent.putExtras(bundle);
        intent.setData(Uri.parse(applinks));
        deepLinkDelegate.dispatchFrom(activity, intent);
    }

    @Override
    public String getBaseUrlDomainPayment() {
        return ConsumerAppBaseUrl.BASE_PAYMENT_URL_DOMAIN;
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
    public DrawerHelper getDrawer(AppCompatActivity activity,
                                  SessionHandler sessionHandler,
                                  LocalCacheHandler drawerCache,
                                  GlobalCacheManager globalCacheManager) {
        return DrawerBuyerHelper.createInstance(activity, sessionHandler, drawerCache, globalCacheManager);
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
        context.startActivity(intent);
    }

    @Override
    public void goToFlightActivity(Context context) {
        TkpdFlight.goToFlightActivity(context);
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
        Bundle bundle = new Bundle();
        bundle.putString(WalletActivity.EXTRA_URL, url);
        Intent intent = new Intent(context, WalletActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void goToMerchantRedirect(Context context) {
        Intent intent = new Intent(context, GoldMerchantRedirectActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void goToCreateMerchantRedirect(Context context) {
        Intent intent = RedirectCreateShopActivity.getCallingIntent(context);
        context.startActivity(intent);
    }

    @Override
    public void getUserInfo(RequestParams empty, ProfileCompletionSubscriber profileSubscriber) {
        Bundle bundle = new Bundle();
        SessionHandler sessionHandler = new SessionHandler(this);
        String authKey = SessionHandler.getAccessToken(this);
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
    public Observable<ProfileInfo> getProfile() {
        return FlightGetProfileInfoData
                .newInstance(getFlightConsumerComponent())
                .inject()
                .getProfileInfoPrefillBooking();
    }

    @Override
    public Intent getHomeHotlistIntent(Context context) {
        return ParentIndexHome.getHomeHotlistIntent(context);
    }

    @Override
    public Intent getInboxReputationIntent(Context context) {
        return InboxReputationActivity.getCallingIntent(context);
    }

    @Override
    public NotificationPass setNotificationPass(Context mContext, NotificationPass mNotificationPass,
                                                Bundle data, String notifTitle) {
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
    public Fragment getShopReputationFragment(String shopId, String shopDomain) {
        return TkpdReputationInternalRouter.getReviewShopFragment(shopId, shopDomain);
    }

    @Override
    public Intent getProductReputationIntent(Context context, String productId, String productName) {
        return TkpdReputationInternalRouter.getProductReviewIntent(context, productId, productName);
    }

    @Override
    public void actionAppLink(Context context, String linkUrl) {
        Intent intent = new Intent(context, DeeplinkHandlerActivity.class);
        intent.setData(Uri.parse(linkUrl));
        context.startActivity(intent);
    }

    @Override
    public void actionApplink(Activity activity, String linkUrl) {
        DeepLinkDelegate deepLinkDelegate = DeeplinkHandlerActivity.getDelegateInstance();
        Intent intent = activity.getIntent();
        intent.setData(Uri.parse(linkUrl));
        deepLinkDelegate.dispatchFrom(activity, intent);
    }

    @Override
    public void actionApplinkFromActivity(Activity activity, String linkUrl) {
        DeepLinkDelegate deepLinkDelegate = DeeplinkHandlerActivity.getDelegateInstance();
        Intent intent = activity.getIntent();
        intent.setData(Uri.parse(linkUrl));
        deepLinkDelegate.dispatchFrom(activity, intent);
    }

    @Override
    public void actionApplink(Activity activity, String linkUrl, String extra) {
        DeepLinkDelegate deepLinkDelegate = DeeplinkHandlerActivity.getDelegateInstance();
        Intent intent = activity.getIntent();
        intent.putExtra("extra", extra);
        intent.setData(Uri.parse(linkUrl));
        deepLinkDelegate.dispatchFrom(activity, intent);
    }

    @Override
    public void actionOpenGeneralWebView(Activity activity, String mobileUrl) {
        activity.startActivity(BannerWebView.getCallingIntent(activity, mobileUrl));
    }

    @Override
    public void onLogout(AppComponent appComponent) {
        new CacheApiClearAllUseCase().executeSync();
        TkpdSellerLogout.onLogOut(appComponent);
    }

    @Override
    public Intent getLoginIntent(Context context) {
        Intent intent = LoginActivity.getCallingIntent(context);
        return intent;
    }

    @Override
    public Intent getRegisterIntent(Context context) {
        Intent intent = RegisterInitialActivity.getCallingIntent(context);
        return intent;
    }

    @Override
    public Intent instanceIntentCartDigitalProduct(DigitalCheckoutPassData passData) {
        return CartDigitalActivity.newInstance(this, passData);
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
    public Intent getHomeIntent(Context context) {
        return new Intent(context, ParentIndexHome.class);
    }

    @Override
    public Intent getContactUsIntent(Activity activity, FlightContactUsPassData passData) {
        return ContactUsCreateTicketActivity.getCallingIntent(
                activity,
                passData.getToolbarTitle(),
                passData.getSolutionId(),
                passData.getOrderId(),
                passData.getDescriptionTitle(),
                passData.getAttachmentTitle(),
                passData.getDescription()
        );
    }

    @Override
    public Intent getDefaultContactUsIntent(Activity activity) {
        Intent intent = new Intent(activity, ContactUsActivity.class);
        intent.putExtra(InboxRouter.PARAM_URL,
                URLGenerator.generateURLContactUs(TkpdBaseURL.BASE_CONTACT_US, activity));
        return intent;
    }

    @Override
    public Intent getDefaultContactUsIntent(Activity activity, String url, String toolbarTitle) {
        Intent intent = new Intent(activity, ContactUsActivity.class);
        intent.putExtra(InboxRouter.PARAM_URL,
                URLGenerator.generateURLContactUs(Uri.encode(url), activity));
        intent.putExtra(ContactUsActivity.PARAM_TOOLBAR_TITLE, toolbarTitle);
        return intent;
    }

    @Override
    public Intent getDefaultContactUsIntent(Activity activity, String url) {
        Intent intent = new Intent(activity, ContactUsActivity.class);
        intent.putExtra(InboxRouter.PARAM_URL,
                URLGenerator.generateURLContactUs(Uri.encode(url), activity));
        return intent;
    }

    @Override
    public Intent getPhoneVerifIntent(Activity activity) {
        return FlightPhoneVerificationActivity.getCallingIntent(activity);
    }

    @Override
    public Intent getSellerHomeIntent(Activity activity) {
        return null;
    }

    @Override
    public Intent getLoginGoogleIntent(Context context) {
        return null;
    }

    @Override
    public Intent getLoginFacebookIntent(Context context) {
        return null;
    }

    @Override
    public Intent getLoginWebviewIntent(Context context, String name, String url) {
        return null;
    }

    @Override
    public Intent getOnBoardingActivityIntent(Context context) {
        return new Intent(context, OnboardingActivity.class);
    }

    @Override
    public Intent getPhoneVerificationActivityIntent(Context context) {
        return PhoneVerificationActivationActivity.getIntent(context, false, false);
    }

    @Override
    public Class<?> getHomeClass(Context context) throws ClassNotFoundException {
        return ParentIndexHome.class;
    }

    @Override
    public String getSchemeAppLinkCancelPayment() {
        return Constants.Applinks.PAYMENT_BACK_TO_DEFAULT;
    }

    @Override
    public Intent instanceIntentCartDigitalProductWithBundle(Bundle bundle) {
        return CartDigitalActivity.newInstance(this, bundle);
    }

    @Override
    public Intent getRidePhoneNumberActivityIntent(Activity activity) {
        return RidePhoneNumberVerificationActivity.getCallingIntent(activity);
    }

    @Override
    public Intent getReferralPhoneNumberActivityIntent(Activity activity) {
        return ReferralPhoneNumberVerificationActivity.getCallingIntent(activity);
    }

    @Override
    public void goToUserPaymentList(Activity activity) {
        Intent intent = new Intent(activity, ListPaymentTypeActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void goToOrderHistory(Context context, String orderId, int userMode) {
        Intent intent = OrderHistoryActivity.createInstance(context, orderId, userMode);
        context.startActivity(intent);
    }

    @Override
    public Intent goToOrderDetail(Context context, String orderId) {
        return OrderDetailActivity.createSellerInstance(context, orderId);

    }

    @Override
    public void sendAddWishlistEmitter(String productId, String userId) {
        reactUtils.sendAddWishlistEmitter(productId, userId);
    }

    @Override
    public void sendRemoveWishlistEmitter(String productId, String userId) {
        reactUtils.sendRemoveWishlistEmitter(productId, userId);
    }

    @Override
    public void sendRemoveFavoriteEmitter(String shopId, String userId) {
        reactUtils.sendRemoveFavoriteEmitter(shopId, userId);
    }

    @Override
    public void sendLoginEmitter(String userId) {
        reactUtils.sendLoginEmitter(userId);
    }

    @Override
    public void sendAddFavoriteEmitter(String shopId, String userId) {
        reactUtils.sendAddFavoriteEmitter(shopId, userId);
    }

    private ReactNativeComponent getReactNativeComponent() {
        if (reactNativeComponent == null)
            reactNativeComponent = daggerReactNativeBuilder.build();
        return reactNativeComponent;
    }

    @Override
    public ReactNativeHost getReactNativeHost() {
        if (reactNativeHost == null) initDaggerInjector();
        return reactNativeHost;
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
    public Intent getAskSellerIntent(Context context, String toShopId, String shopName,
                                     String customSubject, String customMessage, String source, String avatar) {

        if (remoteConfig.getBoolean(TkpdInboxRouter.ENABLE_TOPCHAT))
            return ChatRoomActivity.getAskSellerIntent(context, toShopId, shopName,
                    customSubject, customMessage, source, avatar);
        else
            return SendMessageActivityOld.getAskSellerIntent(context, toShopId, shopName,
                    customSubject, customMessage, source);

    }


    @Override
    public Intent getAskUserIntent(Context context, String userId, String userName, String source,
                                   String avatar) {
        if (remoteConfig.getBoolean(TkpdInboxRouter.ENABLE_TOPCHAT))
            return ChatRoomActivity.getAskUserIntent(context, userId, userName, source, avatar);
        else
            return SendMessageActivityOld.getAskUserIntent(context, userId, userName, source);


    }

    @Override
    public Intent getAskSellerIntent(Context context, String toShopId, String shopName,
                                     String customSubject, String source) {
        if (remoteConfig.getBoolean(TkpdInboxRouter.ENABLE_TOPCHAT))
            return ChatRoomActivity.getAskSellerIntent(context, toShopId, shopName, customSubject, source);
        else
            return SendMessageActivityOld.getAskSellerIntent(context, toShopId, shopName, customSubject, source);


    }

    @Override
    public void goToGMSubscribe(Activity activity) {
        Intent intent = new Intent(activity, GoldMerchantRedirectActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void navigateAppLinkWallet(Context context,
                                      String appLinkScheme,
                                      String alternateRedirectUrl,
                                      Bundle bundlePass) {
        context.startActivity(getIntentAppLinkWallet(context, appLinkScheme, alternateRedirectUrl));
    }

    @Override
    public void navigateAppLinkWallet(Activity activity,
                                      int requestCode,
                                      String appLinkScheme,
                                      String alternateRedirectUrl,
                                      Bundle bundlePass) {
        activity.startActivityForResult(
                getIntentAppLinkWallet(activity, appLinkScheme, alternateRedirectUrl), requestCode
        );
    }

    @Override
    public void navigateAppLinkWallet(android.app.Fragment fragment,
                                      int requestCode,
                                      String appLinkScheme,
                                      String alternateRedirectUrl,
                                      Bundle bundlePass) {
        fragment.startActivityForResult(
                getIntentAppLinkWallet(
                        fragment.getActivity(), appLinkScheme, alternateRedirectUrl
                ), requestCode
        );
    }

    @Override
    public void navigateAppLinkWallet(Fragment fragmentSupport,
                                      int requestCode,
                                      String appLinkScheme,
                                      String alternateRedirectUrl,
                                      Bundle bundlePass) {
        fragmentSupport.startActivityForResult(
                getIntentAppLinkWallet(fragmentSupport.getActivity(),
                        appLinkScheme, alternateRedirectUrl
                ), requestCode
        );
    }

    /**
     * @param context
     * @param appLinkScheme
     * @param alternateRedirectUrl
     * @return
     */

    private Intent getIntentAppLinkWallet(Context context, String appLinkScheme,
                                          String alternateRedirectUrl) {

        return appLinkScheme == null || appLinkScheme.isEmpty() ?
                DigitalWebActivity.newInstance(context, alternateRedirectUrl)
                : isSupportedDelegateDeepLink(appLinkScheme)
                ? new Intent(context, DeeplinkHandlerActivity.class).setData(Uri.parse(appLinkScheme))
                : DigitalWebActivity.newInstance(context, appLinkScheme);
    }

    @Override
    public String getFlavor() {
        return BuildConfig.FLAVOR;
    }

    public Observable<GMFeaturedProductDomainModel> getFeaturedProduct() {
        GMFeaturedProductDomainModel gmFeaturedProductDomainModel = new GMFeaturedProductDomainModel();
        gmFeaturedProductDomainModel.setData(new ArrayList<GMFeaturedProductDomainModel.Datum>());
        return Observable.just(gmFeaturedProductDomainModel);
    }

    @Override
    public void actionAppLinkPaymentModule(Activity activity, String appLinkScheme) {
        if (appLinkScheme.equalsIgnoreCase(Constants.Applinks.HOME)
                || appLinkScheme.contains(Constants.Applinks.SellerApp.SELLER_APP_HOME)) {
            actionApplink(activity, Constants.Applinks.HOME);
        } else {
            actionApplink(activity, appLinkScheme);
        }

    }

    @Override
    public Observable<Boolean> setCashBack(String productId, int cashback) {
        return Observable.just(false);
    }

    @Override
    public Observable<List<DataCashbackModel>> getCashbackList(List<String> productIds) {
        List<DataCashbackModel> dataCashbackModels = new ArrayList<>();
        return Observable.just(dataCashbackModels);
    }

    public void goToAddProduct(Activity activity) {
        if (activity != null) {
            ProductAddActivity.start(activity);
        }
    }

    @Override
    public ApplinkUnsupported getApplinkUnsupported(Activity activity) {
        return new ApplinkUnsupportedImpl(activity);
    }

    @Override
    public boolean isInMyShop(Context context, String shopId) {
        return context != null && new SessionHandler(context).getShopID().trim().equalsIgnoreCase(shopId.trim());
    }

    @Override
    public Intent goToDatePicker(Activity activity, List<PeriodRangeModelCore> periodRangeModels,
                                 long startDate, long endDate,
                                 int datePickerSelection, int datePickerType) {
        return DatePickerUtil.getDatePickerIntent(activity, startDate, endDate, convert(periodRangeModels),
                datePickerSelection, datePickerType);
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
    public Intent getGalleryIntent(Context context, boolean forceOpenCamera, int maxImageSelection, boolean compressToTkpd) {
        return GalleryCropWatermarkActivity.createIntent(context, 1, forceOpenCamera, maxImageSelection, compressToTkpd);
    }

    @Override
    public String getRangeDateFormatted(Context context, long startDate, long endDate) {
        return DateLabelUtils.getRangeDateFormatted(context, startDate, endDate);
    }

    @Override
    public WalletUserSession getTokoCashSession() {
        return new WalletUserSessionImpl(this);
    }

    @Override
    public Intent getWebviewActivityWithIntent(Context context, String url, String title) {
        return SimpleWebViewActivity.getIntentWithTitle(context, url, title);
    }

    @Override
    public String getUserEmailProfil() {
        SessionHandler sessionHandler = new SessionHandler(this);
        return sessionHandler.getEmail();
    }

    @Override
    public Fragment getTopupTokoCashFragment() {
        return TopupTokoCashFragment.newInstance();
    }

    @Override
    public void invalidateCategoryMenuData() {

    }

    @Override
    public Intent getResolutionCenterIntent(Context context) {
        return InboxResCenterActivity.createIntent(context);
    }

    @Override
    public Intent getResolutionCenterIntentBuyer(Context context) {
        return ResoInboxActivity.newBuyerInstance(context);
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
    public void onForceLogout(Activity activity) {
        SessionHandler sessionHandler = new SessionHandler(activity);
        sessionHandler.forceLogout();
        invalidateCategoryMenuData();
        Intent intent = CustomerRouter.getSplashScreenIntent(getBaseContext());
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
    public void showForceLogoutDialog(Response response) {
        ServerErrorHandler.showForceLogoutDialog();
        ServerErrorHandler.sendForceLogoutAnalytics(response.request().url().toString());
    }

    @Override
    public void showServerError(Response response) {
        ServerErrorHandler.showServerErrorSnackbar();
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
    public AnalyticTracker getAnalyticTracker() {
        return new AnalyticTracker() {
            @Override
            public void sendEventTracking(Map<String, Object> events) {

            }

            @Override
            public void sendEventTracking(String event, String category, String action, String label) {
                UnifyTracking.sendGTMEvent(new EventTracking(
                        event,
                        category,
                        action,
                        label
                ).getEvent());
            }

            @Override
            public void sendScreen(Activity activity, final String screenName) {
                ScreenTracking.sendScreen(activity, new ScreenTracking.IOpenScreenAnalytics() {
                    @Override
                    public String getScreenName() {
                        return screenName;
                    }
                });
            }
        };
    }

    @Override
    public UserSession getSession() {
        if (userSession == null) {
            userSession = new UserSessionImpl(this);
        }
        return userSession;
    }

    @Override
    public CacheManager getGlobalCacheManager() {
        if (cacheManager == null) {
            cacheManager = new GlobalCacheManager();
        }
        return cacheManager;
    }

    @Override
    public long getLongConfig(String flightAirport) {
        return remoteConfig.getLong(flightAirport);
    }

    @Override
    public Intent getLoginIntent() {
        Intent intent = LoginActivity.getCallingIntent(this);
        return intent;
    }

    @Override
    public Intent getTopPayIntent(Activity activity, FlightCheckoutViewModel flightCheckoutViewModel) {
        PaymentPassData paymentPassData = new PaymentPassData();
        paymentPassData.setPaymentId(flightCheckoutViewModel.getPaymentId());
        paymentPassData.setTransactionId(flightCheckoutViewModel.getTransactionId());
        paymentPassData.setRedirectUrl(flightCheckoutViewModel.getRedirectUrl());
        paymentPassData.setCallbackFailedUrl(flightCheckoutViewModel.getCallbackFailedUrl());
        paymentPassData.setCallbackSuccessUrl(flightCheckoutViewModel.getCallbackSuccessUrl());
        paymentPassData.setQueryString(flightCheckoutViewModel.getQueryString());
        return TopPayActivity.createInstance(activity, paymentPassData);
    }

    @Override
    public int getTopPayPaymentSuccessCode() {
        return TopPayActivity.PAYMENT_SUCCESS;
    }

    @Override
    public int getTopPayPaymentFailedCode() {
        return TopPayActivity.PAYMENT_FAILED;
    }

    @Override
    public int getTopPayPaymentCancelCode() {
        return TopPayActivity.PAYMENT_CANCELLED;
    }

    @Override
    public Intent getBannerWebViewIntent(Activity activity, String url) {
        return BannerWebView.getCallingIntent(activity, url);
    }

    @Override
    public Intent getWebviewActivity(Activity activity, String url) {
        return AppLinkWebsiteActivity.newInstance(activity, url);
    }

    @Override
    public DialogFragment getLoyaltyTokoPointNotificationDialogFragment(TokoPointDrawerData.PopUpNotif popUpNotif) {
        return LoyaltyNotifFragmentDialog.newInstance(popUpNotif);
    }

    @Override
    public String applink(Activity activity, String deeplink) {
        DeeplinkRepository deeplinkRepository = new DeeplinkRepositoryImpl(this, new GlobalCacheManager());
        MapUrlUseCase mapUrlUseCase = new MapUrlUseCase(deeplinkRepository);
        Uri uri = Uri.parse(deeplink);
        final List<String> linkSegments = uri.getPathSegments();
        StringBuilder finalSegments = new StringBuilder();
        for (int i = 0; i < linkSegments.size(); i++) {
            if (i != linkSegments.size() - 1) {
                finalSegments.append(linkSegments.get(i)).append("/");
            } else {
                finalSegments.append(linkSegments.get(i));
            }
        }
        return mapUrlUseCase.getData(mapUrlUseCase.setRequestParam(finalSegments.toString())).applink;
    }

    public Intent getKolFollowingPageIntent(Context context, int userId) {
        return KolFollowingListActivity.getCallingIntent(context, userId);
    }

    @Override
    public Intent getChangePhoneNumberIntent(Context context, String email, String phoneNumber) {
        return ChangePhoneNumberWarningActivity.newInstance(context, email, phoneNumber);
    }

    @Override
    public Observable<TokoCashData> getTokoCashBalance() {
        return new GetBalanceTokoCashWrapper(this, tokoCashComponent.getBalanceTokoCashUseCase())
                .processGetBalance();
    }

    @Override
    public void sendEventTracking(String event, String category, String action, String label) {
        UnifyTracking.sendGTMEvent(new EventTracking(event, category, action, label).getEvent());
    }

    @Override
    public void navigateToChooseAddressActivityRequest(Fragment var1, Intent var2, int var3) {
        Intent instance = ChooseAddressActivity.createInstance(var1.getContext());
        var1.startActivityForResult(instance, var3);
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
    public Intent getPhoneVerificationActivationIntent(Context context) {
        return PhoneVerificationActivationActivity.getCallingIntent(context);
    }

    @Override
    public Intent getPhoneVerificationProfileIntent(Context context) {
        return PhoneVerificationProfileActivity.getCallingIntent(context);
    }

    @Override
    public BroadcastReceiver getTokoPointBroadcastReceiver() {
        return new TokoPointDrawerBroadcastReceiver();
    }

    @Override
    public BroadcastReceiver getBroadcastReceiverTokocashPending() {
        return new TokocashPendingDataBroadcastReceiver();
    }

    public GetShopInfoUseCase getShopInfo() {
        return getShopComponent().getShopInfoUseCase();
    }

    @Override
    public Observable<CashBackData> getPendingCashbackUseCase(Context context) {
        SessionHandler sessionHandler = new SessionHandler(context);
        com.tokopedia.usecase.RequestParams requestParams = com.tokopedia.usecase.RequestParams.create();
        requestParams.putString("msisdn", sessionHandler.getPhoneNumber());
        return tokoCashComponent.getPendingCasbackUseCase()
                .createObservable(requestParams)
                .map(new TokoCashPendingCashbackMapper());
    }

    @Override
    public Intent getContactUsIntent(Context context) {
        return new Intent(context, ContactUsActivity.class);
    }

    @Override
    public void openIntermediaryActivity(Activity activity, String depID, String title) {
        IntermediaryActivity.moveTo(activity, depID, title);
    }

    @Override
    public void onDigitalItemClick(Activity activity, DigitalCategoryDetailPassData passData, String appLink) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(DigitalProductActivity.EXTRA_CATEGORY_PASS_DATA, passData);
        DeepLinkDelegate deepLinkDelegate = DeeplinkHandlerActivity.getDelegateInstance();
        Intent intent = activity.getIntent();
        intent.setData(Uri.parse(appLink));
        intent.putExtras(bundle);
        deepLinkDelegate.dispatchFrom(activity, intent);
    }

    @Override
    public void openReactNativeOfficialStore(FragmentActivity activity) {
        startActivity(ReactNativeOfficialStoreActivity.createCallingIntent(
                activity, ReactConst.Screen.OFFICIAL_STORE,
                getString(R.string.react_native_banner_official_title)));
    }

    @Override
    public void showForceHockeyAppDialog() {
        ServerErrorHandler.showForceHockeyAppDialog();
    }

    @Override
    public void logInvalidGrant(Response response) {
        AnalyticsLog.logInvalidGrant(response.request().url().toString());
    }

    @Override
    public void openTokoPoint(Context context, String url) {
        startActivity(TokoPointWebviewActivity.getIntent(context, url));
    }
}
