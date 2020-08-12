package com.tokopedia.tkpd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.google.android.gms.tagmanager.DataLayer;
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
import com.tokopedia.applink.ApplinkDelegate;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.ApplinkUnsupported;
import com.tokopedia.buyerorder.common.util.UnifiedOrderListRouter;
import com.tokopedia.buyerorder.others.CreditCardFingerPrintUseCase;
import com.tokopedia.cacheapi.domain.interactor.CacheApiClearAllUseCase;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.cart.view.CartFragment;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.common_digital.common.constant.DigitalCache;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.MaintenancePage;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.util.AccessTokenRefresh;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.SessionRefresh;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.developer_options.config.DevOptConfig;
import com.tokopedia.feedplus.view.fragment.FeedPlusContainerFragment;
import com.tokopedia.fingerprint.util.FingerprintConstant;
import com.tokopedia.flight.orderlist.view.fragment.FlightOrderListFragment;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.home.HomeInternalRouter;
import com.tokopedia.homecredit.view.fragment.FragmentCardIdCamera;
import com.tokopedia.homecredit.view.fragment.FragmentSelfieIdCamera;
import com.tokopedia.iris.Iris;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.kyc.KYCRouter;
import com.tokopedia.linker.interfaces.LinkerRouter;
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomActivity;
import com.tokopedia.logisticaddaddress.features.pinpoint.GeolocationActivity;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.loyalty.di.component.TokopointComponent;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import com.tokopedia.navigation.GlobalNavRouter;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.notifications.CMPushNotificationManager;
import com.tokopedia.notifications.CMRouter;
import com.tokopedia.nps.helper.InAppReviewHelper;
import com.tokopedia.nps.presentation.view.dialog.AppFeedbackRatingBottomSheet;
import com.tokopedia.nps.presentation.view.dialog.SimpleAppRatingDialog;
import com.tokopedia.officialstore.category.presentation.fragment.OfficialHomeContainerFragment;
import com.tokopedia.oms.OmsModuleRouter;
import com.tokopedia.oms.di.DaggerOmsComponent;
import com.tokopedia.oms.di.OmsComponent;
import com.tokopedia.oms.domain.PostVerifyCartWrapper;
import com.tokopedia.phoneverification.PhoneVerificationRouter;
import com.tokopedia.promogamification.common.GamificationRouter;
import com.tokopedia.purchase_platform.common.constant.CartConstant;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.seller.LogisticRouter;
import com.tokopedia.seller.product.etalase.utils.EtalaseUtils;
import com.tokopedia.seller.purchase.detail.activity.OrderHistoryActivity;
import com.tokopedia.seller.shop.common.di.component.DaggerShopComponent;
import com.tokopedia.seller.shop.common.di.component.ShopComponent;
import com.tokopedia.tkpd.applink.ApplinkUnsupportedImpl;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.fcm.AppNotificationReceiver;
import com.tokopedia.tkpd.fcm.appupdate.FirebaseRemoteAppUpdate;
import com.tokopedia.tkpd.nfc.NFCSubscriber;
import com.tokopedia.tkpd.react.DaggerReactNativeComponent;
import com.tokopedia.tkpd.react.ReactNativeComponent;
import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;
import com.tokopedia.tkpd.tkpdreputation.TkpdReputationInternalRouter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationActivity;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.ReviewShopFragment;
import com.tokopedia.tkpd.utils.DeferredResourceInitializer;
import com.tokopedia.tkpd.utils.FingerprintModelGenerator;
import com.tokopedia.tkpd.utils.GQLPing;
import com.tokopedia.tkpdreactnative.react.ReactUtils;
import com.tokopedia.tkpdreactnative.react.di.ReactNativeModule;
import com.tokopedia.tkpdreactnative.router.ReactNativeRouter;
import com.tokopedia.track.TrackApp;
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
import retrofit2.Call;
import retrofit2.Callback;
import rx.Observable;
import timber.log.Timber;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;
import static com.tokopedia.kyc.Constants.Keys.KYC_CARDID_CAMERA;
import static com.tokopedia.kyc.Constants.Keys.KYC_SELFIEID_CAMERA;


/**
 * @author normansyahputa on 12/15/16.
 */
public abstract class ConsumerRouterApplication extends MainApplication implements
        TkpdCoreRouter,
        ReactApplication,
        ReputationRouter,
        AbstractionRouter,
        LogisticRouter,
        ApplinkRouter,
        LoyaltyModuleRouter,
        GamificationRouter,
        ReactNativeRouter,
        NetworkRouter,
        GlobalNavRouter,
        OmsModuleRouter,
        PhoneVerificationRouter,
        TkpdAppsFlyerRouter,
        UnifiedOrderListRouter,
        LinkerRouter,
        DigitalRouter,
        CMRouter,
        KYCRouter {

    @Inject
    Lazy<ReactNativeHost> reactNativeHost;
    @Inject
    Lazy<ReactUtils> reactUtils;

    private DaggerReactNativeComponent.Builder daggerReactNativeBuilder;
    private OmsComponent omsComponent;
    private DaggerShopComponent.Builder daggerShopBuilder;
    private ShopComponent shopComponent;
    private ReactNativeComponent reactNativeComponent;
    private TokopointComponent tokopointComponent;

    private CacheManager cacheManager;

    private TetraDebugger tetraDebugger;
    private Iris mIris;
    private static final String ENABLE_ASYNC_CMPUSHNOTIF_INIT = "android_async_cmpushnotif_init";
    private static final String ENABLE_ASYNC_IRIS_INIT = "android_async_iris_init";


    @Override
    public void onCreate() {
        super.onCreate();
        initialiseHansel();
        initFirebase();
        GraphqlClient.init(getApplicationContext());
        NetworkClient.init(getApplicationContext());
        warmUpGQLClient();
        initIris();
        performLibraryInitialisation();
        DeeplinkHandlerActivity.createApplinkDelegateInBackground(ConsumerRouterApplication.this);
        initResourceDownloadManager();
    }

    private void warmUpGQLClient(){
        if(remoteConfig.getBoolean(RemoteConfigKey.EXECUTE_GQL_CONNECTION_WARM_UP, false)) {
            try{
            GQLPing gqlPing = GraphqlClient.getRetrofit().create(GQLPing.class);
            Call<String> gqlPingCall = gqlPing.pingGQL();
            gqlPingCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    if(response != null && response.body() != null) {
                        Timber.d("Success %s", response.body());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Timber.d("Failure");
                }
            });
            } catch (Exception ex){
                Timber.d("GQL Ping exception %s", ex.getMessage());
            }
        }
    }

    private void performLibraryInitialisation(){
        WeaveInterface initWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return initLibraries();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(initWeave, ENABLE_ASYNC_CMPUSHNOTIF_INIT, ConsumerRouterApplication.this);
    }

    private boolean initLibraries(){
        initCMPushNotification();
        initTetraDebugger();
        return true;
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
        mIris = IrisAnalytics.Companion.getInstance(ConsumerRouterApplication.this);
        WeaveInterface irisInitializeWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return executeIrisInitialize();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(irisInitializeWeave, ENABLE_ASYNC_IRIS_INIT, ConsumerRouterApplication.this);
    }

    private boolean executeIrisInitialize(){
        mIris.initialize();
        return true;
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
    public Interceptor getChuckerInterceptor() {
        return getAppComponent().ChuckerInterceptor();
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
    public Intent getOrderHistoryIntent(Context context, String orderId) {
        return OrderHistoryActivity.createInstance(context, orderId, 1);
    }

    @Override
    public void onLogout(AppComponent appComponent) {
        SessionHandler sessionHandler = new SessionHandler(this);
        sessionHandler.forceLogout();
        PersistentCacheManager.instance.delete(DigitalCache.NEW_DIGITAL_CATEGORY_AND_FAV);
        new CacheApiClearAllUseCase(this).executeSync();
    }

    public Intent getHomeIntent(Context context) {
        return MainParentActivity.start(context);
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
    public void logInvalidGrant(Response response) {
        AnalyticsLog.logInvalidGrant(this, legacyGCMHandler(), legacySessionHandler(), response.request().url().toString());
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
    public ApplicationUpdate getAppUpdate(Context context) {
        return new FirebaseRemoteAppUpdate(context);
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
    public Fragment getFlightOrderListFragment() {
        return FlightOrderListFragment.createInstance();
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
        return DevOptConfig.isChuckNotifEnabled(this);
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
        if(!InAppReviewHelper.launchInAppReview(activity)) {
            SimpleAppRatingDialog.show(activity);
        }
    }

    private void initCMPushNotification() {
        CMPushNotificationManager.getInstance().init(ConsumerRouterApplication.this);
        refreshFCMTokenFromBackgroundToCM(FCMCacheManager.getRegistrationId(ConsumerRouterApplication.this), false);
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
}
