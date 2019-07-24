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

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.legacy.AnalyticsLog;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.Actions.interfaces.ActionCreator;
import com.tokopedia.abstraction.Actions.interfaces.ActionUIDelegate;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkDelegate;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.ApplinkUnsupported;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.broadcast.message.BroadcastMessageInternalRouter;
import com.tokopedia.broadcast.message.common.BroadcastMessageRouter;
import com.tokopedia.broadcast.message.common.constant.BroadcastMessageConstant;
import com.tokopedia.cacheapi.domain.interactor.CacheApiClearAllUseCase;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.changepassword.ChangePasswordRouter;
import com.tokopedia.changephonenumber.view.activity.ChangePhoneNumberWarningActivity;
import com.tokopedia.chatbot.ChatbotRouter;
import com.tokopedia.contactus.ContactUsModuleRouter;
import com.tokopedia.contactus.createticket.activity.ContactUsActivity;
import com.tokopedia.contactus.home.view.ContactUsHomeActivity;
import com.tokopedia.contactus.inboxticket2.view.activity.InboxListActivity;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.MaintenancePage;
import com.tokopedia.core.Router;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
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
import com.tokopedia.core.network.CoreNetworkRouter;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.peoplefave.fragment.PeopleFavoritedShopFragment;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.share.DefaultShare;
import com.tokopedia.core.util.AccessTokenRefresh;
import com.tokopedia.core.util.AppWidgetUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.SessionRefresh;
import com.tokopedia.cpm.CharacterPerMinuteInterface;
import com.tokopedia.fingerprint.util.FingerprintConstant;
import com.tokopedia.flashsale.management.router.FlashSaleInternalRouter;
import com.tokopedia.flashsale.management.router.FlashSaleRouter;
import com.tokopedia.gm.GMModuleRouter;
import com.tokopedia.gm.common.di.component.DaggerGMComponent;
import com.tokopedia.gm.common.di.component.GMComponent;
import com.tokopedia.gm.common.di.module.GMModule;
import com.tokopedia.gm.subscribe.GMSubscribeInternalRouter;
import com.tokopedia.gm.subscribe.GmSubscribeModuleRouter;
import com.tokopedia.imageuploader.ImageUploaderRouter;
import com.tokopedia.inbox.common.ResolutionRouter;
import com.tokopedia.inbox.rescenter.create.activity.CreateResCenterActivity;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.DetailResChatActivity;
import com.tokopedia.inbox.rescenter.inbox.activity.InboxResCenterActivity;
import com.tokopedia.inbox.rescenter.inboxv2.view.activity.ResoInboxActivity;
import com.tokopedia.linker.interfaces.LinkerRouter;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.loginregister.login.view.activity.LoginActivity;
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DistrictRecommendationActivity;
import com.tokopedia.logisticaddaddress.features.manage.ManagePeopleAddressActivity;
import com.tokopedia.logisticaddaddress.features.pinpoint.GeolocationActivity;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.logisticuploadawb.ILogisticUploadAwbRouter;
import com.tokopedia.merchantvoucher.MerchantVoucherModuleRouter;
import com.tokopedia.mitratoppers.MitraToppersRouter;
import com.tokopedia.mitratoppers.MitraToppersRouterInternal;
import com.tokopedia.mlp.router.MLPRouter;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.network.service.AccountsService;
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
import com.tokopedia.saldodetails.router.SaldoDetailsInternalRouter;
import com.tokopedia.saldodetails.router.SaldoDetailsRouter;
import com.tokopedia.seller.LogisticRouter;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.TkpdSeller;
import com.tokopedia.seller.common.logout.TkpdSellerLogout;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;
import com.tokopedia.seller.product.draft.view.activity.ProductDraftListActivity;
import com.tokopedia.seller.product.etalase.utils.EtalaseUtils;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantDashboardActivity;
import com.tokopedia.seller.purchase.detail.activity.OrderDetailActivity;
import com.tokopedia.seller.reputation.view.fragment.SellerReputationFragment;
import com.tokopedia.seller.shop.common.di.component.DaggerShopComponent;
import com.tokopedia.seller.shop.common.di.component.ShopComponent;
import com.tokopedia.seller.shop.common.di.module.ShopModule;
import com.tokopedia.sellerapp.dashboard.view.activity.DashboardActivity;
import com.tokopedia.sellerapp.deeplink.DeepLinkActivity;
import com.tokopedia.sellerapp.deeplink.DeepLinkDelegate;
import com.tokopedia.sellerapp.deeplink.DeepLinkHandlerActivity;
import com.tokopedia.sellerapp.drawer.DrawerSellerHelper;
import com.tokopedia.sellerapp.utils.FingerprintModelGenerator;
import com.tokopedia.sellerapp.webview.SellerappWebViewActivity;
import com.tokopedia.sellerapp.welcome.WelcomeActivity;
import com.tokopedia.session.addchangeemail.view.activity.AddEmailActivity;
import com.tokopedia.session.addchangepassword.view.activity.AddPasswordActivity;
import com.tokopedia.session.changename.view.activity.ChangeNameActivity;
import com.tokopedia.session.forgotpassword.activity.ForgotPasswordActivity;
import com.tokopedia.settingbank.banklist.view.activity.SettingBankActivity;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.ShopPageInternalRouter;
import com.tokopedia.shop.open.ShopOpenRouter;
import com.tokopedia.talk.common.TalkRouter;
import com.tokopedia.talk.inboxtalk.view.activity.InboxTalkActivity;
import com.tokopedia.talk.producttalk.view.activity.TalkProductActivity;
import com.tokopedia.talk.shoptalk.view.activity.ShopTalkActivity;
import com.tokopedia.talk.talkdetails.view.activity.TalkDetailsActivity;
import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;
import com.tokopedia.tkpd.tkpdreputation.TkpdReputationInternalRouter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationActivity;
import com.tokopedia.topads.TopAdsComponentInstance;
import com.tokopedia.topads.TopAdsManagementInternalRouter;
import com.tokopedia.topads.TopAdsManagementRouter;
import com.tokopedia.topads.TopAdsModuleRouter;
import com.tokopedia.topads.auto.router.TopAdsAutoRouter;
import com.tokopedia.topads.common.TopAdsWebViewRouter;
import com.tokopedia.topads.dashboard.TopAdsDashboardInternalRouter;
import com.tokopedia.topads.dashboard.TopAdsDashboardRouter;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.domain.interactor.GetDepositTopAdsUseCase;
import com.tokopedia.topads.dashboard.view.activity.TopAdsCheckProductPromoActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity;
import com.tokopedia.topchat.attachproduct.view.activity.BroadcastMessageAttachProductActivity;
import com.tokopedia.topchat.chatlist.activity.InboxChatActivity;
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity;
import com.tokopedia.topchat.common.TopChatRouter;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.transaction.common.TransactionRouter;
import com.tokopedia.transaction.common.sharedata.AddToCartRequest;
import com.tokopedia.transaction.common.sharedata.AddToCartResult;
import com.tokopedia.transaction.common.sharedata.ShipmentFormRequest;
import com.tokopedia.transaction.orders.UnifiedOrderListRouter;
import com.tokopedia.transaction.orders.orderlist.view.activity.SellerOrderListActivity;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.withdraw.WithdrawRouter;
import com.tokopedia.withdraw.view.activity.WithdrawActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Response;
import rx.Observable;

import static com.tokopedia.core.analytics.AppEventTracking.Event.CLICK_PDP;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;
import static com.tokopedia.remoteconfig.RemoteConfigKey.APP_ENABLE_SALDO_SPLIT_FOR_SELLER_APP;

/**
 * Created by normansyahputa on 12/15/16.
 */

public abstract class SellerRouterApplication extends MainApplication
        implements TkpdCoreRouter, SellerModuleRouter, PdpRouter, GMModuleRouter, TopAdsModuleRouter,
        IPaymentModuleRouter, IDigitalModuleRouter, TkpdInboxRouter, TransactionRouter,
        ReputationRouter, LogisticRouter, ProfileModuleRouter,
        MitraToppersRouter, AbstractionRouter, ShopModuleRouter,
        ApplinkRouter, ImageUploaderRouter, ILogisticUploadAwbRouter,
        NetworkRouter, TopChatRouter, TopAdsWebViewRouter, ContactUsModuleRouter,
        ChangePasswordRouter, WithdrawRouter, GmSubscribeModuleRouter,
        PaymentSettingRouter, TalkRouter, PhoneVerificationRouter,
        TopAdsDashboardRouter,
        TopAdsManagementRouter,
        TopAdsAutoRouter,
        BroadcastMessageRouter,
        MerchantVoucherModuleRouter,
        UnifiedOrderListRouter,
        com.tokopedia.product.detail.ProductDetailRouter,
        CoreNetworkRouter,
        ChatbotRouter,
        SaldoDetailsRouter,
        FlashSaleRouter,
        LinkerRouter,
        CharacterPerMinuteInterface,
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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
    public void resetAddProductCache(Context context) {
        EtalaseUtils.clearEtalaseCache(context);
        EtalaseUtils.clearDepartementCache(context);
    }

    @Override
    public void goToWallet(Context context, String url) {
        //no route to wallet on seller, go to default
        goToDefaultRoute(context);
    }

    public Intent getMitraToppersActivityIntent(Context context) {
        return MitraToppersRouterInternal.getMitraToppersActivityIntent(context);
    }

    @Override
    public void shareFeed(Activity activity, String detailId, String url, String title, String
            imageUrl, String description) {
        LinkerData shareData = LinkerData.Builder.getLinkerBuilder()
                .setId(detailId)
                .setName(title)
                .setDescription(description)
                .setImgUri(imageUrl)
                .setUri(url)
                .setType(LinkerData.FEED_TYPE)
                .build();
        new DefaultShare(activity, shareData).show();
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
        new CacheApiClearAllUseCase(this).executeSync();

        TkpdSellerLogout.onLogOut(appComponent);
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

    public void sendScreenName(@NonNull String screenName) {
        ScreenTracking.screen(this, screenName);
    }

    public void goToWebview(Context context, String url) {
        Intent intent = new Intent(this, BannerWebView.class);
        intent.putExtra(BannerWebView.EXTRA_URL, url);
        context.startActivity(intent);
    }

    @Override
    public void openImagePreview(Context context, ArrayList<String> images,
                                 int position) {

    }

    private void goToDefaultRoute(Context context) {
        Intent intent = DashboardActivity.createInstance(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
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
                customMessage, source, avatar);
    }


    @NonNull
    @Override
    public Intent getAskSellerIntent(@NonNull Context context, @NonNull String toShopId,
                                     @NonNull String shopName, @NonNull String customSubject,
                                     @NonNull String customMessage, @NonNull String source,
                                     @NonNull String avatar) {

        return TopChatRoomActivity.getAskSellerIntent(context, toShopId, shopName,
                customMessage, source, avatar);

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
    public void goToGMSubscribe(@NonNull Activity activity) {
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

    public GetShopInfoUseCase getShopInfo() {
        return getShopComponent().getShopInfoUseCase();
    }

    @Override
    public void goToAddProduct(Activity activity) {
        if (activity != null) {
            activity.startActivity(new Intent(activity, ProductAddNameCategoryActivity.class));
        }
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
    public Intent getDistrictRecommendationIntent(Activity activity, Token token) {
        return DistrictRecommendationActivity.newInstance(activity, token);
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
    public Intent getShopPageIntent(Context context, String shopId) {
        return ShopPageInternalRouter.getShopPageIntent(context, shopId);
    }

    @Override
    public void startSaldoDepositIntent(Context context) {
        if (remoteConfig.getBoolean(APP_ENABLE_SALDO_SPLIT_FOR_SELLER_APP, false)) {
            SaldoDetailsInternalRouter.startSaldoDepositIntent(context);
        } else {
            context.startActivity(SellerappWebViewActivity.createIntent(context, ApplinkConst.WebViewUrl.SALDO_DETAIL));
        }
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
    public Interceptor getChuckInterceptor() {
        return getAppComponent().chuckInterceptor();
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
        return SellerappWebViewActivity.createIntent(context, webviewUrl);
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
    public void logoutToHome(Activity activity) {
        //From DialogLogoutFragment
        if (activity != null) {
            new GlobalCacheManager().deleteAll();
            PersistentCacheManager.instance.delete();
            Router.clearEtalase(activity);
            DbManagerImpl.getInstance().removeAllEtalase();
            try {
                TrackApp.getInstance().getMoEngage().logoutEvent();
            } catch (Exception ignored) {}
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
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                CLICK_PDP,
                "product detail page",
                String.format(
                        "click - filter review by %s",
                        filterName.toLowerCase()
                ),
                productId
        ));
    }

    @Override
    public void eventImageClickOnReview(Context context,
                                        String productId,
                                        String reviewId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                CLICK_PDP,
                "product detail page",
                "click - review gallery on review list",
                String.format(
                        "product_id: %s - review_id : %s",
                        productId,
                        reviewId
                )
        ));
    }

    @Override
    public Intent getTalkDetailIntent(Context context, String talkId, String shopId,
                                      String source) {
        return TalkDetailsActivity.getCallingIntent(talkId, shopId, context, source);
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
    public Intent getWithdrawIntent(Context context, boolean isSeller) {
        return WithdrawActivity.getCallingIntent(context, isSeller);
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
    public Intent getAutomaticResetPasswordIntent(Context context, String email) {
        return ForgotPasswordActivity.getAutomaticResetPasswordIntent(context, email);
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
    public Intent getGeoLocationActivityIntent(Context context, com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass locationPass) {
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
        Intent topadsIntent = context.getPackageManager()
                .getLaunchIntentForPackage(GlobalConfig.PACKAGE_SELLER_APP);

        if (topadsIntent != null) {
            goToApplinkActivity(context, ApplinkConst.SellerApp.TOPADS_DASHBOARD);
        } else {
            goToCreateMerchantRedirect(context);
        }
    }

    @Override
    public boolean getBooleanRemoteConfig(String key, boolean defaultValue) {
        return remoteConfig.getBoolean(key, defaultValue);
    }

    /*@Override
    public String getContactUsBaseURL() {
        return TkpdBaseURL.ContactUs.URL_HELP;
    }*/

    @Override
    public Class getDeeplinkClass() {
        return DeepLinkActivity.class;
    }

    @Override
    public Intent getMaintenancePageIntent() {
        return MaintenancePage.createIntentFromNetwork(getAppContext());
    }

    @Override
    public boolean isSaldoNativeEnabled() {
        return remoteConfig.getBoolean(RemoteConfigKey.SALDO_PRIORITAS_NATIVE_ANDROID,
                true);
    }

    public void onLoginSuccess() {
    }

    @Override
    public void getDynamicShareMessage(Context dataObj, ActionCreator<String, Integer> actionCreator, ActionUIDelegate<String, String> actionUIDelegate) {
    }

    @Override
    public Intent getCheckoutIntent(Context context, ShipmentFormRequest shipmentFormRequest) {
        return null;
    }

    @Override
    public Intent getCheckoutIntent(Context context, String deviceid) {
        return null;
    }

    @Override
    public boolean isMerchantCreditLineEnabled() {
        return false;
    }

    @Override
    public String getDeviceId(Context context) {
        return "";
    }

    @Override
    public Intent getExpressCheckoutIntent(Activity activity,
                                           com.tokopedia.transactiondata.entity.shared.expresscheckout.AtcRequestParam atcRequestParam) {
        return null;
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
    public com.tokopedia.iris.Iris getIris() {
        return null;
    }

    public String getContactUsBaseURL() {
        return TkpdBaseURL.ContactUs.URL_HELP;
    }

    @Override
    public Intent getCreateResCenterActivityIntent(Context context, String orderId) {
        return CreateResCenterActivity.getCreateResCenterActivityIntent(context, orderId);
    }

    @Override
    public Intent getCreateResCenterActivityIntent(Context context, String orderId, int troubleId, int solutionId) {
        return CreateResCenterActivity.getCreateResCenterActivityIntent(context, orderId, troubleId, solutionId);
    }

    @Override
    public void sendForceLogoutAnalytics(Response response, boolean isInvalidToken,
                                         boolean isRequestDenied) {
        ServerErrorHandler.sendForceLogoutAnalytics(response.request().url().toString(), isInvalidToken, isRequestDenied);
    }

    @Override
    public Intent getInboxTicketCallingIntent(Context context) {
        return new Intent(context, InboxListActivity.class);
    }

    @Override
    public void saveCPM(@NonNull String cpm) {
    }

    @Override
    public String getCPM() {
        return "";
    }

    @Override
    public boolean isEnable() {
        return false;
    }


    @Override
    public Intent transactionOrderDetailRouterGetIntentUploadAwb(String urlUpload) {
        return null;
    }

    @Override
    public boolean isToggleBuyAgainOn() {
        return false;
    }

    @Override
    public Intent getOrderHistoryIntent(Context context, String orderId) {
        return null;
    }

    @Override
    public Intent getLoginIntent(Context context) {
        return LoginActivity.DeepLinkIntents.getCallingIntent(context);
    }
}