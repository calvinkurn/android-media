package com.tokopedia.tkpd.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.tkpd.library.utils.legacy.AnalyticsLog;
import com.tkpd.library.utils.legacy.SessionAnalytics;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.Actions.interfaces.ActionCreator;
import com.tokopedia.abstraction.Actions.interfaces.ActionDataProvider;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.analytics.mapper.TkpdAppsFlyerMapper;
import com.tokopedia.analytics.mapper.TkpdAppsFlyerRouter;
import com.tokopedia.analyticsdebugger.debugger.TetraDebugger;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkDelegate;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.ApplinkUnsupported;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.cacheapi.domain.interactor.CacheApiClearAllUseCase;
import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.common_digital.common.constant.DigitalCache;
import com.tokopedia.core.MaintenancePage;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.util.AccessTokenRefresh;
import com.tokopedia.core.util.PasswordGenerator;
import com.tokopedia.core.util.SessionRefresh;
import com.tokopedia.developer_options.config.DevOptConfig;
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator;
import com.tokopedia.fingerprint.util.FingerprintConstant;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.homecredit.view.fragment.FragmentCardIdCamera;
import com.tokopedia.homecredit.view.fragment.FragmentSelfieIdCamera;
import com.tokopedia.iris.Iris;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.kyc.KYCRouter;
import com.tokopedia.linker.interfaces.LinkerRouter;
import com.tokopedia.loyalty.di.component.TokopointComponent;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.notifications.CMPushNotificationManager;
import com.tokopedia.notifications.inApp.CMInAppManager;
import com.tokopedia.notifications.inApp.viewEngine.CmInAppConstant;
import com.tokopedia.oms.OmsModuleRouter;
import com.tokopedia.oms.di.DaggerOmsComponent;
import com.tokopedia.oms.di.OmsComponent;
import com.tokopedia.oms.domain.PostVerifyCartWrapper;
import com.tokopedia.promogamification.common.GamificationRouter;
import com.tokopedia.promotionstarget.presentation.GratifCmInitializer;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.tkpd.ConsumerSplashScreen;
import com.tokopedia.tkpd.applink.ApplinkUnsupportedImpl;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.fcm.AppNotificationReceiver;
import com.tokopedia.tkpd.nfc.NFCSubscriber;
import com.tokopedia.tkpd.react.DaggerReactNativeComponent;
import com.tokopedia.tkpd.react.ReactNativeComponent;
import com.tokopedia.tkpd.utils.DeferredResourceInitializer;
import com.tokopedia.tkpd.utils.GQLPing;
import com.tokopedia.tkpdreactnative.react.ReactUtils;
import com.tokopedia.tkpdreactnative.react.creditcard.domain.CreditCardFingerPrintUseCase;
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
import java.util.List;

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
        AbstractionRouter,
        ApplinkRouter,
        LoyaltyModuleRouter,
        GamificationRouter,
        ReactNativeRouter,
        NetworkRouter,
        OmsModuleRouter,
        TkpdAppsFlyerRouter,
        LinkerRouter,
        KYCRouter {

    private static final String ENABLE_ASYNC_CMPUSHNOTIF_INIT = "android_async_cmpushnotif_init";
    private static final String ENABLE_ASYNC_IRIS_INIT = "android_async_iris_init";
    private static final String ADD_BROTLI_INTERCEPTOR = "android_add_brotli_interceptor";
    protected CacheManager cacheManager;
    @Inject
    Lazy<ReactNativeHost> reactNativeHost;
    @Inject
    Lazy<ReactUtils> reactUtils;
    private DaggerReactNativeComponent.Builder daggerReactNativeBuilder;
    private OmsComponent omsComponent;
    private ReactNativeComponent reactNativeComponent;
    private TokopointComponent tokopointComponent;
    private TetraDebugger tetraDebugger;
    private Iris mIris;

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

    private void warmUpGQLClient() {
        if (remoteConfig.getBoolean(RemoteConfigKey.EXECUTE_GQL_CONNECTION_WARM_UP, false)) {
            try {
                GQLPing gqlPing = GraphqlClient.getRetrofit().create(GQLPing.class);
                Call<String> gqlPingCall = gqlPing.pingGQL();
                gqlPingCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        if (response != null && response.body() != null) {
                            Timber.d("Success %s", response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Timber.d("Failure");
                    }
                });
            } catch (Exception ex) {
                Timber.d("GQL Ping exception %s", ex.getMessage());
            }
        }
    }

    private void performLibraryInitialisation() {
        WeaveInterface initWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return initLibraries();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(initWeave, ENABLE_ASYNC_CMPUSHNOTIF_INIT, ConsumerRouterApplication.this);
    }

    private boolean initLibraries() {
        initCMPushNotification();
        initTetraDebugger();
        initCMDependencies();
        return true;
    }

    private void initCMDependencies(){
        GratifCmInitializer.INSTANCE.start(this);
    }

    private void initialiseHansel() {
        WeaveInterface hanselWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return executeHanselInit();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(hanselWeave, RemoteConfigKey.ENABLE_ASYNC_HANSEL_INIT, getApplicationContext());
    }

    private boolean executeHanselInit() {
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

    private boolean executeIrisInitialize() {
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
    public boolean getEnableFingerprintPayment() {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(this);
        return remoteConfig.getBoolean(FingerprintConstant.ENABLE_FINGERPRINT_MAINAPP);
    }

    @Override
    public Interceptor getChuckerInterceptor() {
        return getAppComponent().ChuckerInterceptor();
    }

    @Override
    public NotificationPass setNotificationPass(Context mContext, NotificationPass mNotificationPass,
                                                Bundle data, String notifTitle) {
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(getInboxReputationIntent(this));
        mNotificationPass.classParentStack = getHomeClass();
        mNotificationPass.title = notifTitle;
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        return mNotificationPass;
    }

    private Intent getInboxReputationIntent(Context context) {
        return RouteManager.getIntent(context, ApplinkConst.REPUTATION);
    }

    @Override
    public void onLogout(AppComponent appComponent) {

        forceLogout();

        PersistentCacheManager.instance.delete(DigitalCache.NEW_DIGITAL_CATEGORY_AND_FAV);
        new CacheApiClearAllUseCase(this).executeSync();
    }

    private void forceLogout() {
        PasswordGenerator.clearTokenStorage(context);
        TrackApp.getInstance().getMoEngage().logoutEvent();
        UserSessionInterface userSession = new UserSession(context);
        userSession.logoutSession();
    }

    public Intent getHomeIntent(Context context) {
        return RouteManager.getIntent(context, ApplinkConst.HOME);
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
        if (daggerReactNativeBuilder == null) {
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
        forceLogout();
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
    public void sendForceLogoutAnalytics(String url, boolean isInvalidToken,
                                         boolean isRequestDenied) {
        ServerErrorHandler.sendForceLogoutAnalytics(url,
                isInvalidToken, isRequestDenied);
    }

    @Override
    public void showForceLogoutTokenDialog(String path) {
        ServerErrorHandler.showForceLogoutDialog(path);
        ServerErrorHandler.sendForceLogoutTokenAnalytics(path);
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

    @Override
    public CacheManager getPersistentCacheManager() {
        if (cacheManager == null) {
            cacheManager = new PersistentCacheManager(this);
        }
        return cacheManager;
    }

    @Override
    public void logInvalidGrant(Response response) {
        AnalyticsLog.logInvalidGrant(this, response.request().url().toString());
    }

    @Override
    public Observable<TKPDMapParam<String, Object>> verifyDealPromo(com.tokopedia.usecase.RequestParams requestParams) {
        if (omsComponent == null) {
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
    public boolean getBooleanRemoteConfig(String key, boolean defaultValue) {
        return remoteConfig.getBoolean(key, defaultValue);
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

    private boolean executeAppflyerInit() {
        TkpdAppsFlyerMapper.getInstance(ConsumerRouterApplication.this).mapAnalytics();
        return true;
    }

    @Override
    public boolean isAllowLogOnChuckInterceptorNotification() {
        return DevOptConfig.isChuckNotifEnabled(this);
    }

    private void initCMPushNotification() {
        CMPushNotificationManager.getInstance().init(ConsumerRouterApplication.this);
        List<String> excludeScreenList = new ArrayList<>();
        excludeScreenList.add(CmInAppConstant.ScreenListConstants.SPLASH);
        excludeScreenList.add(CmInAppConstant.ScreenListConstants.DEEPLINK_ACTIVITY);
        excludeScreenList.add(CmInAppConstant.ScreenListConstants.DEEPLINK_HANDLER_ACTIVITY);
        CMInAppManager.getInstance().setExcludeScreenList(excludeScreenList);
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
    public Intent getInboxTalkCallingIntent(Context mContext) {
        return null;
    }

    @Override
    public void sendRefreshTokenAnalytics(String errorMessage) {
        if(TextUtils.isEmpty(errorMessage)){
            SessionAnalytics.trackRefreshTokenSuccess();
        }else {
            SessionAnalytics.trackRefreshTokenFailed(errorMessage);
        }
    }
}
