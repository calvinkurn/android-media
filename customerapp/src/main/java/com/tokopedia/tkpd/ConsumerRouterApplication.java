package com.tokopedia.tkpd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.play.core.inappreview.InAppReviewInfo;
import com.google.android.play.core.inappreview.InAppReviewManager;
import com.google.android.play.core.inappreview.InAppReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.legacy.AnalyticsLog;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.Actions.interfaces.ActionCreator;
import com.tokopedia.abstraction.Actions.interfaces.ActionDataProvider;
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.analytics.mapper.TkpdAppsFlyerMapper;
import com.tokopedia.analytics.mapper.TkpdAppsFlyerRouter;
import com.tokopedia.analyticsdebugger.debugger.TetraDebugger;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkDelegate;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.ApplinkUnsupported;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.buyerorder.common.util.UnifiedOrderListRouter;
import com.tokopedia.buyerorder.others.CreditCardFingerPrintUseCase;
import com.tokopedia.cacheapi.domain.interactor.CacheApiClearAllUseCase;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.common_digital.common.constant.DigitalCache;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.MaintenancePage;
import com.tokopedia.core.analytics.AnalyticsEventTrackingHelper;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.util.AccessTokenRefresh;
import com.tokopedia.core.util.AppWidgetUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.SessionRefresh;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity;
import com.tokopedia.events.EventModuleRouter;
import com.tokopedia.events.di.DaggerEventComponent;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.di.EventModule;
import com.tokopedia.feedplus.view.fragment.FeedPlusContainerFragment;
import com.tokopedia.fingerprint.util.FingerprintConstant;
import com.tokopedia.flight.orderlist.view.fragment.FlightOrderListFragment;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.home.HomeInternalRouter;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.analytics.data.model.UserAttributeData;
import com.tokopedia.home.account.di.AccountHomeInjection;
import com.tokopedia.home.account.di.AccountHomeInjectionImpl;
import com.tokopedia.homecredit.view.fragment.FragmentCardIdCamera;
import com.tokopedia.homecredit.view.fragment.FragmentSelfieIdCamera;
import com.tokopedia.inbox.common.ResolutionRouter;
import com.tokopedia.inbox.rescenter.create.activity.CreateResCenterActivity;
import com.tokopedia.iris.Iris;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.kyc.KYCRouter;
import com.tokopedia.linker.interfaces.LinkerRouter;
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomActivity;
import com.tokopedia.logisticaddaddress.features.manage.ManagePeopleAddressActivity;
import com.tokopedia.logisticaddaddress.features.pinpoint.GeolocationActivity;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.loyalty.di.component.TokopointComponent;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.activity.LoyaltyActivity;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import com.tokopedia.navigation.GlobalNavRouter;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.notifications.CMPushNotificationManager;
import com.tokopedia.notifications.CMRouter;
import com.tokopedia.nps.presentation.view.dialog.AppFeedbackRatingBottomSheet;
import com.tokopedia.nps.presentation.view.dialog.SimpleAppRatingDialog;
import com.tokopedia.officialstore.category.presentation.fragment.OfficialHomeContainerFragment;
import com.tokopedia.oms.OmsModuleRouter;
import com.tokopedia.oms.di.DaggerOmsComponent;
import com.tokopedia.oms.di.OmsComponent;
import com.tokopedia.oms.domain.PostVerifyCartWrapper;
import com.tokopedia.phoneverification.PhoneVerificationRouter;
import com.tokopedia.phoneverification.view.activity.PhoneVerificationActivationActivity;
import com.tokopedia.promogamification.common.GamificationRouter;
import com.tokopedia.purchase_platform.common.constant.CartConstant;
import com.tokopedia.cart.view.CartFragment;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.seller.LogisticRouter;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.common.logout.TkpdSellerLogout;
import com.tokopedia.seller.product.etalase.utils.EtalaseUtils;
import com.tokopedia.seller.reputation.view.fragment.SellerReputationFragment;
import com.tokopedia.seller.shop.common.di.component.DaggerShopComponent;
import com.tokopedia.seller.shop.common.di.component.ShopComponent;
import com.tokopedia.seller.shop.common.di.module.ShopModule;
import com.tokopedia.seller.shopsettings.shipping.EditShippingActivity;
import com.tokopedia.seller.purchase.detail.activity.OrderHistoryActivity;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.tkpd.applink.ApplinkUnsupportedImpl;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.fcm.AppNotificationReceiver;
import com.tokopedia.tkpd.fcm.appupdate.FirebaseRemoteAppUpdate;
import com.tokopedia.tkpd.home.analytics.HomeAnalytics;
import com.tokopedia.tkpd.nfc.NFCSubscriber;
import com.tokopedia.tkpd.react.DaggerReactNativeComponent;
import com.tokopedia.tkpd.react.ReactNativeComponent;
import com.tokopedia.tkpd.redirect.RedirectCreateShopActivity;
import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;
import com.tokopedia.tkpd.tkpdreputation.TkpdReputationInternalRouter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationActivity;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.ReviewShopFragment;
import com.tokopedia.tkpd.utils.DeferredResourceInitializer;
import com.tokopedia.tkpd.utils.FingerprintModelGenerator;
import com.tokopedia.tkpdreactnative.react.ReactUtils;
import com.tokopedia.tkpdreactnative.react.di.ReactNativeModule;
import com.tokopedia.tkpdreactnative.router.ReactNativeRouter;
import com.tokopedia.track.TrackApp;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.Lazy;
import io.hansel.hanselsdk.Hansel;
import okhttp3.Interceptor;
import okhttp3.Response;
import rx.Observable;
import timber.log.Timber;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;
import static com.tokopedia.kyc.Constants.Keys.KYC_CARDID_CAMERA;
import static com.tokopedia.kyc.Constants.Keys.KYC_SELFIEID_CAMERA;
import static com.tokopedia.remoteconfig.RemoteConfigKey.APP_ENABLE_SALDO_SPLIT;
import static com.tokopedia.tkpd.thankyou.view.ReactNativeThankYouPageActivity.CACHE_KEY_HAS_SHOWN_IN_APP_REVIEW_BEFORE;
import static com.tokopedia.tkpd.thankyou.view.ReactNativeThankYouPageActivity.CACHE_THANK_YOU_PAGE;


/**
 * @author normansyahputa on 12/15/16.
 */
public abstract class ConsumerRouterApplication extends MainApplication implements
        TkpdCoreRouter,
        SellerModuleRouter,
        ReactApplication,
        ReputationRouter,
        AbstractionRouter,
        LogisticRouter,
        ApplinkRouter,
        ShopModuleRouter,
        LoyaltyModuleRouter,
        GamificationRouter,
        ReactNativeRouter,
        NetworkRouter,
        GlobalNavRouter,
        AccountHomeRouter,
        OmsModuleRouter,
        EventModuleRouter,
        PhoneVerificationRouter,
        TkpdAppsFlyerRouter,
        UnifiedOrderListRouter,
        LinkerRouter,
        DigitalRouter,
        CMRouter,
        ResolutionRouter,
        KYCRouter {

    @Inject
    Lazy<ReactNativeHost> reactNativeHost;
    @Inject
    Lazy<ReactUtils> reactUtils;

    private DaggerReactNativeComponent.Builder daggerReactNativeBuilder;
    private EventComponent eventComponent;
    private OmsComponent omsComponent;
    private DaggerShopComponent.Builder daggerShopBuilder;
    private ShopComponent shopComponent;
    private ReactNativeComponent reactNativeComponent;
    private TokopointComponent tokopointComponent;

    private CacheManager cacheManager;

    private TetraDebugger tetraDebugger;
    private Iris mIris;

    @Override
    public void onCreate() {
        super.onCreate();
        initialiseHansel();
        initFirebase();
        GraphqlClient.init(getApplicationContext());
        NetworkClient.init(getApplicationContext());
        initCMPushNotification();
        initIris();
        initTetraDebugger();
        DeeplinkHandlerActivity.createApplinkDelegateInBackground();
        initResourceDownloadManager();
    }

    private void initialiseHansel(){
        WeaveInterface hanselWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return executeHanselInit();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(hanselWeave, RemoteConfigKey.ENABLE_ASYNC_HANSEL_INIT, getApplicationContext());
    }

    private boolean executeHanselInit(){
        Hansel.init(ConsumerRouterApplication.this);
        return true;
    }

    private void initResourceDownloadManager() {
        (new DeferredResourceInitializer()).initializeResourceDownloadManager(context);
    }

    private void initDaggerInjector() {
        getReactNativeComponent().inject(this);
    }

    private void initIris() {
        mIris = IrisAnalytics.Companion.getInstance(this);
        mIris.initialize();
    }

    private void initTetraDebugger() {
        if (com.tokopedia.config.GlobalConfig.isAllowDebuggingTools()) {
            tetraDebugger = TetraDebugger.Companion.instance(context);
            tetraDebugger.init();
        }
    }

    private void setTetraUserId(String userId) {
        if (tetraDebugger != null) {
            tetraDebugger.setUserId(userId);
        }
    }


    private void initFirebase() {
        if (com.tokopedia.config.GlobalConfig.DEBUG) {
            try {
                FirebaseOptions.Builder builder = new FirebaseOptions.Builder();
                builder.setApplicationId("1:692092518182:android:f4cc247c743f7921");
                FirebaseApp.initializeApp(this, builder.build());
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ShopComponent getShopComponent() {
        if (shopComponent == null) {
            if(daggerShopBuilder == null){
                daggerShopBuilder = DaggerShopComponent.builder().shopModule(new ShopModule());
            }
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
    public Intent getSplashScreenIntent(Context context) {
        return new Intent(context, ConsumerSplashScreen.class);
    }

    @Override
    public void onNewIntent(Context context, Intent intent) {
        NFCSubscriber.onNewIntent(context, intent);
    }

    /**
     * Use {@link com.tokopedia.applink.RouteManager} or {@link ApplinkRouter#isSupportApplink(String)}
     *
     * @param appLinks
     * @return
     */
    @Deprecated
    public boolean isSupportedDelegateDeepLink(String appLinks) {
        return isSupportApplink(appLinks);
    }

    public Intent getIntentDeepLinkHandlerActivity() {
        return new Intent(this, DeeplinkHandlerActivity.class);
    }

    @Override
    public String getGeneratedOverrideRedirectUrlPayment(String originUrl) {
        Uri originUri = Uri.parse(originUrl);
        Uri.Builder uriBuilder = Uri.parse(originUrl).buildUpon();
        if (!originUri.isOpaque()) {
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
        return remoteConfig.getBoolean(FingerprintConstant.ENABLE_FINGERPRINT_MAINAPP);
    }

    @Override
    public void resetAddProductCache(Context context) {
        EtalaseUtils.clearEtalaseCache(context);
        EtalaseUtils.clearDepartementCache(context);
    }

    @Override
    public void goToCreateMerchantRedirect(Context context) {
        Intent intent = RedirectCreateShopActivity.getCallingIntent(context);
        context.startActivity(intent);
    }

    @Override
    public Interceptor getChuckerInterceptor() {
        return getAppComponent().ChuckerInterceptor();
    }


    @Override
    public Intent getTrainOrderListIntent(Context context) {
        String WEB_DOMAIN = TokopediaUrl.Companion.getInstance().getTIKET();
        String KAI_WEBVIEW = WEB_DOMAIN + "kereta-api";
        String PATH_USER_BOOKING_LIST = "/user/bookings";
        String PARAM_DIGITAL_ISPULSA = "?ispulsa=1";
        String TRAIN_ORDER_LIST = KAI_WEBVIEW + PATH_USER_BOOKING_LIST + PARAM_DIGITAL_ISPULSA;
        return RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW, TRAIN_ORDER_LIST);
    }

    @Override
    public void sendAnalyticsUserAttribute(UserAttributeData userAttributeData) {
        HomeAnalytics.setUserAttribute(this, userAttributeData);
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

    private Intent getInboxReputationIntent(Context context) {
        return TkpdReputationInternalRouter.getInboxReputationActivityIntent(context);
    }

    @Override
    public Fragment getReputationHistoryFragment() {
        return SellerReputationFragment.createInstance();
    }

    @Override
    public Fragment getReviewSellerFragment() {
        return null;
    }

    @Override
    public Intent getOrderHistoryIntent(Context context, String orderId) {
        return OrderHistoryActivity.createInstance(context, orderId, 1);
    }

    @Override
    public void onLogout(AppComponent appComponent) {
        SessionHandler sessionHandler = new SessionHandler(this);
        sessionHandler.forceLogout();
        PersistentCacheManager.instance.delete(DigitalCache.NEW_DIGITAL_CATEGORY_AND_FAV);
        new CacheApiClearAllUseCase(this).executeSync();
        TkpdSellerLogout.onLogOut(appComponent, this);
    }

    @Override
    public Intent getRegisterIntent(Context context) {
        return RegisterInitialActivity.getCallingIntent(context);
    }

    public Intent getHomeIntent(Context context) {
        return MainParentActivity.start(context);
    }

    @Override
    public Intent getPhoneVerificationActivityIntent(Context context) {
        return PhoneVerificationActivationActivity.getIntent(context, false, false);
    }

    @Override
    public Class<?> getHomeClass() {
        return MainParentActivity.class;
    }

    @Override
    public void sendLoginEmitter(String userId) {
        reactUtils.get().sendLoginEmitter(userId);
    }

    private ReactNativeComponent getReactNativeComponent() {
        if(daggerReactNativeBuilder == null){
            daggerReactNativeBuilder = DaggerReactNativeComponent.builder()
                    .appComponent(getApplicationComponent())
                    .reactNativeModule(new ReactNativeModule(ConsumerRouterApplication.this));
        }
        if (reactNativeComponent == null)
            reactNativeComponent = daggerReactNativeBuilder.build();
        return reactNativeComponent;
    }

    @Override
    public ReactNativeHost getReactNativeHost() {
        if (reactNativeHost == null) initDaggerInjector();
        return reactNativeHost.get();
    }

    @Override
    public Intent getCreateResCenterActivityIntent(Context context, String orderId) {
        return CreateResCenterActivity.getCreateResCenterActivityIntent(context, orderId);
    }

    @Override
    public void onForceLogout(Activity activity) {
        SessionHandler sessionHandler = new SessionHandler(activity);
        sessionHandler.forceLogout();
        Intent intent = ((TkpdCoreRouter) getBaseContext().getApplicationContext()).getSplashScreenIntent(getBaseContext());
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
    public void sendForceLogoutAnalytics(Response response, boolean isInvalidToken,
                                         boolean isRequestDenied) {
        ServerErrorHandler.sendForceLogoutAnalytics(response.request().url().toString(),
                isInvalidToken, isRequestDenied);
    }

    @Override
    public void showForceLogoutTokenDialog(String response) {
        ServerErrorHandler.showForceLogoutDialog();
        ServerErrorHandler.sendForceLogoutTokenAnalytics(response);
    }

    @Override
    public void sendAnalyticsAnomalyResponse(String title,
                                             String accessToken, String refreshToken,
                                             String response, String request) {
        Timber.w("P2#USER_ANOMALY_REPONSE#AnomalyResponse;title=" + title +
                ";accessToken=" + accessToken + ";refreshToken=" + refreshToken +
                ";response=" + response + ";request=" + request);
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

    /**
     * User PersistentCacheManager Library directly
     */
    @Deprecated
    @Override
    public CacheManager getGlobalCacheManager() {
        if (cacheManager == null) {
            cacheManager = new GlobalCacheManager();
        }
        return cacheManager;
    }

    @Override
    public Intent getDistrictRecommendationIntent(Activity activity, Token token) {
        return DiscomActivity.newInstance(activity, token);
    }

    @Override
    public Intent getGeoLocationActivityIntent(Context context, LocationPass locationPass) {
        return GeolocationActivity.createInstance(context, locationPass, false);
    }

    @Override
    public Intent getPhoneVerificationActivationIntent(Context context) {
        return PhoneVerificationActivationActivity.getCallingIntent(context);
    }

    @Override
    public Intent tkpdCartCheckoutGetLoyaltyOldCheckoutCouponActiveIntent(
            Context context, String platform, String category, String defaultSelectedTab) {
        return LoyaltyActivity.newInstanceCouponActive(context, platform, category, defaultSelectedTab);
    }

    @Override
    public Fragment getReviewFragment(Activity activity, String shopId, String shopDomain) {
        return ReviewShopFragment.createInstance(shopId, shopDomain);
    }

    @Override
    public Class getReviewFragmentClass() {
        return ReviewShopFragment.class;
    }

    @Override
    public void logInvalidGrant(Response response) {
        AnalyticsLog.logInvalidGrant(this, legacyGCMHandler(), legacySessionHandler(), response.request().url().toString());
    }

    @Override
    public Observable<TKPDMapParam<String, Object>> verifyEventPromo(com.tokopedia.usecase.RequestParams requestParams) {
        boolean isEventOMS = remoteConfig.getBoolean("event_oms_android", false);
        if(eventComponent == null){
            eventComponent = DaggerEventComponent.builder()
                    .baseAppComponent((this).getBaseAppComponent())
                    .eventModule(new EventModule(this))
                    .build();
        }
        if (!isEventOMS) {
            return eventComponent.getVerifyCartWrapper().verifyPromo(requestParams);
        } else {
            return new PostVerifyCartWrapper(this, eventComponent.getPostVerifyCartUseCase())
                    .verifyPromo(requestParams);
        }
    }

    @Override
    public Observable<TKPDMapParam<String, Object>> verifyDealPromo(com.tokopedia.usecase.RequestParams requestParams) {
        if(omsComponent == null){
            omsComponent = DaggerOmsComponent.builder()
                    .baseAppComponent((ConsumerRouterApplication.this).getBaseAppComponent())
                    .build();
        }
        return new PostVerifyCartWrapper(this, omsComponent.getPostVerifyCartUseCase())
                .verifyPromo(requestParams);
    }

    @Override
    public boolean isSupportApplink(String appLink) {
        return DeeplinkHandlerActivity.getApplinkDelegateInstance().supportsUri(appLink);
    }

    @Override
    public ApplinkUnsupported getApplinkUnsupported(Activity activity) {
        return new ApplinkUnsupportedImpl(activity);
    }

    @Override
    public ApplinkDelegate applinkDelegate() {
        return DeeplinkHandlerActivity.getApplinkDelegateInstance();
    }

    @Override
    public void setCartCount(Context context, int count) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, CartConstant.CART);
        localCacheHandler.putInt(CartConstant.CACHE_TOTAL_CART, count);
        localCacheHandler.applyEditor();
    }

    @Override
    public int getCartCount(Context context) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, CartConstant.CART);
        return localCacheHandler.getInt(CartConstant.CACHE_TOTAL_CART, 0);
    }

    @Override
    public void goToApplinkActivity(Context context, String applink) {
        if (context != null) {
            if (context instanceof Activity) {
                goToApplinkActivity((Activity) context, applink, new Bundle());
            } else {
                Intent intent = new Intent(context, DeeplinkHandlerActivity.class);
                intent.setData(Uri.parse(applink));
                context.startActivity(intent);
            }
        }
    }

    @Override
    public void goToApplinkActivity(Activity activity, String applink, Bundle bundle) {
        if (activity != null) {
            ApplinkDelegate deepLinkDelegate = DeeplinkHandlerActivity.getApplinkDelegateInstance();
            Intent intent = activity.getIntent();
            intent.setData(Uri.parse(applink));
            intent.putExtras(bundle);
            deepLinkDelegate.dispatchFrom(activity, intent);
        }
    }

    @Override
    public Intent getApplinkIntent(Context context, String applink) {
        Intent intent = new Intent(context, DeeplinkHandlerActivity.class);
        intent.setData(Uri.parse(applink));

        if (context instanceof Activity) {
            try {
                return DeeplinkHandlerActivity.getApplinkDelegateInstance().getIntent((Activity) context, applink);
            } catch (Exception e) {

            }
        }

        return intent;
    }

    @Override
    public Observable<VoucherViewModel> checkTrainVoucher(String trainReservationId,
                                                          String trainReservationCode,
                                                          String galaCode) {
        return Observable.just(new VoucherViewModel());
    }

    public UseCase<String> setCreditCardSingleAuthentication() {
        return new CreditCardFingerPrintUseCase();
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
    public Fragment getHomeFragment(boolean scrollToRecommendList) {
        return HomeInternalRouter.getHomeFragment(scrollToRecommendList);
    }

    @Override
    public Fragment getFeedPlusFragment(Bundle bundle) {
        return FeedPlusContainerFragment.newInstance(bundle);
    }

    @Override
    public Fragment getCartFragment(Bundle bundle) {
        return CartFragment.newInstance(bundle, CartFragment.class.getSimpleName());
    }

    @Override
    public Fragment getOfficialStoreFragment(Bundle bundle) {
        return OfficialHomeContainerFragment.newInstance(bundle);
    }

    @Override
    public Intent getManageAddressIntent(Context context) {
        return new Intent(context, ManagePeopleAddressActivity.class);
    }

    @Override
    public void goToManageShopShipping(Context context) {
        UnifyTracking.eventManageShopShipping(context);
        context.startActivity(new Intent(context, EditShippingActivity.class));
    }

    @Override
    public void goToManageShopProduct(Context context) {
        Intent intent = RouteManager.getIntent(context, ApplinkConst.PRODUCT_MANAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void goToSaldo(Context context) {

        if (remoteConfig.getBoolean(APP_ENABLE_SALDO_SPLIT, false)) {
            RouteManager.route(context, ApplinkConstInternalGlobal.SALDO_DEPOSIT);
        } else {
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW,
                    ApplinkConst.WebViewUrl.SALDO_DETAIL));
        }

        AnalyticsEventTrackingHelper.homepageSaldoClick(getApplicationContext(),
                "com.tokopedia.saldodetails.activity.SaldoDepositActivity");
    }

    @Override
    public ApplicationUpdate getAppUpdate(Context context) {
        return new FirebaseRemoteAppUpdate(context);
    }

    @Override
    public String getStringRemoteConfig(String key, String defaultValue) {
        return remoteConfig.getString(key, defaultValue);
    }

    @Override
    public long getLongRemoteConfig(String key, long defaultValue) {
        return remoteConfig.getLong(key, defaultValue);
    }

    @Override
    public boolean getBooleanRemoteConfig(String key, boolean defaultValue) {
        return remoteConfig.getBoolean(key, defaultValue);
    }

    @Override
    public void sendOpenHomeEvent() {
        UserSessionInterface userSession = new UserSession(context);
        Map<String, Object> value = DataLayer.mapOf(
                AppEventTracking.MOENGAGE.LOGIN_STATUS, userSession.isLoggedIn()
        );
        TrackApp.getInstance().getMoEngage().sendTrackEvent(value, AppEventTracking.EventMoEngage.OPEN_BERANDA);
    }

    @Override
    public AccountHomeInjection getAccountHomeInjection() {
        return new AccountHomeInjectionImpl();
    }

    public void doLogoutAccount(Context activity) {
        new GlobalCacheManager().deleteAll();
        PersistentCacheManager.instance.delete();
        EtalaseUtils.clearEtalaseCache(getApplicationContext());
        TrackApp.getInstance().getMoEngage().logoutEvent();
        SessionHandler.clearUserData(activity);
        NotificationModHandler notif = new NotificationModHandler(activity);
        notif.dismissAllActivedNotifications();
        NotificationModHandler.clearCacheAllNotification(activity);

        Intent intent = getHomeIntent(activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        AppWidgetUtil.sendBroadcastToAppWidget(activity);
        refreshFCMTokenFromForegroundToCM();

        setTetraUserId("");
    }

    @Override
    public Fragment getFlightOrderListFragment() {
        return FlightOrderListFragment.createInstance();
    }

    @Override
    public boolean isEnableInterestPick() {
        return remoteConfig.getBoolean("mainapp_enable_interest_pick", Boolean.TRUE);
    }

    @Override
    public String getAppsFlyerID() {
        return TrackingUtils.getAfUniqueId(this);
    }

    public void onAppsFlyerInit() {
        WeaveInterface appsflyerInitWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return executeAppflyerInit();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(appsflyerInitWeave, RemoteConfigKey.ENABLE_ASYNC_APPSFLYER_INIT, getApplicationContext());
    }

    private boolean executeAppflyerInit(){
        TkpdAppsFlyerMapper.getInstance(ConsumerRouterApplication.this).mapAnalytics();
        return true;
    }

    @Override
    public boolean isAllowLogOnChuckInterceptorNotification() {
        LocalCacheHandler cache = new LocalCacheHandler(this, DeveloperOptionActivity.CHUCK_ENABLED);
        return cache.getBoolean(DeveloperOptionActivity.IS_CHUCK_ENABLED, false);
    }

    @Override
    public void showAppFeedbackRatingDialog(
            FragmentManager manager,
            Context context,
            BottomSheets.BottomSheetDismissListener dismissListener
    ) {
        AppFeedbackRatingBottomSheet rating = new AppFeedbackRatingBottomSheet();
        rating.setDialogDismissListener(dismissListener);
        rating.showDialog(manager, context);
    }

    @Override
    public void showSimpleAppRatingDialog(Activity activity) {
        //this code needs to be improved in the future
        try {
            boolean hasShownInAppReviewBefore = getInAppReviewHasShownBefore();
            boolean enableInAppReview = remoteConfig.getBoolean(RemoteConfigKey.ENABLE_IN_APP_REVIEW_DIGITAL_THANKYOU_PAGE, false);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && enableInAppReview && !hasShownInAppReviewBefore) {
                launchInAppReview(activity);
            } else {
                SimpleAppRatingDialog.show(activity);
            }
        } catch (Exception e) {

        }
    }

    private boolean getInAppReviewHasShownBefore() {
        LocalCacheHandler cacheHandler = new LocalCacheHandler(this, CACHE_THANK_YOU_PAGE);
        return cacheHandler.getBoolean(CACHE_KEY_HAS_SHOWN_IN_APP_REVIEW_BEFORE);
    }

    private void setInAppReviewHasShownBefore() {
        LocalCacheHandler cacheHandler = new LocalCacheHandler(this, CACHE_THANK_YOU_PAGE);
        cacheHandler.putBoolean(CACHE_KEY_HAS_SHOWN_IN_APP_REVIEW_BEFORE, true);
        cacheHandler.applyEditor();
    }

    private void launchInAppReview(Activity activity) {
        InAppReviewManager inAppReviewManager = InAppReviewManagerFactory.create(activity);
        inAppReviewManager.requestInAppReviewFlow().addOnCompleteListener(new OnCompleteListener<InAppReviewInfo>() {
            @Override
            public void onComplete(Task<InAppReviewInfo> request) {
                if (request.isSuccessful()) {
                    inAppReviewManager.launchInAppReviewFlow(activity, request.getResult()).addOnCompleteListener(new OnCompleteListener<Integer>() {
                        @Override
                        public void onComplete(Task<Integer> task) {
                            setInAppReviewHasShownBefore();
                        }
                    });
                }
            }
        });
    }

    private void initCMPushNotification() {
        CMPushNotificationManager.getInstance().init(this);
        refreshFCMTokenFromBackgroundToCM(FCMCacheManager.getRegistrationId(this), false);
    }

    @Override
    public void refreshFCMTokenFromBackgroundToCM(String token, boolean force) {
        CMPushNotificationManager.getInstance().refreshTokenFromBackground(token, force);
    }

    @Override
    public void refreshFCMFromInstantIdService(String token) {
        CMPushNotificationManager.getInstance().refreshFCMTokenFromForeground(token, true);
    }

    @Override
    public void refreshFCMTokenFromForegroundToCM() {
        CMPushNotificationManager.getInstance().refreshFCMTokenFromForeground(FCMCacheManager.getRegistrationId(this), true);
    }

    @Override
    public Intent getMaintenancePageIntent() {
        return MaintenancePage.createIntentFromNetwork(getAppContext());
    }

    @SuppressLint("MissingPermission")
    @Override
    public BaseDaggerFragment getKYCCameraFragment(ActionCreator<HashMap<String, Object>, Integer> actionCreator,
                                                   ActionDataProvider<ArrayList<String>, Object> keysListProvider, int cameraType) {
        Bundle bundle = new Bundle();
        BaseDaggerFragment baseDaggerFragment = null;
        switch (cameraType) {
            case KYC_CARDID_CAMERA:
                baseDaggerFragment = FragmentCardIdCamera.newInstance();
                bundle.putSerializable(FragmentCardIdCamera.ACTION_CREATOR_ARG, actionCreator);
                bundle.putSerializable(FragmentCardIdCamera.ACTION_KEYS_PROVIDER_ARG, keysListProvider);
                baseDaggerFragment.setArguments(bundle);
                break;
            case KYC_SELFIEID_CAMERA:
                baseDaggerFragment = new FragmentSelfieIdCamera();
                bundle.putSerializable(FragmentSelfieIdCamera.ACTION_CREATOR_ARG, actionCreator);
                bundle.putSerializable(FragmentSelfieIdCamera.ACTION_KEYS_PROVIDER_ARG, keysListProvider);
                baseDaggerFragment.setArguments(bundle);
                break;
        }
        return baseDaggerFragment;
    }

    @Override
    public IAppNotificationReceiver getAppNotificationReceiver() {
        return new AppNotificationReceiver();
    }

    @Override
    public Intent getInboxTalkCallingIntent(Context mContext){
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
}
