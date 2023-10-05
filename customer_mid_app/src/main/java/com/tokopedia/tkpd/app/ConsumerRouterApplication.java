package com.tokopedia.tkpd.app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.TaskStackBuilder;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tkpd.library.utils.legacy.AnalyticsLog;
import com.tkpd.library.utils.legacy.SessionAnalytics;
import com.tokopedia.tokochat.config.util.TokoChatConnection;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.analytics.mapper.TkpdAppsFlyerMapper;
import com.tokopedia.analytics.mapper.TkpdAppsFlyerRouter;
import com.tokopedia.app.common.MainApplication;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.ApplinkUnsupported;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.common.ui.MaintenancePage;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.network.CoreNetworkApplication;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.util.AccessTokenRefresh;
import com.tokopedia.core.util.SessionRefresh;
import com.tokopedia.developer_options.config.DevOptConfig;
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator;
import com.tokopedia.fcmcommon.FirebaseMessagingManager;
import com.tokopedia.fcmcommon.FirebaseMessagingManagerImpl;
import com.tokopedia.fcmcommon.SendTokenToCMHandler;
import com.tokopedia.fcmcommon.common.FcmCacheHandler;
import com.tokopedia.fcmcommon.domain.SendTokenToCMUseCase;
import com.tokopedia.fcmcommon.domain.UpdateFcmTokenUseCase;
import com.tokopedia.fcmcommon.utils.FcmRemoteConfigUtils;
import com.tokopedia.fcmcommon.utils.FcmTokenUtils;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.interceptors.authenticator.TkpdAuthenticatorGql;
import com.tokopedia.interceptors.refreshtoken.RefreshTokenGql;
import com.tokopedia.iris.Iris;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.linker.interfaces.LinkerRouter;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.loginregister.goto_seamless.worker.TemporaryTokenWorker;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.notifications.CMPushNotificationManager;
import com.tokopedia.notifications.inApp.CMInAppManager;
import com.tokopedia.notifications.inApp.viewEngine.CmInAppConstant;
import com.tokopedia.notifications.worker.PushWorker;
import com.tokopedia.oms.di.DaggerOmsComponent;
import com.tokopedia.oms.di.OmsComponent;
import com.tokopedia.oms.domain.PostVerifyCartWrapper;
import com.tokopedia.promotionstarget.presentation.GratifCmInitializer;
import com.tokopedia.pushnotif.PushNotification;
import com.tokopedia.remoteconfig.GraphqlHelper;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.tkpd.ConsumerSplashScreen;
import com.tokopedia.tkpd.applink.ApplinkUnsupportedImpl;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.fcm.AppNotificationReceiver;
import com.tokopedia.tkpd.nfc.NFCSubscriber;
import com.tokopedia.tkpd.utils.DeferredResourceInitializer;
import com.tokopedia.tkpd.utils.GQLPing;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.datastore.workmanager.DataStoreMigrationWorker;
import com.tokopedia.sessioncommon.worker.RefreshProfileWorker;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.hansel.hanselsdk.Hansel;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import rx.Observable;
import timber.log.Timber;

/**
 * @author normansyahputa on 12/15/16.
 */
public abstract class ConsumerRouterApplication extends MainApplication implements
        TkpdCoreRouter,
        AbstractionRouter,
        ApplinkRouter,
        LoyaltyModuleRouter,
        NetworkRouter,
        TkpdAppsFlyerRouter,
        LinkerRouter {

    private static final String ENABLE_ASYNC_CMPUSHNOTIF_INIT = "android_async_cmpushnotif_init";
    private static final String ENABLE_ASYNC_IRIS_INIT = "android_async_iris_init";
    private static final String ENABLE_ASYNC_GCM_LEGACY = "android_async_gcm_legacy";
    private static final String ADD_BROTLI_INTERCEPTOR = "android_add_brotli_interceptor";
    protected CacheManager cacheManager;

    private OmsComponent omsComponent;
    private Iris mIris;

    private FirebaseMessagingManager fcmManager;

    private static final int REDIRECTION_HOME = 1;
    private static final int REDIRECTION_WEBVIEW = 2;
    private static final int REDIRECTION_DEFAULT = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        initialiseHansel();
        initFirebase();
        GraphqlClient.setContextData(getApplicationContext());
        GraphqlClient.init(getApplicationContext(), getAuthenticator());
        NetworkClient.init(getApplicationContext());
        warmUpGQLClient();
        initIris();
        performLibraryInitialisation();
        initResourceDownloadManager();
    }

    private TkpdAuthenticatorGql getAuthenticator() {
        return new TkpdAuthenticatorGql(this, this, userSession, new RefreshTokenGql());
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
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(initWeave, ENABLE_ASYNC_CMPUSHNOTIF_INIT, ConsumerRouterApplication.this, true);
    }

    private boolean initLibraries() {
        initCMPushNotification();
        initCMDependencies();
        initDataStoreMigration();
        initRefreshProfileWorker();
        initSeamlessLoginWorker();
        connectTokoChat(false);
        return true;
    }

    // To Refresh the seamless login token every week
    private void initSeamlessLoginWorker() {
        if (userSession.isLoggedIn()) {
            TemporaryTokenWorker.Companion.scheduleWorker(this);
        }
    }

    private void initDataStoreMigration() {
        if (userSession.isLoggedIn()) {
            DataStoreMigrationWorker.Companion.scheduleWorker(this);
        }
    }

    private void initRefreshProfileWorker() {
        if (userSession.isLoggedIn()) {
            RefreshProfileWorker.scheduleWorker(this);
        }
    }

    private void initCMDependencies() {
        WeaveInterface gratiWeave = () -> {
            GratifCmInitializer.INSTANCE.start(ConsumerRouterApplication.this);
            return true;
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(gratiWeave, RemoteConfigKey.ENABLE_ASYNC_GRATIFICATION_INIT, getApplicationContext(), false);


    }

    private void initialiseHansel() {
        WeaveInterface hanselWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return executeHanselInit();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(hanselWeave, RemoteConfigKey.ENABLE_ASYNC_HANSEL_INIT, getApplicationContext(), true);
    }

    private boolean executeHanselInit() {
        Hansel.init(ConsumerRouterApplication.this);
        return true;
    }

    private void initResourceDownloadManager() {
        (new DeferredResourceInitializer()).initializeResourceDownloadManager(context);
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
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(irisInitializeWeave, ENABLE_ASYNC_IRIS_INIT, ConsumerRouterApplication.this, true);
    }

    private boolean executeIrisInitialize() {
        mIris.initialize();
        return true;
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
    public void onNewIntent(Context context, Intent intent) {
        NFCSubscriber.onNewIntent(context, intent);
    }

    private Intent getInboxReputationIntent(Context context) {
        return RouteManager.getIntent(context, ApplinkConst.REPUTATION);
    }

    private void forceLogout() {
        TrackApp.getInstance().getMoEngage().logoutEvent();
        userSession.logoutSession();
        disconnectTokoChat();
    }

    @Override
    public void onForceLogout(Activity activity) {
        forceLogout();
        Intent intent = new Intent(context, ConsumerSplashScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onForceLogoutV2(Activity activity, int redirectionType, String url) {
        forceLogout();
        if (redirectionType == REDIRECTION_HOME) {
            Intent intent = RouteManager.getIntent(context, ApplinkConst.HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (redirectionType == REDIRECTION_WEBVIEW) {
            Intent homeIntent = RouteManager.getIntent(this, ApplinkConst.HOME);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent webViewIntent = RouteManager.getIntent(this, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url));
            TaskStackBuilder task = TaskStackBuilder.create(this);
            task.addNextIntent(homeIntent);
            task.addNextIntent(webViewIntent);
            task.startActivities();
        } else {
            Intent intent = new Intent(context, ConsumerSplashScreen.class);
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
        CoreNetworkApplication.getAppContext().startActivity(MaintenancePage.createIntentFromNetwork(getAppContext()));
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
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "USER_ANOMALY_REPONSE");
        messageMap.put("title", title);
        messageMap.put("accessToken", accessToken);
        messageMap.put("refreshToken", refreshToken);
        messageMap.put("response", refreshToken);
        messageMap.put("request", request);
        ServerLogger.log(Priority.P2, "USER_ANOMALY_REPONSE", messageMap);
    }

    @Override
    public void showServerError(Response response) {
        ServerErrorHandler.sendErrorNetworkAnalytics(response.request().url().toString(), response.code());
    }

    @Override
    public void gcmUpdate() throws IOException {
        AccessTokenRefresh accessTokenRefresh = new AccessTokenRefresh();
        String accessToken = accessTokenRefresh.refreshToken();
        doRelogin(accessToken);
    }

    private Boolean isOldGcmUpdate() {
        return getBooleanRemoteConfig(FirebaseMessagingManager.ENABLE_OLD_GCM_UPDATE, false);
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
    public void logRefreshTokenException(String error, String type, String path, String accessToken) {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", type);
        messageMap.put("path", path);
        messageMap.put("error", error);
        if (!accessToken.isEmpty()) {
            messageMap.put("oldToken", accessToken);
        }
        ServerLogger.log(Priority.P2, "USER_AUTHENTICATOR", messageMap);
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
        return false;
    }

    @Override
    public ApplinkUnsupported getApplinkUnsupported(Activity activity) {
        return new ApplinkUnsupportedImpl(activity);
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
            RouteManager.route(activity, bundle, applink);
        }
    }

    @Override
    public Intent getApplinkIntent(Context context, String applink) {
        return RouteManager.getIntent(context, applink);
    }

    @Override
    public Observable<VoucherViewModel> checkTrainVoucher(String trainReservationId,
                                                          String trainReservationCode,
                                                          String galaCode) {
        return Observable.just(new VoucherViewModel());
    }

    @Override
    public FingerprintModel getFingerprintModel() {
        return FingerprintModelGenerator.generateFingerprintModel(this);
    }

    @Override
    public void doRelogin(String newAccessToken) {
        SessionRefresh sessionRefresh = new SessionRefresh(newAccessToken);
        try {
            if (isOldGcmUpdate()) {
                sessionRefresh.gcmUpdate();
            } else {
                if (fcmManager == null) {
                    provideFcmManager();
                }
                newGcmUpdate(sessionRefresh);
            }
        } catch (IOException e) {
        }
    }

    private void newGcmUpdate(SessionRefresh sessionRefresh) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    String token = task.getResult();
                    if (!TextUtils.isEmpty(token)) {
                        fcmManager.onNewToken(token);
                    } else {
                        gcmUpdateLegacy(sessionRefresh);
                    }
                } else {
                    gcmUpdateLegacy(sessionRefresh);
                }
            }
        });
    }

    private void gcmUpdateLegacy(SessionRefresh sessionRefresh) {
        WeaveInterface weave = new WeaveInterface() {
            @NotNull
            @Override
            public Boolean execute() {
                try {
                    sessionRefresh.gcmUpdate();
                } catch (Throwable ignored) {
                }
                return true;
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(weave, ENABLE_ASYNC_GCM_LEGACY, getApplicationContext(), true);
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
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(appsflyerInitWeave, RemoteConfigKey.ENABLE_ASYNC_APPSFLYER_INIT, getApplicationContext(), true);
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
        PushNotification.init(getApplicationContext());

        List<String> excludeScreenList = new ArrayList<>();
        excludeScreenList.add(CmInAppConstant.ScreenListConstants.SPLASH);
        excludeScreenList.add(CmInAppConstant.ScreenListConstants.DEEPLINK_ACTIVITY);
        excludeScreenList.add(CmInAppConstant.ScreenListConstants.DEEPLINK_HANDLER_ACTIVITY);
        CMInAppManager.getInstance().setExcludeScreenList(excludeScreenList);
        refreshFCMTokenFromBackgroundToCM(FCMCacheManager.getRegistrationId(ConsumerRouterApplication.this), false);
        PushWorker.Companion.schedulePeriodicWorker(this);
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


    @Override
    public IAppNotificationReceiver getAppNotificationReceiver() {
        return new AppNotificationReceiver();
    }

    @Override
    public void sendRefreshTokenAnalytics(String errorMessage) {
        if (TextUtils.isEmpty(errorMessage)) {
            SessionAnalytics.trackRefreshTokenSuccess();
        } else {
            SessionAnalytics.trackRefreshTokenFailed(errorMessage);
        }
    }

    private void provideFcmManager() {
        fcmManager = new FirebaseMessagingManagerImpl(
                new UpdateFcmTokenUseCase(
                        new GraphqlUseCase<>(GraphqlInteractor.getInstance().getGraphqlRepository()),
                        GraphqlHelper.loadRawString(context.getResources(), com.tokopedia.fcmcommon.R.raw.query_update_fcm_token)
                ),
                PreferenceManager.getDefaultSharedPreferences(context),
                userSession,
                provideCMHandler()
        );
    }

    private SendTokenToCMHandler provideCMHandler() {
        return new SendTokenToCMHandler(
                new SendTokenToCMUseCase(
                        GraphqlInteractor.getInstance().getGraphqlRepository()
                ),
                getApplicationContext(),
                userSession,
                new FcmRemoteConfigUtils(context),
                new FcmTokenUtils(new FcmCacheHandler(context)),
                new FcmCacheHandler(context)
        );
    }

    @Override
    public void connectTokoChat(Boolean isFromLoginFlow) {
        TokoChatConnection.init(getApplicationContext(), isFromLoginFlow);
    }

    @Override
    public void disconnectTokoChat() {
        TokoChatConnection.disconnect();
    }
}
