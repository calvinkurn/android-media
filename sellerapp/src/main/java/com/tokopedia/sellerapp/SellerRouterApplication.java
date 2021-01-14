package com.tokopedia.sellerapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tkpd.library.utils.legacy.AnalyticsLog;
import com.tkpd.library.utils.legacy.SessionAnalytics;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.analyticsdebugger.debugger.TetraDebugger;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkDelegate;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.ApplinkUnsupported;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp;
import com.tokopedia.cacheapi.domain.interactor.CacheApiClearAllUseCase;
import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.MaintenancePage;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.network.CoreNetworkRouter;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.util.AccessTokenRefresh;
import com.tokopedia.core.util.PasswordGenerator;
import com.tokopedia.core.util.SessionRefresh;
import com.tokopedia.developer_options.config.DevOptConfig;
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.linker.interfaces.LinkerRouter;
import com.tokopedia.loginregister.login.router.LoginRouter;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.notifications.CMPushNotificationManager;
import com.tokopedia.notifications.inApp.CMInAppManager;
import com.tokopedia.product.manage.feature.list.view.fragment.ProductManageSellerFragment;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.sellerapp.deeplink.DeepLinkActivity;
import com.tokopedia.sellerapp.deeplink.DeepLinkDelegate;
import com.tokopedia.sellerapp.deeplink.DeepLinkHandlerActivity;
import com.tokopedia.sellerapp.fcm.AppNotificationReceiver;
import com.tokopedia.sellerapp.onboarding.SellerOnboardingBridgeActivity;
import com.tokopedia.sellerapp.utils.DeferredResourceInitializer;
import com.tokopedia.sellerapp.utils.SellerOnboardingPreference;
import com.tokopedia.sellerapp.utils.constants.Constants;
import com.tokopedia.sellerhome.SellerHomeRouter;
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity;
import com.tokopedia.sellerorder.common.util.SomConsts;
import com.tokopedia.sellerorder.list.presentation.fragments.SomListFragment;
import com.tokopedia.talk.feature.inbox.presentation.activity.TalkInboxActivity;
import com.tokopedia.topads.TopAdsComponentInstance;
import com.tokopedia.topads.TopAdsModuleRouter;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topchat.chatlist.fragment.ChatTabListFragment;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.hansel.hanselsdk.Hansel;
import okhttp3.Response;
import timber.log.Timber;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;

/**
 * Created by normansyahputa on 12/15/16.
 */

public abstract class SellerRouterApplication extends MainApplication
        implements TkpdCoreRouter, TopAdsModuleRouter,
        AbstractionRouter,
        ApplinkRouter,
        NetworkRouter,
        CoreNetworkRouter,
        LinkerRouter,
        SellerHomeRouter,
        LoginRouter {

    protected RemoteConfig remoteConfig;
    private TopAdsComponent topAdsComponent;
    private TetraDebugger tetraDebugger;
    protected CacheManager cacheManager;

    private static final String ENABLE_ASYNC_CMPUSHNOTIF_INIT = "android_async_cmpushnotif_init";

    @Override
    public void onCreate() {
        super.onCreate();
        Hansel.init(this);
        initializeRemoteConfig();
        initResourceDownloadManager();
        initIris();
        performLibraryInitialisation();
    }

    private void performLibraryInitialisation(){
        WeaveInterface initWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return initLibraries();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(initWeave, ENABLE_ASYNC_CMPUSHNOTIF_INIT, SellerRouterApplication.this);
    }

    private boolean initLibraries(){
        initCMPushNotification();
        initTetraDebugger();
        return true;
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
        List<String> excludeScreenList = new ArrayList<>();
        excludeScreenList.add(Constants.SPLASH);
        excludeScreenList.add(Constants.DEEPLINK_ACTIVITY);
        excludeScreenList.add(Constants.DEEPLINK_HANDLER_ACTIVITY);
        CMInAppManager.getInstance().setExcludeScreenList(excludeScreenList);
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

    private void initTetraDebugger() {
        if(GlobalConfig.isAllowDebuggingTools()) {
            tetraDebugger = TetraDebugger.Companion.instance(this);
            tetraDebugger.init();
        }
    }

    private void setTetraUserId(String userId) {
        if(tetraDebugger != null) {
            tetraDebugger.setUserId(userId);
        }
    }

    @Override
    public TopAdsComponent getTopAdsComponent() {
        if (topAdsComponent == null) {
            topAdsComponent = TopAdsComponentInstance.getComponent(this);
        }
        return topAdsComponent;
    }

    @Override
    public CacheManager getPersistentCacheManager() {
        if(cacheManager == null)
            cacheManager = new PersistentCacheManager(this);
        return cacheManager;
    }

    @Override
    public NotificationPass setNotificationPass(Context mContext, NotificationPass mNotificationPass, Bundle data, String notifTitle) {
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
    public Intent getHomeIntent(Context context) {
        UserSessionInterface userSession = new UserSession(context);
        Intent intent = RouteManager.getIntent(this, ApplinkConstInternalSellerapp.WELCOME);
        if (userSession.isLoggedIn()) {
            if (userSession.hasShop()) {
                return SellerHomeActivity.createIntent(context);
            } else {
                return intent;
            }
        } else {
            return intent;
        }
    }

    @Override
    public Class<?> getHomeClass() {
        UserSessionInterface userSession = new UserSession(context);
        if (userSession.isLoggedIn()) {
            return SellerHomeActivity.class;
        } else {
            return SellerOnboardingBridgeActivity.class;
        }
    }

    @Override
    public void onLogout(AppComponent appComponent) {
        forceLogout();
        new CacheApiClearAllUseCase(this).executeSync();
        setTetraUserId("");
    }

    private void forceLogout() {
        PasswordGenerator.clearTokenStorage(context);
        TrackApp.getInstance().getMoEngage().logoutEvent();
        UserSessionInterface userSession = new UserSession(context);
        userSession.logoutSession();
    }

    @Override
    public boolean isSupportedDelegateDeepLink(String appLinks) {
        DeepLinkDelegate deepLinkDelegate = DeepLinkHandlerActivity.getDelegateInstance();
        return deepLinkDelegate.supportsUri(appLinks);
    }

    @NonNull
    @Override
    public Intent getSplashScreenIntent(@NonNull Context context) {
        return new Intent(context, SplashScreenActivity.class);
    }

    @Override
    public ApplinkUnsupported getApplinkUnsupported(Activity activity) {
        return null;
    }

    @Override
    public void onForceLogout(Activity activity) {
        forceLogout();
        Intent intent = getSplashScreenIntent(this);
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
    public void logInvalidGrant(Response response) {
        AnalyticsLog.logInvalidGrant(this, response.request().url().toString());

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
    public Intent getInboxTalkCallingIntent(@NonNull Context context) {
        return TalkInboxActivity.Companion.createIntent(context);
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
    public Class getDeeplinkClass() {
        return DeepLinkActivity.class;
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
        if(TextUtils.isEmpty(errorMessage)){
            SessionAnalytics.trackRefreshTokenSuccess();
        }else {
            SessionAnalytics.trackRefreshTokenFailed(errorMessage);
        }
    }

    @Override
    public void onNewIntent(Context context, Intent intent) {
        //no op
    }

    @NotNull
    @Override
    public Fragment getSomListFragment(String tabPage, int orderType) {
        Bundle bundle = new Bundle();
        tabPage = (null == tabPage || "".equals(tabPage)) ? SomConsts.STATUS_ALL_ORDER : tabPage;
        bundle.putString(SomConsts.TAB_ACTIVE, tabPage);
        bundle.putInt(SomConsts.FILTER_ORDER_TYPE, orderType);
        if (getBooleanRemoteConfig(SomConsts.ENABLE_NEW_SOM, true)) {
            return SomListFragment.newInstance(bundle);
        } else {
            return com.tokopedia.sellerorder.oldlist.presentation.fragment.SomListFragment.newInstance(bundle);
        }
    }

    @NotNull
    @Override
    public Fragment getProductManageFragment(@NotNull ArrayList<String> filterOptions, @NotNull String searchKeyword) {
        return ProductManageSellerFragment.newInstance(filterOptions, searchKeyword);
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
}