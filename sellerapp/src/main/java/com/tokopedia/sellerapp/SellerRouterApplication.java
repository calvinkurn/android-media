package com.tokopedia.sellerapp;

import static com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.QUERY_PARAM_SEARCH;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.TaskStackBuilder;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.tkpd.library.utils.legacy.AnalyticsLog;
import com.tkpd.library.utils.legacy.SessionAnalytics;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.analyticsdebugger.debugger.TetraDebugger;
import com.tokopedia.app.common.MainApplication;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.ApplinkUnsupported;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.order.DeeplinkMapperOrder;
import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.common.ui.MaintenancePage;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.network.CoreNetworkApplication;
import com.tokopedia.core.network.CoreNetworkRouter;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.util.AccessTokenRefresh;
import com.tokopedia.core.util.SessionRefresh;
import com.tokopedia.developer_options.config.DevOptConfig;
import com.tokopedia.device.info.DeviceScreenInfo;
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator;
import com.tokopedia.fcmcommon.FirebaseMessagingManager;
import com.tokopedia.fcmcommon.di.DaggerFcmComponent;
import com.tokopedia.fcmcommon.di.FcmComponent;
import com.tokopedia.fcmcommon.di.FcmModule;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.linker.interfaces.LinkerRouter;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.loginregister.goto_seamless.worker.TemporaryTokenWorker;
import com.tokopedia.sessioncommon.worker.RefreshProfileWorker;
import com.tokopedia.loginregister.login.router.LoginRouter;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.notifications.CMPushNotificationManager;
import com.tokopedia.notifications.inApp.CMInAppManager;
import com.tokopedia.notifications.worker.PushWorker;
import com.tokopedia.product.manage.feature.list.view.fragment.ProductManageSellerFragment;
import com.tokopedia.pushnotif.PushNotification;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.sellerapp.fcm.AppNotificationReceiver;
import com.tokopedia.sellerapp.fcm.di.DaggerGcmUpdateComponent;
import com.tokopedia.sellerapp.fcm.di.GcmUpdateComponent;
import com.tokopedia.sellerapp.utils.DeferredResourceInitializer;
import com.tokopedia.sellerapp.utils.SellerOnboardingPreference;
import com.tokopedia.sellerapp.utils.constants.Constants;
import com.tokopedia.sellerhome.SellerHomeRouter;
import com.tokopedia.sellerorder.common.presenter.fragments.SomContainerFragment;
import com.tokopedia.sellerorder.common.util.SomConsts;
import com.tokopedia.sellerorder.list.presentation.fragments.SomListFragment;
import com.tokopedia.topchat.chatlist.view.fragment.ChatTabListFragment;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.Lazy;
import io.hansel.hanselsdk.Hansel;
import okhttp3.Response;

/**
 * Created by normansyahputa on 12/15/16.
 */

public abstract class SellerRouterApplication extends MainApplication implements
        TkpdCoreRouter,
        AbstractionRouter,
        ApplinkRouter,
        NetworkRouter,
        CoreNetworkRouter,
        LinkerRouter,
        SellerHomeRouter,
        LoginRouter {

    private TetraDebugger tetraDebugger;

    protected RemoteConfig remoteConfig;
    protected CacheManager cacheManager;

    private DaggerGcmUpdateComponent.Builder daggerGcmUpdateBuilder;
    private GcmUpdateComponent gcmUpdateComponent;
    @Inject
    Lazy<FirebaseMessagingManager> fcmManager;

    private static final String ENABLE_ASYNC_CMPUSHNOTIF_INIT = "android_async_cmpushnotif_init";
    private static final String ENABLE_ASYNC_GCM_LEGACY = "android_async_gcm_legacy";

    private static final int REDIRECTION_HOME = 1;
    private static final int REDIRECTION_WEBVIEW = 2;
    private static final int REDIRECTION_DEFAULT= 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Hansel.init(this);
        initializeRemoteConfig();
        initResourceDownloadManager();
        initIris();
        performLibraryInitialisation();
    }

    private void performLibraryInitialisation() {
        WeaveInterface initWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return initLibraries();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(initWeave, ENABLE_ASYNC_CMPUSHNOTIF_INIT, SellerRouterApplication.this, true);
    }

    private boolean initLibraries() {
        initCMPushNotification();
        initTetraDebugger();
        initSeamlessLoginWorker();
        initRefreshProfileWorker();
        return true;
    }

    private void initSeamlessLoginWorker() {
        UserSessionInterface userSession = new UserSession(context);
        if(userSession.isLoggedIn()) {
            TemporaryTokenWorker.Companion.scheduleWorker(this);
        }
    }

    private void initRefreshProfileWorker() {
        UserSessionInterface userSession = new UserSession(context);
        if(userSession.isLoggedIn()) {
            RefreshProfileWorker.Companion.scheduleWorker(this);
        }
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


    private void initCMPushNotification() {
        CMPushNotificationManager.getInstance().init(this);
        PushNotification.init(getApplicationContext());

        List<String> excludeScreenList = new ArrayList<>();
        excludeScreenList.add(Constants.SPLASH);
        excludeScreenList.add(Constants.DEEPLINK_ACTIVITY);
        excludeScreenList.add(Constants.DEEPLINK_HANDLER_ACTIVITY);
        CMInAppManager.getInstance().setExcludeScreenList(excludeScreenList);
        refreshFCMTokenFromBackgroundToCM(FCMCacheManager.getRegistrationId(this), false);
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

    private void initTetraDebugger() {
        if (GlobalConfig.isAllowDebuggingTools()) {
            tetraDebugger = TetraDebugger.Companion.instance(this);
            tetraDebugger.init();
        }
    }

    private void setTetraUserId(String userId) {
        if (tetraDebugger != null) {
            tetraDebugger.setUserId(userId);
        }
    }

    @Override
    public CacheManager getPersistentCacheManager() {
        if (cacheManager == null)
            cacheManager = new PersistentCacheManager(this);
        return cacheManager;
    }

    @Override
    public ApplinkUnsupported getApplinkUnsupported(Activity activity) {
        return null;
    }


    @Override
    public void onForceLogout(Activity activity) {
        forceLogout();
        Intent intent = new Intent(context, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onForceLogoutV2(Activity activity, int redirectionType, String url) {
        forceLogout();
        if(redirectionType == REDIRECTION_WEBVIEW) {
            Intent homeIntent = new Intent(context, SplashScreenActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent webViewIntent = RouteManager.getIntent(this, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url));
            TaskStackBuilder task = TaskStackBuilder.create(this);
            task.addNextIntent(homeIntent);
            task.addNextIntent(webViewIntent);
            task.startActivities();
        } else {
            Intent intent = new Intent(context, SplashScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void forceLogout() {
        TrackApp.getInstance().getMoEngage().logoutEvent();
        UserSessionInterface userSession = new UserSession(context);
        userSession.logoutSession();
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
    public void showForceLogoutTokenDialog(String path) {
        ServerErrorHandler.showForceLogoutDialog(path);
        ServerErrorHandler.sendForceLogoutTokenAnalytics(path);
    }

    @Override
    public void sendAnalyticsAnomalyResponse(String title,
                                             String accessToken, String refreshToken,
                                             String response, String request) {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "AnomalyResponse");
        messageMap.put("title", title);
        messageMap.put("accessToken", accessToken);
        messageMap.put("refreshToken", refreshToken);
        messageMap.put("response", response);
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

    @Override
    public void refreshToken() throws IOException {
        AccessTokenRefresh accessTokenRefresh = new AccessTokenRefresh();
        accessTokenRefresh.refreshToken();
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
    public void goToApplinkActivity(Context context, String applink) {
        RouteManager.route(context, applink);
    }

    @Override
    public Intent getApplinkIntent(Context context, String applink) {
        return RouteManager.getIntent(context, applink);
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
                if (gcmUpdateComponent == null) {
                    injectGcmUpdateComponent();
                }
                newGcmUpdate(sessionRefresh);
            }
        } catch (IOException e) {
        }
    }

    private void newGcmUpdate(SessionRefresh sessionRefresh) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful() || task.getResult() == null) {
                    gcmUpdateLegacy(sessionRefresh);
                } else {
                    fcmManager.get().onNewToken(task.getResult().getToken());
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
    public void goToApplinkActivity(Activity activity, String applink, Bundle bundle) {
        Intent intent = RouteManager.getIntent(activity, applink);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    public boolean isSupportApplink(String appLink) {
        return false;
    }

    @Override
    public void onAppsFlyerInit() {

    }

    @Override
    public boolean isAllowLogOnChuckInterceptorNotification() {
        return DevOptConfig.isChuckNotifEnabled(this);
    }

    @Override
    public boolean getBooleanRemoteConfig(String key, boolean defaultValue) {
        return remoteConfig.getBoolean(key, defaultValue);
    }

    @Override
    public Intent getMaintenancePageIntent() {
        return MaintenancePage.createIntentFromNetwork(getAppContext());
    }

    @Override
    public void sendForceLogoutAnalytics(String url, boolean isInvalidToken,
                                         boolean isRequestDenied) {
        ServerErrorHandler.sendForceLogoutAnalytics(url, isInvalidToken, isRequestDenied);
    }

    @Override
    public void sendRefreshTokenAnalytics(String errorMessage) {
        if (TextUtils.isEmpty(errorMessage)) {
            SessionAnalytics.trackRefreshTokenSuccess();
        } else {
            SessionAnalytics.trackRefreshTokenFailed(errorMessage);
        }
    }

    @Override
    public void onNewIntent(Context context, Intent intent) {
        //no op
    }

    @NotNull
    @Override
    public Fragment getSomListFragment(@NotNull Context context, @Nullable String tabPage, @NotNull String orderType, @NotNull String searchKeyword, @NotNull String orderId) {
        Bundle bundle = new Bundle();
        tabPage = (null == tabPage || "".equals(tabPage)) ? SomConsts.STATUS_NEW_ORDER : tabPage;
        bundle.putString(SomConsts.TAB_ACTIVE, tabPage);
        bundle.putString(SomConsts.FILTER_ORDER_TYPE, orderType);
        bundle.putString(QUERY_PARAM_SEARCH, searchKeyword);
        if (DeviceScreenInfo.isTablet(context)) {
            if (orderId.trim().length() > 0) {
                bundle.putString(DeeplinkMapperOrder.QUERY_PARAM_ORDER_ID, orderId);
            }
            return SomContainerFragment.newInstance(bundle);
        } else {
            return SomListFragment.newInstance(bundle);
        }
    }

    @NonNull
    @Override
    public Fragment getProductManageFragment(@NonNull ArrayList<String> filterOptions, @NonNull String searchKeyword, @NonNull String tab, View navigationMenu) {
        ProductManageSellerFragment productManageSellerFragment = ProductManageSellerFragment.newInstance(filterOptions, tab, searchKeyword);
        productManageSellerFragment.setNavigationHomeMenuView(navigationMenu);
        return productManageSellerFragment;
    }

    @NotNull
    @Override
    public Fragment getChatListFragment() {
        return ChatTabListFragment.create();
    }

    @Override
    public IAppNotificationReceiver getAppNotificationReceiver() {
        return new AppNotificationReceiver();
    }

    @Override
    public void setOnboardingStatus(boolean status) {
        SellerOnboardingPreference preference = new SellerOnboardingPreference(this);
        preference.putBoolean(SellerOnboardingPreference.HAS_OPEN_ONBOARDING, status);
    }

    private Boolean isOldGcmUpdate() {
        return getBooleanRemoteConfig(FirebaseMessagingManager.ENABLE_OLD_GCM_UPDATE, false);
    }

    private void injectGcmUpdateComponent() {
        if (!isOldGcmUpdate()) {
            if (daggerGcmUpdateBuilder == null) {
                FcmComponent fcmComponent = DaggerFcmComponent.builder()
                        .fcmModule(new FcmModule(this))
                        .build();

                daggerGcmUpdateBuilder = DaggerGcmUpdateComponent.builder()
                        .fcmComponent(fcmComponent);
            }
            if (gcmUpdateComponent == null) {
                gcmUpdateComponent = daggerGcmUpdateBuilder.build();
            }
            gcmUpdateComponent.inject(this);
        }
    }

    @Override
    public void connectTokoChat(Boolean isFromLoginFlow) {
        //Do nothing
    }

    @Override
    public void disconnectTokoChat() {
        //Do nothing
    }

}