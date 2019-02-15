package com.tokopedia.sellerapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.crashlytics.android.Crashlytics;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.legacy.AnalyticsLog;
import com.tokopedia.SessionRouter;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.applink.ApplinkDelegate;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkUnsupported;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.broadcast.message.BroadcastMessageInternalRouter;
import com.tokopedia.broadcast.message.common.BroadcastMessageRouter;
import com.tokopedia.broadcast.message.common.constant.BroadcastMessageConstant;
import com.tokopedia.cacheapi.domain.interactor.CacheApiClearAllUseCase;
import com.tokopedia.changepassword.ChangePasswordRouter;
import com.tokopedia.changephonenumber.ChangePhoneNumberRouter;
import com.tokopedia.changephonenumber.view.activity.ChangePhoneNumberWarningActivity;
import com.tokopedia.chatbot.ChatbotRouter;
import com.tokopedia.transaction.common.sharedata.AddToCartRequest;
import com.tokopedia.transaction.common.sharedata.AddToCartResult;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.contactus.ContactUsModuleRouter;
import com.tokopedia.contactus.createticket.ContactUsConstant;
import com.tokopedia.contactus.createticket.activity.ContactUsActivity;
import com.tokopedia.contactus.home.view.ContactUsHomeActivity;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.MaintenancePage;
import com.tokopedia.core.Router;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.subscriber.ProfileCompletionSubscriber;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.home.SimpleWebViewWithFilePickerActivity;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.manage.people.address.activity.ChooseAddressActivity;
import com.tokopedia.core.manage.people.profile.activity.ManagePeopleProfileActivity;
import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.core.network.CoreNetworkRouter;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.peoplefave.fragment.PeopleFavoritedShopFragment;
import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.flashsale.management.router.FlashSaleInternalRouter;
import com.tokopedia.flashsale.management.router.FlashSaleRouter;
import com.tokopedia.core.share.DefaultShare;
import com.tokopedia.core.util.AccessTokenRefresh;
import com.tokopedia.core.util.AppWidgetUtil;
import com.tokopedia.core.util.BranchSdkUtils;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.SessionRefresh;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital.cart.presentation.activity.CartDigitalActivity;
import com.tokopedia.digital.categorylist.view.activity.DigitalCategoryListActivity;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.product.view.activity.DigitalProductActivity;
import com.tokopedia.digital.product.view.activity.DigitalWebActivity;
import com.tokopedia.district_recommendation.domain.model.Token;
import com.tokopedia.district_recommendation.view.DistrictRecommendationActivity;
import com.tokopedia.district_recommendation.view.shopsettings.DistrictRecommendationShopSettingsActivity;
import com.tokopedia.fingerprint.util.FingerprintConstant;
import com.tokopedia.gm.GMModuleRouter;
import com.tokopedia.gm.cashback.domain.GetCashbackUseCase;
import com.tokopedia.gm.common.di.component.DaggerGMComponent;
import com.tokopedia.gm.common.di.component.GMComponent;
import com.tokopedia.gm.common.di.module.GMModule;
import com.tokopedia.gm.featured.domain.interactor.GMFeaturedProductGetListUseCase;
import com.tokopedia.gm.subscribe.GMSubscribeInternalRouter;
import com.tokopedia.gm.subscribe.GmSubscribeModuleRouter;
import com.tokopedia.imageuploader.ImageUploaderRouter;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.DetailResChatActivity;
import com.tokopedia.inbox.rescenter.inbox.activity.InboxResCenterActivity;
import com.tokopedia.inbox.rescenter.inboxv2.view.activity.ResoInboxActivity;
import com.tokopedia.kol.KolRouter;
import com.tokopedia.kol.feature.post.view.fragment.KolPostShopFragment;
import com.tokopedia.loginregister.LoginRegisterRouter;
import com.tokopedia.loginregister.login.view.activity.LoginActivity;
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity;
import com.tokopedia.logisticgeolocation.pinpoint.GeolocationActivity;
import com.tokopedia.logisticuploadawb.ILogisticUploadAwbRouter;
import com.tokopedia.merchantvoucher.MerchantVoucherModuleRouter;
import com.tokopedia.mitratoppers.MitraToppersRouter;
import com.tokopedia.mitratoppers.MitraToppersRouterInternal;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.otp.OtpModuleRouter;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.payment.router.IPaymentModuleRouter;
import com.tokopedia.payment.setting.PaymentSettingInternalRouter;
import com.tokopedia.payment.setting.util.PaymentSettingRouter;
import com.tokopedia.phoneverification.PhoneVerificationRouter;
import com.tokopedia.phoneverification.view.activity.PhoneVerificationActivationActivity;
import com.tokopedia.phoneverification.view.activity.PhoneVerificationProfileActivity;
import com.tokopedia.product.manage.item.common.di.component.DaggerProductComponent;
import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.common.di.module.ProductModule;
import com.tokopedia.product.manage.item.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.product.manage.item.main.add.view.activity.ProductAddNameCategoryActivity;
import com.tokopedia.product.manage.item.main.base.data.model.ProductPictureViewModel;
import com.tokopedia.product.manage.item.main.edit.view.activity.ProductEditActivity;
import com.tokopedia.product.manage.item.utils.ProductEditModuleRouter;
import com.tokopedia.product.manage.item.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.product.manage.item.variant.data.model.variantbyprd.ProductVariantViewModel;
import com.tokopedia.product.manage.list.view.activity.ProductManageActivity;
import com.tokopedia.profile.ProfileModuleRouter;
import com.tokopedia.profile.view.activity.ProfileActivity;
import com.tokopedia.profilecompletion.data.factory.ProfileSourceFactory;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.profilecompletion.data.repository.ProfileRepositoryImpl;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.saldodetails.router.SaldoDetailsInternalRouter;
import com.tokopedia.seller.LogisticRouter;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.TkpdSeller;
import com.tokopedia.seller.common.cashback.DataCashbackModel;
import com.tokopedia.seller.common.featuredproduct.GMFeaturedProductDomainModel;
import com.tokopedia.seller.common.logout.TkpdSellerLogout;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;
import com.tokopedia.seller.product.category.view.activity.CategoryPickerActivity;
import com.tokopedia.seller.product.draft.view.activity.ProductDraftListActivity;
import com.tokopedia.seller.product.etalase.utils.EtalaseUtils;
import com.tokopedia.seller.product.etalase.view.activity.EtalasePickerActivity;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantDashboardActivity;
import com.tokopedia.seller.reputation.view.fragment.SellerReputationFragment;
import com.tokopedia.seller.shop.common.di.component.DaggerShopComponent;
import com.tokopedia.seller.shop.common.di.component.ShopComponent;
import com.tokopedia.seller.shop.common.di.module.ShopModule;
import com.tokopedia.seller.shopsettings.shipping.EditShippingActivity;
import com.tokopedia.sellerapp.dashboard.view.activity.DashboardActivity;
import com.tokopedia.sellerapp.deeplink.DeepLinkActivity;
import com.tokopedia.sellerapp.deeplink.DeepLinkDelegate;
import com.tokopedia.sellerapp.deeplink.DeepLinkHandlerActivity;
import com.tokopedia.sellerapp.drawer.DrawerSellerHelper;
import com.tokopedia.sellerapp.utils.FingerprintModelGenerator;
import com.tokopedia.sellerapp.welcome.WelcomeActivity;
import com.tokopedia.session.addchangeemail.view.activity.AddEmailActivity;
import com.tokopedia.session.addchangepassword.view.activity.AddPasswordActivity;
import com.tokopedia.session.changename.view.activity.ChangeNameActivity;
import com.tokopedia.session.forgotpassword.activity.ForgotPasswordActivity;
import com.tokopedia.settingbank.BankRouter;
import com.tokopedia.settingbank.banklist.view.activity.SettingBankActivity;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.ShopPageInternalRouter;
import com.tokopedia.shop.common.router.ShopSettingRouter;
import com.tokopedia.shop.open.ShopOpenRouter;
import com.tokopedia.shop.settings.ShopSettingsInternalRouter;
import com.tokopedia.talk.common.TalkRouter;
import com.tokopedia.talk.inboxtalk.view.activity.InboxTalkActivity;
import com.tokopedia.talk.producttalk.view.activity.TalkProductActivity;
import com.tokopedia.talk.shoptalk.view.activity.ShopTalkActivity;
import com.tokopedia.talk.talkdetails.view.activity.TalkDetailsActivity;
import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;
import com.tokopedia.tkpd.tkpdreputation.TkpdReputationInternalRouter;
import com.tokopedia.tkpd.tkpdreputation.analytic.ReputationTracking;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationActivity;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.ReviewShopInfoActivity;
import com.tokopedia.tkpdpdp.PreviewProductImageDetail;
import com.tokopedia.tkpdpdp.ProductInfoActivity;
import com.tokopedia.tkpdpdp.tracking.ProductPageTracking;
import com.tokopedia.topads.TopAdsComponentInstance;
import com.tokopedia.topads.TopAdsManagementInternalRouter;
import com.tokopedia.topads.TopAdsManagementRouter;
import com.tokopedia.topads.TopAdsModuleRouter;
import com.tokopedia.topads.common.TopAdsWebViewRouter;
import com.tokopedia.topads.dashboard.TopAdsDashboardInternalRouter;
import com.tokopedia.topads.dashboard.TopAdsDashboardRouter;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsComponent;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsModule;
import com.tokopedia.topads.dashboard.domain.interactor.GetDepositTopAdsUseCase;
import com.tokopedia.topads.dashboard.view.activity.TopAdsCheckProductPromoActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity;
import com.tokopedia.topchat.attachproduct.view.activity.BroadcastMessageAttachProductActivity;
import com.tokopedia.topchat.chatlist.activity.InboxChatActivity;
import com.tokopedia.topchat.common.TopChatRouter;
import com.tokopedia.transaction.common.TransactionRouter;
import com.tokopedia.transaction.orders.UnifiedOrderListRouter;
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity;
import com.tokopedia.trackingoptimizer.TrackingOptimizerRouter;
import com.tokopedia.transaction.orders.orderlist.view.activity.SellerOrderListActivity;
import com.tokopedia.transaction.purchase.detail.activity.OrderDetailActivity;
import com.tokopedia.transaction.purchase.detail.activity.OrderHistoryActivity;
import com.tokopedia.updateinactivephone.activity.ChangeInactiveFormRequestActivity;
import com.tokopedia.withdraw.WithdrawRouter;
import com.tokopedia.withdraw.view.activity.WithdrawActivity;
import com.tokopedia.abstraction.ActionInterfaces.ActionCreator;
import com.tokopedia.abstraction.ActionInterfaces.ActionUIDelegate;
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
import static com.tokopedia.core.router.productdetail.ProductDetailRouter.ARG_FROM_DEEPLINK;
import static com.tokopedia.core.router.productdetail.ProductDetailRouter.ARG_PARAM_PRODUCT_PASS_DATA;

/**
 * Created by normansyahputa on 12/15/16.
 */

public abstract class SellerRouterApplication extends MainApplication
        implements TkpdCoreRouter, SellerModuleRouter, PdpRouter, GMModuleRouter, TopAdsModuleRouter,
        IPaymentModuleRouter, IDigitalModuleRouter, TkpdInboxRouter, TransactionRouter,
        ReputationRouter, LogisticRouter, SessionRouter, ProfileModuleRouter,
        MitraToppersRouter, AbstractionRouter, DigitalModuleRouter, ShopModuleRouter,
        ApplinkRouter, OtpModuleRouter, ImageUploaderRouter, ILogisticUploadAwbRouter,
        NetworkRouter, TopChatRouter, ProductEditModuleRouter, TopAdsWebViewRouter, ContactUsModuleRouter,
        BankRouter, ChangePasswordRouter, WithdrawRouter, ShopSettingRouter, GmSubscribeModuleRouter,
        KolRouter, PaymentSettingRouter, TalkRouter, ChangePhoneNumberRouter, PhoneVerificationRouter,
        com.tokopedia.tkpdpdp.ProductDetailRouter,
        TopAdsDashboardRouter,
        TopAdsManagementRouter,
        DigitalRouter,
        BroadcastMessageRouter,
        MerchantVoucherModuleRouter,
        LoginRegisterRouter,
        UnifiedOrderListRouter,
        CoreNetworkRouter,
        ChatbotRouter,
        TrackingOptimizerRouter ,
        FlashSaleRouter{

    protected RemoteConfig remoteConfig;
    private DaggerProductComponent.Builder daggerProductBuilder;
    private ProductComponent productComponent;
    private DaggerGMComponent.Builder daggerGMBuilder;
    private GMComponent gmComponent;
    private DaggerTopAdsComponent.Builder daggerTopAdsBuilder;
    private TopAdsComponent topAdsComponent;
    private DaggerShopComponent.Builder daggerShopBuilder;
    private ShopComponent shopComponent;
    private AnalyticTracker analyticTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        analyticTracker = initializeAnalyticTracker();
        initializeDagger();
        initializeRemoteConfig();
    }

    private AnalyticTracker initializeAnalyticTracker() {
        return new AnalyticTracker() {
            @Override
            public void sendEventTracking(Map<String, Object> events) {
                UnifyTracking.eventClearEnhanceEcommerce(SellerRouterApplication.this);
                UnifyTracking.sendGTMEvent(SellerRouterApplication.this, events);
            }

            @Override
            public void sendEventTracking(String event, String category, String action, String label) {
                UnifyTracking.sendGTMEvent(SellerRouterApplication.this, new EventTracking(
                        event,
                        category,
                        action,
                        label
                ).getEvent());
            }

            @Override
            public void sendScreen(Activity activity, final String screenName) {
                if (activity != null && !TextUtils.isEmpty(screenName)) {
                    ScreenTracking.sendScreen(activity, () -> screenName);
                }
            }

            @Override
            public void sendCustomScreen(Activity activity, String screenName, String shopID, String shopType, String pageType, String productId) {
                if (activity != null && !TextUtils.isEmpty(screenName)) {
                    ScreenTracking.eventCustomScreen(activity, screenName, shopID,
                            shopType, pageType, productId);
                }
            }

            @Override
            public void sendEnhancedEcommerce(Map<String, Object> trackingData) {
                TrackingUtils.eventTrackingEnhancedEcommerce(SellerRouterApplication.this, trackingData);
            }
        };
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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void goToEtalaseList(Context context) {
        Intent intent = ShopSettingsInternalRouter.getShopSettingsEtalaseActivity(context);
        context.startActivity(intent);
    }

    @Override
    public void goToDraftProductList(Context context) {
        Intent intent = new Intent(context, ProductDraftListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void clearEtalaseCache() {
        EtalaseUtils.clearEtalaseCache(getApplicationContext());
    }

    @Override
    public Intent goToEditProduct(Context context, boolean isEdit, String productId) {
        return ProductEditActivity.Companion.createInstance(context, productId);
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
        Intent intent = GMSubscribeInternalRouter.getGMSubscribeHomeIntent(context);
        context.startActivity(intent);
    }

    @Override
    public void goToGmSubscribeMembershipRedirect(Context context) {
        Intent intent = GMSubscribeInternalRouter.getGMMembershipIntent(context);
        context.startActivity(intent);
    }

    public Intent getMitraToppersActivityIntent(Context context) {
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
    public Intent getHomeHotlistIntent(Context context) {
        return null;
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
    public Fragment getShopReputationFragment(String shopId, String shopDomain) {
        return TkpdReputationInternalRouter.getReviewShopFragment(shopId, shopDomain);
    }

    @Override
    public Intent getProductReputationIntent(Context context, String productId, String productName) {
        return TkpdReputationInternalRouter.getProductReviewIntent(context, productId, productName);
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
    public Intent getAddEmailIntent(Context context) {
        return AddEmailActivity.newInstance(context);
    }

    @Override
    public Intent getAddPasswordIntent(Context context) {
        return AddPasswordActivity.newInstance(context);
    }

    @Override
    public Intent getChangeNameIntent(Context context) {
        return ChangeNameActivity.newInstance(context);
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
        new CacheApiClearAllUseCase().executeSync();

        TkpdSellerLogout.onLogOut(appComponent);
    }

    @Override
    public Intent getLoginIntent(Context context) {
        Intent intent = LoginActivity.getCallingIntent(context);
        return intent;
    }

    @Override
    public void sendEventTracking(Map<String, Object> eventTracking) {
        UnifyTracking.eventClearEnhanceEcommerce(this);
        UnifyTracking.sendGTMEvent(getAppContext(), eventTracking);
        CommonUtils.dumper(eventTracking.toString());
    }

    @Override
    public void sendEventTrackingGmSubscribe(Map<String, Object> eventTracking) {
        UnifyTracking.sendGTMEvent(getAppContext(), eventTracking);
        CommonUtils.dumper(eventTracking.toString());
    }

    @Override
    public Intent getRegisterIntent(Context context) {
        Intent intent = RegisterInitialActivity.getCallingIntent(context);
        return intent;
    }

    @Override
    public void sendScreenName(@NonNull String screenName) {
        ScreenTracking.screen(this, screenName);
    }

    @Override
    public void gotToProductDetail(Context context) {
        Intent intent = ProductInfoActivity.createInstance(context);
        context.startActivity(intent);
    }

    @Override
    public void goToProductDetail(Context context, String productId, String name, String displayedPrice, String imageUrl, String attribution, String listNameOfProduct) {
        ProductItem data = new ProductItem();
        data.setId(productId);
        data.setName(name);
        data.setPrice(displayedPrice);
        data.setImgUri(imageUrl);
        data.setTrackerAttribution(attribution);
        data.setTrackerListName(listNameOfProduct);
        Bundle bundle = new Bundle();
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(context);
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void goToProductDetail(Context context, String productUrl) {
        DeepLinkChecker.openProduct(productUrl, context);
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
    public void goToWebview(Context context, String url) {
        Intent intent = new Intent(this, BannerWebView.class);
        intent.putExtra(BannerWebView.EXTRA_URL, url);
        context.startActivity(intent);
    }

    @Override
    public void goToProductDetailById(Context activity, String productId) {
        activity.startActivity(ProductInfoActivity.createInstance(activity, productId));
    }

    @Override
    public void goToProfileShop(Context context, String userId) {
        context.startActivity(
                getTopProfileIntent(context, userId)
        );
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

    @Override
    public void openImagePreview(Context context, ArrayList<String> images,
                                 int position) {

    }

    @Override
    public void openImagePreviewFromChat(Context context, ArrayList<String> images,
                                         ArrayList<String> imageDesc, String title, String date) {
        Intent intent = PreviewProductImageDetail.getCallingIntentChat(context, images, imageDesc,
                title, date);
        context.startActivity(intent);
    }

    private void goToDefaultRoute(Context context) {
        Intent intent = DashboardActivity.createInstance(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
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
        return TopChatRoomActivity.getAskBuyerIntent(context, toUserId, customerName,
                customSubject, customMessage, source, avatar);
    }


    @Override
    public Intent getAskSellerIntent(Context context, String toShopId, String shopName, String customSubject, String customMessage, String source, String avatar) {

        return TopChatRoomActivity.getAskSellerIntent(context, toShopId, shopName,
                customSubject, customMessage, source, avatar);

    }

    @Override
    public Intent getAskSellerIntent(Context context, String toShopId, String shopName, String
            source, String avatar) {
        return TopChatRoomActivity.getAskSellerIntent(context, toShopId, shopName, source, avatar);

    }

    @Override
    public Intent getAskUserIntent(Context context, String userId, String userName, String
            source, String avatar) {
        return TopChatRoomActivity.getAskUserIntent(context, userId, userName, source, avatar);
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
        Intent intent = ShopOpenRouter.getIntentCreateEditShop(context);
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
    public Intent getActivitySellingTransactionOpportunity(Context context, String query) {
        return TkpdSeller.getActivitySellingTransactionOpportunity(context, query);
    }

    @Override
    public void goToTopAdsDashboard(Activity activity) {
        Intent intent = new Intent(activity, TopAdsDashboardActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void goToGMSubscribe(Activity activity) {
        Intent intent = GMSubscribeInternalRouter.getGMSubscribeHomeIntent(activity);
        activity.startActivity(intent);
    }

    @Override
    public Intent getGMHomeIntent(Context context) {
        return GMSubscribeInternalRouter.getGMSubscribeHomeIntent(context);
    }

    @Override
    public String getFlavor() {
        return BuildConfig.FLAVOR;
    }

    @Override
    public Observable<List<DataCashbackModel>> getCashbackList(List<String> productIds) {
        GetCashbackUseCase getCashbackUseCase = getGMComponent().getCashbackUseCase();
        return getCashbackUseCase.getExecuteObservable(GetCashbackUseCase.createRequestParams(productIds));
    }

    public GetShopInfoUseCase getShopInfo() {
        return getShopComponent().getShopInfoUseCase();
    }

    public void goToAddProduct(Activity activity) {
        if (activity != null) {
            activity.startActivity(new Intent(activity, ProductAddNameCategoryActivity.class));
        }
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
    public void goToUserPaymentList(Activity activity) {
        activity.startActivity(PaymentSettingInternalRouter.getSettingListPaymentActivityIntent(activity));
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
    public Intent getInboxMessageIntent(Context context) {
        return InboxChatActivity.getCallingIntent(context);
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
    public String applink(Activity activity, String deeplink) {
        return null;
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
    public Intent getSellerHomeIntent(Activity activity) {
        return DashboardActivity.createInstance(activity);
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

    public Intent getKolFollowingPageIntent(Context context, int userId) {
        return null;
    }

    @Override
    public Intent getChangePhoneNumberIntent(Context context, String email, String phoneNumber) {
        return ChangePhoneNumberWarningActivity.newInstance(context, email, phoneNumber);
    }

    @Override
    public Observable<TokoCashData> getTokoCashBalance() {
        return null;
    }

    @Override
    public void navigateToChooseAddressActivityRequest(Fragment var1, Intent var2, int var3) {
        Intent instance = ChooseAddressActivity.createInstance(var1.getContext());
        var1.startActivityForResult(instance, var3);
    }

    @Override
    public void navigateToEditAddressActivityRequest(final Fragment fragment, final int requestCode, Token token) {
        fragment.startActivityForResult(DistrictRecommendationActivity.createInstanceIntent(fragment.getActivity(),
                token),
                requestCode);
    }

    @Deprecated
    @Override
    public void sendEventTracking(String event, String category, String action, String label) {
        UnifyTracking.sendGTMEvent(getAppContext(), new EventTracking(event, category, action, label).getEvent());
    }

    @Override
    public void sendMoEngageOpenShopEventTracking(String screenName) {
        TrackingUtils.sendMoEngageCreateShopEvent(getAppContext(), screenName);
    }

    /**
     * Temporary Solution to send custom dimension for shop
     * should not pass the param, after tkpd common com in
     */
    @Override
    public void sendEventTrackingWithShopInfo(String event, String category, String action, String label,
                                              String shopId, boolean isGoldMerchant, boolean isOfficialStore) {
        UnifyTracking.sendGTMEvent(getAppContext(), new EventTracking(event, category, action, label)
                .setUserId(getSession().getUserId())
                .setShopId(shopId)
                .setShopType(isGoldMerchant, isOfficialStore).getEvent());
    }

    @Override
    public void goToCreateTopadsPromo(Context activity, String productId, String shopId, String source) {
        Intent intent = TopAdsCheckProductPromoActivity.createIntent(activity, shopId, productId, source);
        activity.startActivity(intent);
    }

    @Override
    public Intent getContactUsIntent(Context context) {
        return new Intent(context, ContactUsHomeActivity.class);
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
    public void showForceLogoutDialog(Response response) {
        ServerErrorHandler.showForceLogoutDialog();
        ServerErrorHandler.sendForceLogoutAnalytics(response.request().url().toString());
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
    public UserSession getSession() {
        return new UserSessionImpl(this);
    }

    @Override
    public AnalyticTracker getAnalyticTracker() {
        initAnalyticTracker();
        return analyticTracker;
    }

    private void initAnalyticTracker() {
        if (analyticTracker == null) {
            analyticTracker = new AnalyticTracker() {
                @Override
                public void sendEventTracking(Map<String, Object> events) {
                    SellerRouterApplication.this.sendEventTracking(events);
                }

                @Override
                public void sendEventTracking(String event, String category, String action, String label) {
                    UnifyTracking.sendGTMEvent(SellerRouterApplication.this, new EventTracking(
                            event,
                            category,
                            action,
                            label
                    ).getEvent());
                }

                @Override
                public void sendScreen(Activity activity, final String screenName) {
                    ScreenTracking.sendScreen(SellerRouterApplication.this, screenName);
                }

                @Override
                public void sendCustomScreen(Activity activity, String screenName, String shopID, String shopType, String pageType, String productId) {
                    if (activity != null && !TextUtils.isEmpty(screenName)) {
                        ScreenTracking.eventCustomScreen(activity, screenName, shopID,
                                shopType, pageType, productId);
                    }
                }

                @Override
                public void sendEnhancedEcommerce(Map<String, Object> trackingData) {
                    SellerRouterApplication.this.sendEnhanceECommerceTracking(trackingData);
                }
            };
        }
    }

    @Override
    public void sendEnhanceECommerceTracking(@NotNull Map<String, Object> events) {
        TrackingUtils.eventTrackingEnhancedEcommerce(this, events);
    }

    @Override
    public void sendTrackDefaultAuth() {
        ScreenTracking.sendAuth(this);
    }

    @Override
    public void sendTrackCustomAuth(@NotNull Context context, @NotNull String shopID,
                                    @NotNull String shopType, @NotNull String pageType,
                                    @NotNull String productId) {
        ScreenTracking.sendCustomAuth(this, shopID, shopType, pageType, productId);
    }

    @Override
    public Fragment getShopReputationFragmentShop(String shopId, String shopDomain) {
        return TkpdReputationInternalRouter.getReviewShopInfoFragment(shopId, shopDomain);
    }

    @Override
    public void goToShareShop(Activity activity, String shopId, String shopUrl, String shareLabel) {
        ShareData shareData = ShareData.Builder.aShareData()
                .setType(ShareData.SHOP_TYPE)
                .setName(getString(R.string.message_share_shop))
                .setTextContent(shareLabel)
                .setUri(shopUrl)
                .setId(shopId)
                .build();
        new DefaultShare(activity, shareData).show();
    }

    @Override
    public void goToManageShop(Context context) {
        context.startActivity(getIntentManageShop(context));
    }

    @Override
    public void goToEditShopNote(Context context) {
        Intent intent = ShopSettingsInternalRouter.getShopSettingsNotesActivity(context);
        context.startActivity(intent);
    }

    @Override
    public void goToAddProduct(Context context) {
        if (context != null && context instanceof Activity) {
            context.startActivity(new Intent(context, ProductAddNameCategoryActivity.class));
        }
    }

    @Override
    public void goToChatSeller(Context context, String shopId, String shopName, String avatar) {
        if (getSession().isLoggedIn()) {
            UnifyTracking.eventShopSendChat(context);
            Intent intent = getAskSellerIntent(this, shopId, shopName, TkpdInboxRouter.SHOP, avatar);
            context.startActivity(intent);
        } else {
            Intent intent = ((TkpdCoreRouter) MainApplication.getAppContext()).getLoginIntent(context);
            ((Activity) context).startActivityForResult(intent, 100);
        }
    }

    @Override
    public Intent getShopPageIntent(Context context, String shopId) {
        return ShopPageInternalRouter.getShopPageIntent(context, shopId);
    }

    @Override
    public void startSaldoDepositIntent(Context context) {
        SaldoDetailsInternalRouter.startSaldoDepositIntent(context);
    }

    @Override
    public Intent getShopPageIntentByDomain(Context context, String domain) {
        return ShopPageInternalRouter.getShopPageIntentByDomain(context, domain);
    }

    @Override
    public Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId) {
        return ShopPageInternalRouter.getShoProductListIntent(context, shopId, keyword, etalaseId);
    }

    public Intent getGroupChatIntent(Context context, String channelUrl) {
        return null;
    }

    @Override
    public Intent getInboxChannelsIntent(Context context) {
        return null;
    }

    @Override
    public Fragment getChannelFragment(Bundle bundle) {
        return null;
    }

    @Override
    public String getChannelFragmentTag() {
        return "";
    }


    @Override
    public void init() {
    }

    @Override
    public void unregisterShake() {
    }

    public Intent getDefaultContactUsIntent(Activity activity, String url, String toolbarTitle) {
        Intent intent = new Intent(activity, ContactUsActivity.class);
        intent.putExtra(InboxRouter.PARAM_URL,
                URLGenerator.generateURLContactUs(Uri.encode(url), activity));
        intent.putExtra(ContactUsActivity.EXTRAS_PARAM_TOOLBAR_TITLE, toolbarTitle);
        return intent;
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
    public Intent getProductDetailIntent(Context context, ProductPass productPass) {
        return ProductInfoActivity.createInstance(context, productPass);
    }

    @Override
    public Interceptor getChuckInterceptor() {
        return getAppComponent().chuckInterceptor();
    }

    @Override
    public void startAddProduct(Activity activity, String shopId) {
        goToAddProduct(activity);
    }

    @Override
    public void sendTrackingGroupChatLeftNavigation() {
    }

    @Override
    public String getDesktopLinkGroupChat() {
        return "";
    }

    @Override
    public String logisticUploadRouterGetApplicationBuildFlavor() {
        return BuildConfig.FLAVOR;
    }

    @Override
    public void gotoTopAdsDashboard(Context context) {
        context.startActivity(TopAdsDashboardActivity.Companion.getCallingIntent(context));
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
    public Intent getChangePhoneNumberRequestIntent(Context context, String userId, String oldPhoneNumber) {
        return ChangeInactiveFormRequestActivity.createIntentWithUserId(context, userId, oldPhoneNumber);
    }

    @Override
    public Intent getPromoListIntent(Activity activity) {
        return null;
    }

    @Override
    public Intent getPromoDetailIntent(Context context, String slug) {
        return null;
    }


    @Override
    public Intent getCartIntent(Activity activity) {
        return null;
    }

    @Override
    public void updateMarketplaceCartCounter(CartNotificationListener listener) {

    }

    @Override
    public Interceptor getAuthInterceptor() {
        return new TkpdAuthInterceptor();
    }


    public Intent getOrderListIntent(Context context) {
        return SellerOrderListActivity.getInstance(context);
    }

    @Override
    public void goToShopReview(Context context, String shopId, String shopDomain) {
        SessionHandler sessionHandler = new SessionHandler(this);
        ReputationTracking tracking = new ReputationTracking(this);
        tracking.eventClickSeeMoreReview(getString(R.string.review), shopId, sessionHandler.getShopID().equals(shopId));
        context.startActivity(ReviewShopInfoActivity.createIntent(context, shopId, shopDomain));
    }

    @Override
    public void goToManageShipping(Context context) {
        context.startActivity(new Intent(context, EditShippingActivity.class));
    }

    @Override
    public void goToEditShop(Context context) {
        Intent intent = ShopSettingsInternalRouter.getShopSettingsBasicInfoActivity(context);
        context.startActivity(intent);
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
        return  RouteManager.getIntent(context, ApplinkConst.CHATBOT
                .replace(String.format("{%s}",ApplinkConst.Chat.MESSAGE_ID), messageId));
    }

    @Override
    public void registerShake(String screenName, Activity activity) {

    }

    @Override
    public void openRedirectUrl(Activity activity, String url) {
        goToWebview(activity, url);
    }

    @Override
    public boolean isIndicatorVisible() {
        return false; //Sellerapp dont have groupchat therefore always set false to indicator
    }

    @Override
    public boolean isLoginInactivePhoneLinkEnabled() {
        return false;
    }

    @Override
    public Intent getSettingBankIntent(Context context) {
        return SettingBankActivity.Companion.createIntent(context);
    }

    @Override
    public String getStringRemoteConfig(String key) {
        return remoteConfig.getString(key, "");
    }

    @Override
    public void setStringRemoteConfigLocal(String key, String value) {
        remoteConfig.setString(key, value);
    }

    @Override
    public Intent getProfileSettingIntent(Context context) {
        return ManagePeopleProfileActivity.createIntent(context);
    }

    @Override
    public Intent getWithdrawIntent(Context context) {
        return WithdrawActivity.getCallingIntent(context);
    }

    @Override
    public void logoutToHome(Activity activity) {
        //From DialogLogoutFragment
        if (activity != null) {
            new GlobalCacheManager().deleteAll();
            Router.clearEtalase(activity);
            DbManagerImpl.getInstance().removeAllEtalase();
            TrackingUtils.eventMoEngageLogoutUser(activity);
            SessionHandler.clearUserData(activity);
            NotificationModHandler notif = new NotificationModHandler(activity);
            notif.dismissAllActivedNotifications();
            NotificationModHandler.clearCacheAllNotification(activity);

            invalidateCategoryMenuData();
            onLogout(getApplicationComponent());

            Intent intent = getHomeIntent(activity);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            AppWidgetUtil.sendBroadcastToAppWidget(activity);
        }
    }

    public Intent createIntentProductVariant(Context context, ArrayList<ProductVariantByCatModel> productVariantByCatModelList,
                                             ProductVariantViewModel productVariant, int productPriceCurrency, double productPrice,
                                             int productStock, boolean officialStore, String productSku,
                                             boolean needRetainImage, ProductPictureViewModel productSizeChart, boolean hasOriginalVariantLevel1,
                                             boolean hasOriginalVariantLevel2, boolean hasWholesale) {
        return ProductVariantDashboardActivity.getIntent(context, productVariantByCatModelList, productVariant,
                productPriceCurrency, productPrice, productStock, officialStore, productSku, needRetainImage, productSizeChart,
                hasOriginalVariantLevel1, hasOriginalVariantLevel2, hasWholesale);
    }

    @Override
    public Intent getManageProductIntent(Context context) {
        return new Intent(context, ProductManageActivity.class);
    }

    @Override
    public Intent createIntentProductEtalase(Context context, int etalaseId) {
        return EtalasePickerActivity.createInstance(context, etalaseId);
    }

    @Override
    public Intent getCategoryPickerIntent(Context context, int categoryId) {
        return CategoryPickerActivity.createIntent(context, categoryId);
    }

    @Override
    public Intent getProductTalk(Context context, String productId) {
        return TalkProductActivity.Companion.createIntent(context, productId);
    }

    @Override
    public Intent getShopTalkIntent(Context context, String shopId) {
        return ShopTalkActivity.Companion.createIntent(context, shopId);
    }

    @Override
    public void eventClickFilterReview(Context context,
                                       String filterName,
                                       String productId) {
        ProductPageTracking.eventClickFilterReview(
                context,
                filterName,
                productId
        );
    }

    @Override
    public void eventImageClickOnReview(Context context,
                                        String productId,
                                        String reviewId) {
        ProductPageTracking.eventClickImageOnReviewList(
                context,
                productId,
                reviewId
        );
    }

    @Override
    public void goToShopDiscussion(Context context, String shopId) {
        context.startActivity(ShopTalkActivity.Companion.createIntent(context, shopId));
    }

    @Override
    public Intent getTalkDetailIntent(Context context, String talkId, String shopId,
                                             String source) {
        return TalkDetailsActivity.getCallingIntent(talkId, shopId, context, source);
    }

    @Override
    public void setCartCount(Context context, int count) {

    }

    @Override
    public int getCartCount(Context context) {
        return 0;
    }

    @Override
    public void goToApplinkActivity(Activity activity, String applink, Bundle bundle) {

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

    @Override
    public @NonNull
    Intent getManageShopEtalaseIntent(@NonNull Context context) {
        return ShopSettingsInternalRouter.getShopSettingsEtalaseActivity(context);
    }

    @Override
    public @NonNull
    Intent getManageShopNotesIntent(@NonNull Context context) {
        return ShopSettingsInternalRouter.getShopSettingsNotesActivity(context);
    }

    @Override
    public @NonNull
    Intent getManageShopBasicDataIntent(@NonNull Context context) {
        return ShopSettingsInternalRouter.getShopSettingsBasicInfoActivity(context);
    }

    @Override
    public @NonNull
    Intent getManageShopLocationIntent(@NonNull Context context) {
        return ShopSettingsInternalRouter.getShopSettingsLocationActivity(context);
    }

    @NonNull
    @Override
    public Intent getDistrictRecommendationIntent(@NonNull Activity activity) {
        return DistrictRecommendationShopSettingsActivity.createInstance(activity);
    }

    @NonNull
    @Override
    public Intent getBroadcastMessageListIntent(@NonNull Context context) {
        sendEventTracking(BroadcastMessageConstant.VALUE_GTM_EVENT_NAME_INBOX,
                BroadcastMessageConstant.VALUE_GTM_EVENT_CATEGORY,
                BroadcastMessageConstant.VALUE_GTM_EVENT_ACTION_BM_CLICK,"");
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
    public Fragment getKolPostShopFragment(String shopId, String createPostUrl) {
        return KolPostShopFragment.newInstance(shopId, createPostUrl);
    }

    @Override
    public boolean isFeedShopPageEnabled() {
        return remoteConfig.getBoolean("sellerapp_enable_feed_shop_page", Boolean.TRUE);
    }

    @Override
    public String getResourceUrlAssetPayment() {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(getApplicationContext());
        String baseUrl = remoteConfig.getString(RemoteConfigKey.IMAGE_HOST,
                TkpdBaseURL.Payment.DEFAULT_HOST);

        final String resourceUrl = baseUrl + TkpdBaseURL.Payment.CDN_IMG_ANDROID_DOMAIN;
        return resourceUrl;
    }

    @Override
    public Intent getIntentOtpPageVerifCreditCard(Context context, String phoneNumber) {
        return VerificationActivity.getCallingIntent(context, phoneNumber, RequestOtpUseCase.OTP_TYPE_VERIFY_AUTH_CREDIT_CARD,
                false, RequestOtpUseCase.MODE_SMS);
    }

    @Override
    public Intent getProductPageIntent(Context context, String productId) {
        return ProductInfoActivity.createInstance(context, productId);
    }

    @Override
    public void instabugCaptureUserStep(Activity activity, MotionEvent me) {
        InstabugInitalize.dispatchTouchEvent(activity, me);
    }

    @Override
    public Intent getInboxTalkCallingIntent(@NonNull Context context) {
        return InboxTalkActivity.Companion.createIntent(context);
    }

    @Override
    public void onAppsFlyerInit() {

    }

    @Override
    public Intent getAutomaticResetPasswordIntent(Context context, String email) {
        return ForgotPasswordActivity.getAutomaticResetPasswordIntent(context, email);
    }

    @Override
    public void sendAFCompleteRegistrationEvent(int userId, String methodName) {

    }

    @Override
    public void sendMoEngageFavoriteEvent(String shopName, String shopID, String shopDomain, String shopLocation, boolean isShopOfficaial, boolean isFollowed) {
        TrackingUtils.sendMoEngageFavoriteEvent(getAppContext(), shopName, shopID, shopDomain, shopLocation, isShopOfficaial, isFollowed);
    }

    @Override
    public void setTrackingUserId(String userId, Context applicationContext) {
        onAppsFlyerInit();
        TrackingUtils.eventPushUserID(applicationContext, userId);
        if (!BuildConfig.DEBUG && Crashlytics.getInstance() != null)
            Crashlytics.setUserIdentifier(userId);
        BranchSdkUtils.sendIdentityEvent(userId);
        BranchSdkUtils.sendLoginEvent(applicationContext);
    }

    @Override
    public void setMoEUserAttributesLogin(String userId, String name, String email, String phoneNumber, boolean isGoldMerchant, String shopName, String shopId, boolean hasShop, String loginMethod) {
        TrackingUtils.setMoEUserAttributesLogin(getAppContext(),
                userId,
                name,
                email,
                phoneNumber,
                isGoldMerchant,
                shopName,
                shopId,
                hasShop,
                loginMethod
        );
    }

    @Override
    public void eventMoRegistrationStart(String label) {
        UnifyTracking.eventMoRegistrationStart(getAppContext(), label);
    }

    @Override
    public void eventMoRegister(String name, String phone) {
        UnifyTracking.eventMoRegister(getAppContext(), name, phone);

    }

    @Override
    public void sendBranchRegisterEvent(String email, String phone) {
        BranchSdkUtils.sendRegisterEvent(getAppContext(), email, phone);

    }

    @Override
    public Observable<AddToCartResult> addToCartProduct(AddToCartRequest addToCartRequest, boolean isOneClickShipment) {
        return null;
    }

    @NonNull
    @Override
    public Fragment getFavoritedShopFragment(@NonNull String userId) {
        return PeopleFavoritedShopFragment.createInstance(userId);
    }

    @NonNull
    @Override
    public Intent getTopAdsDetailShopIntent(@NonNull Context context) {
        return TopAdsManagementInternalRouter.getTopAdsDetailShopIntent(context);
    }

    @NonNull
    @Override
    public Intent getTopAdsKeywordListIntent(@NonNull Context context) {
        return TopAdsManagementInternalRouter.getTopAdsKeywordListIntent(context);
    }

    @NonNull
    @Override
    public Intent getTopAdsAddingPromoOptionIntent(@NonNull Context context) {
        return TopAdsManagementInternalRouter.getTopAdsAddingPromoOptionIntent(context);
    }

    @NonNull
    @Override
    public Intent getTopAdsProductAdListIntent(@NonNull Context context) {
        return TopAdsManagementInternalRouter.getTopAdsProductAdListIntent(context);
    }

    @NonNull
    @Override
    public Intent getTopAdsGroupAdListIntent(@NonNull Context context) {
        return TopAdsManagementInternalRouter.getTopAdsGroupAdListIntent(context);
    }

    @NonNull
    @Override
    public Intent getTopAdsGroupNewPromoIntent(@NonNull Context context) {
        return TopAdsManagementInternalRouter.getTopAdsGroupNewPromoIntent(context);
    }

    @NonNull
    @Override
    public Intent getTopAdsKeywordNewChooseGroupIntent(@NonNull Context context, boolean isPositive, String groupId) {
        return TopAdsManagementInternalRouter.getTopAdsKeywordNewChooseGroupIntent(context, isPositive, groupId);
    }

    @Override
    public void eventTopAdsProductClickProductDashboard() {
        UnifyTracking.eventTopAdsProductClickKeywordDashboard(getAppContext());
    }

    @Override
    public void eventTopAdsProductClickGroupDashboard() {
        UnifyTracking.eventTopAdsProductClickGroupDashboard(getAppContext());
    }

    @Override
    public void eventTopAdsProductAddBalance() {
        UnifyTracking.eventTopAdsProductAddBalance(getAppContext());
    }

    @Override
    public void eventTopAdsShopChooseDateCustom() {
        UnifyTracking.eventTopAdsShopChooseDateCustom(getAppContext());
    }

    @Override
    public void eventTopAdsShopDatePeriod(@NonNull String label) {
        UnifyTracking.eventTopAdsShopDatePeriod(getAppContext(), label);
    }

    @Override
    public void eventTopAdsProductStatisticBar(@NonNull String label) {
        UnifyTracking.eventTopAdsProductStatisticBar(getAppContext(), label);
    }

    @Override
    public void eventTopAdsShopStatisticBar(@NonNull String label) {
        UnifyTracking.eventTopAdsShopStatisticBar(getAppContext(), label);
    }

    @Override
    public void eventTopAdsProductClickKeywordDashboard() {
        UnifyTracking.eventTopAdsProductClickKeywordDashboard(getAppContext());
    }

    @Override
    public void eventOpenTopadsPushNotification(@NonNull String label) {
        UnifyTracking.eventOpenTopadsPushNotification(getAppContext(), label);
    }

    @Override
    public void showAdvancedAppRatingDialog(Activity activity, DialogInterface.OnDismissListener dismissListener) {

    }

    @Override
    public void showSimpleAppRatingDialog(Activity activity) {

    }

    @Override
    public boolean isAllowLogOnChuckInterceptorNotification() {
        LocalCacheHandler cache = new LocalCacheHandler(this, DeveloperOptions.CHUCK_ENABLED);
        return cache.getBoolean(DeveloperOptions.IS_CHUCK_ENABLED, false);
    }

    @Override
    public Intent navigateToGeoLocationActivityRequest(Context context, com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass locationPass) {
        return GeolocationActivity.createInstance(context, locationPass, false);
    }

    @Override
    @NonNull
    public Intent getTopAdsDashboardIntent(@NonNull Context context) {
        return TopAdsDashboardInternalRouter.getTopAdsdashboardIntent(context);
    }

    @Override
    @NonNull
    public Intent getTopAdsAddCreditIntent(@NonNull Context context) {
        return TopAdsDashboardInternalRouter.getTopAdsAddCreditIntent(context);
    }

    @Override
    public void openTopAdsDashboardApplink(Context context) {
    }

    @Override
    public boolean getBooleanRemoteConfig(String key, boolean defaultValue) {
        return remoteConfig.getBoolean(key, defaultValue);
    }

    @Override
    public String getBranchAutoApply(Activity activity) {
        return null;
    }

    @Override
    public String getTrackingClientId() {
        return null;
    }

    @Override
    public Intent getDealDetailIntent(Activity activity, String slug, boolean enableBuy, boolean enableRecommendation, boolean enableShare, boolean enableLike) {
        return null;
    }

    @Override
    public Intent getLoyaltyActivitySelectedCoupon(Context context, String digitalString, String categoryId) {
        return null;
    }

    @Override
    public Intent getLoyaltyActivity(Context context, String platform, String categoryId) {
        return null;
    }

    @Override
    public Intent getLoyaltyActivityNoCouponActive(Context context, String platform, String categoryId) {
        return null;
    }

    @Override
    public String getContactUsBaseURL() {
        return TkpdBaseURL.ContactUs.URL_HELP;
    }

    @Override
    public Class getDeeplinkClass() {
        return DeepLinkActivity.class;
    }

    @Override
    public void refereshFcmTokenToCMNotif(String token) {

    }

    @Override
    public void onLoginSuccess() {

    }

    @Override
    public boolean isSaldoNativeEnabled() {
        return remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.SALDO_PRIORITAS_NATIVE_ANDROID,
                true);
    }

    @Override
    public Intent getSaldoDepositIntent(Context context) {
        return null;
    }

    @Override
    public void refereshFcmTokenToCMNotif(String token) {

    }

    @Override
    public Intent getMaintenancePageIntent() {
        return MaintenancePage.createIntentFromNetwork(getAppContext());
    }

    @Override
    public boolean isSaldoNativeEnabled() {
        return false;
    }

    @Override
    public String getContactUsBaseURL() {
        return TkpdBaseURL.ContactUs.URL_HELP;
    }

    public void onLoginSuccess() {
    }

    @Override
    public void getDynamicShareMessage(Context dataObj, ActionCreator<String, Integer> actionCreator, ActionUIDelegate<String, String> actionUIDelegate){
    }

    @Override
    public Intent getCheckoutIntent(Activity activity) {
        return null;
    }
}