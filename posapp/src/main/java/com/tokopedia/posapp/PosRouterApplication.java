package com.tokopedia.posapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.tkpd.library.utils.AnalyticsLog;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.SessionRouter;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.applink.ApplinkUnsupported;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.subscriber.ProfileCompletionSubscriber;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.manage.people.address.model.Token;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.router.posapp.PosAppDataGetter;
import com.tokopedia.core.router.reactnative.IReactNativeRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.SessionRefresh;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.posapp.applink.DeepLinkDelegate;
import com.tokopedia.posapp.applink.DeeplinkHandlerActivity;
import com.tokopedia.posapp.auth.login.view.activity.PosLoginActivity;
import com.tokopedia.posapp.base.drawer.DrawerPosHelper;
import com.tokopedia.posapp.cache.PosCacheHandler;
import com.tokopedia.posapp.cache.view.service.SchedulerService;
import com.tokopedia.posapp.di.component.DaggerPosAppComponent;
import com.tokopedia.posapp.di.component.PosAppComponent;
import com.tokopedia.posapp.outlet.view.activity.OutletActivity;
import com.tokopedia.posapp.product.productlist.view.activity.ProductListActivity;
import com.tokopedia.posapp.react.di.component.DaggerPosReactNativeComponent;
import com.tokopedia.posapp.react.di.component.PosReactNativeComponent;
import com.tokopedia.posapp.react.di.module.PosReactNativeModule;
import com.tokopedia.session.forgotpassword.activity.ForgotPasswordActivity;
import com.tokopedia.tkpdreactnative.react.ReactUtils;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Response;
import rx.Observable;

/**
 * Created by okasurya on 7/30/17.
 */

public class PosRouterApplication extends MainApplication implements
        TkpdCoreRouter, IDigitalModuleRouter, IReactNativeRouter, ReactApplication, PosAppDataGetter,
        AbstractionRouter, SessionRouter, NetworkRouter {

    @Inject
    ReactNativeHost reactNativeHost;
    @Inject
    ReactUtils reactUtils;
    private DaggerPosReactNativeComponent.Builder daggerReactNativeBuilder;
    private PosReactNativeComponent posReactNativeComponent;
    private UserSession userSession;
    private CacheManager cacheManager;
    private PosAppComponent posAppComponent;

    @Override
    public String getOutletName() {
        return PosSessionHandler.getOutletName(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeDagger();
        initDaggerInjector();
    }

    @Override
    public void goToManageProduct(Context context) {

    }

    @Override
    public void goToEtalaseList(Context context) {

    }

    @Override
    public void goToDraftProductList(Context context) {

    }

    @Override
    public void clearEtalaseCache() {

    }

    @Override
    public Intent goToEditProduct(Context context, boolean isEdit, String productId) {
        return null;
    }

    @Override
    public void resetAddProductCache(Context context) {

    }

    @Override
    public void goToWallet(Context context, String url) {

    }

    @Override
    public void goToMerchantRedirect(Context context) {

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

    }

    @Override
    public void actionApplink(Activity activity, String linkUrl, String extras) {
        DeepLinkDelegate deepLinkDelegate = DeeplinkHandlerActivity.getDelegateInstance();
        Intent intent = activity.getIntent();
        intent.setData(Uri.parse(linkUrl));
        intent.putExtra(EXTRAS, extras);
        deepLinkDelegate.dispatchFrom(activity, intent);
    }

    @Override
    public void actionOpenGeneralWebView(Activity activity, String mobileUrl) {

    }

    @Override
    public Fragment getShopReputationFragment(String shopId, String shopDomain) {
        return null;
    }

    @Override
    public Intent getHomeIntent(Context context) {
        startService(SchedulerService.getDefaultServiceIntent(this));
        Intent intent;
        if (isOutletSelected(context)) {
            intent = new Intent(this, ProductListActivity.class);
        } else {
            intent = new Intent(this, OutletActivity.class);
        }
        return intent;
    }

    @Override
    public Interceptor getChuckInterceptor() {
        return null;
    }

    @Override
    public Intent getPhoneVerificationActivityIntent(Context context) {
        return null;
    }

    public Class getHomeActivity(Context context) {
        if (isOutletSelected(context)) {
            return ProductListActivity.class;
        } else {
            return OutletActivity.class;
        }
    }

    @Override
    public Class<?> getHomeClass(Context context) throws ClassNotFoundException {
        return null;
    }

    @Override
    public DrawerHelper getDrawer(AppCompatActivity activity, SessionHandler sessionHandler, LocalCacheHandler drawerCache, GlobalCacheManager globalCacheManager) {
        return DrawerPosHelper.createInstance(activity, sessionHandler, drawerCache);
    }

    @Override
    public void onLogout(AppComponent appComponent) {
        PosSessionHandler.clearPosUserData(this);
        PosCacheHandler.clearUserData(this);
        SchedulerService.cancelCacheScheduler(getApplicationContext());
    }

    @Override
    public void onAppsFlyerInit() {

    }

    @Override
    public void goToCreateMerchantRedirect(Context context) {

    }

    @Override
    public Intent getLoginIntent(Context context) {
        return PosLoginActivity.getPosLoginIntent(context);
    }

    @Override
    public Intent getOrderListIntent(Context context) {
        return null;
    }

    @Override
    public Intent getRegisterIntent(Context context) {
        return null;
    }

    @Override
    public void getUserInfo(RequestParams empty, ProfileCompletionSubscriber profileSubscriber) {

    }

    @Override
    public String getFlavor() {
        return BuildConfig.FLAVOR;
    }

    @Override
    public Intent instanceIntentCartDigitalProduct(DigitalCheckoutPassData passData) {
        return null;
    }

    @Override
    public Intent instanceIntentCartDigitalProductWithBundle(Bundle bundle) {
        return null;
    }

    @Override
    public Intent instanceIntentDigitalProduct(DigitalCategoryDetailPassData passData) {
        return null;
    }

    @Override
    public Intent instanceIntentDigitalCategoryList() {
        return null;
    }

    @Override
    public Intent instanceIntentDigitalWeb(String url) {
        return null;
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
    public boolean isSupportedDelegateDeepLink(String appLinks) {
        DeepLinkDelegate deepLinkDelegate = DeeplinkHandlerActivity.getDelegateInstance();
        return deepLinkDelegate.supportsUri(appLinks);
    }

    @Override
    public Intent getIntentDeepLinkHandlerActivity() {
        return null;
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
    public void goToAddProduct(Activity activity) {

    }

    @Override
    public boolean isInMyShop(Context context, String shopId) {
        return false;
    }

    @Override
    public Intent getForgotPasswordIntent(Context context, String email) {
        return null;
    }

    @Override
    public void invalidateCategoryMenuData() {

    }

    @Override
    public ApplinkUnsupported getApplinkUnsupported(Activity activity) {
        return null;
    }

    @Override
    public Intent getIntentCreateShop(Context context) {
        return null;
    }

    @Override
    public Intent getSplashScreenIntent(Context context) {
        return null;
    }

    @Override
    public Class getDeepLinkClass() {
        return null;
    }

    @Override
    public Intent getIntentManageShop(Context context) {
        return null;
    }

    @Override
    public android.app.Fragment getFragmentShopSettings() {
        return null;
    }

    @Override
    public android.app.Fragment getFragmentSellingNewOrder() {
        return null;
    }

    @Override
    public Class getSellingActivityClass() {
        return null;
    }

    @Override
    public Intent getActivitySellingTransactionNewOrder(Context context) {
        return null;
    }

    @Override
    public Intent getActivitySellingTransactionConfirmShipping(Context context) {
        return null;
    }

    @Override
    public Intent getActivitySellingTransactionShippingStatus(Context context) {
        return null;
    }

    @Override
    public Intent getActivitySellingTransactionList(Context context) {
        return null;
    }

    @Override
    public Intent getActivitySellingTransactionOpportunity(Context context, String query) {
        return null;
    }

    @Override
    public Intent getHomeHotlistIntent(Context context) {
        return null;
    }

    @Override
    public NotificationPass setNotificationPass(Context mContext, NotificationPass mNotificationPass, Bundle data, String notifTitle) {
        return null;
    }

    @Override
    public Intent getInboxReputationIntent(Context context) {
        return null;
    }

    @Override
    public Intent getResolutionCenterIntent(Context context) {
        return null;
    }

    @Override
    public Intent getResolutionCenterIntentBuyer(Context context) {
        return null;
    }

    @Override
    public Intent getResolutionCenterIntentSeller(Context context) {
        return null;
    }

    @Override
    public String applink(Activity activity, String deeplink) {
        return null;
    }

    @Override
    public Intent getKolFollowingPageIntent(Context context, int userId) {
        return null;
    }

    @Override
    public Intent getChangePhoneNumberIntent(Context context, String email, String phoneNumber) {
        return null;
    }

    @Override
    public Intent getPhoneVerificationProfileIntent(Context context) {
        return null;
    }

    @Override
    public Intent getPhoneVerificationActivationIntent(Context context) {
        return null;
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
    public Intent getShopPageIntent(Context context, String shopId) {
        return null;
    }

    @Override
    public Intent getShopPageIntentByDomain(Context context, String domain) {
        return null;
    }

    @Override
    public Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId) {
        return null;
    }

    @Override
    public Observable<TokoCashData> getTokoCashBalance() {
        return null;
    }

    @Override
    public Intent getAddEmailIntent(Context context) {
        return null;
    }

    @Override
    public Intent getAddPasswordIntent(Context context) {
        return null;
    }

    @Override
    public Intent getChangeNameIntent(Context context) {
        return null;
    }

    @Override
    public Intent getTopProfileIntent(Context context, String userId) {
        return null;
    }

    @Override
    public Intent getGroupChatIntent(Context context, String channelUrl) {
        return null;
    }

    @Override
    public Intent getInboxChannelsIntent(Context context) {
        return null;
    }

    @Override
    public Intent getInboxMessageIntent(Context context) {
        return null;
    }

    @Override
    public void sendTrackingGroupChatLeftNavigation() {

    }

    @Override
    public String getDesktopLinkGroupChat() {
        return null;
    }

    @Override
    public Intent getDistrictRecommendationIntent(Activity activity, Token token, boolean isFromMarketplaceCart) {
        return null;
    }

    @Override
    public Intent getWithdrawIntent(Context context) {
        return null;
    }

    @Override
    public void sendAFCompleteRegistrationEvent(int userId, String methodName) {

    }

    @Override
    public String getStringRemoteConfig(String key) {
        return null;
    }

    @Override
    public void setStringRemoteConfigLocal(String key, String value) {

    }

    @Override
    public void sendAddWishlistEmitter(String productId, String userId) {

    }

    @Override
    public void sendRemoveWishlistEmitter(String productId, String userId) {

    }

    @Override
    public void sendAddFavoriteEmitter(String shopId, String userId) {

    }

    @Override
    public void sendRemoveFavoriteEmitter(String shopId, String userId) {

    }

    @Override
    public void sendLoginEmitter(String userId) {

    }

    @Override
    public ReactNativeHost getReactNativeHost() {
        if (reactNativeHost == null) initDaggerInjector();
        return reactNativeHost;
    }

    public boolean isOutletSelected(Context context) {
        return PosSessionHandler.getOutletId(context) != null
                && !PosSessionHandler.getOutletId(context).equals("");
    }

    private void initializeDagger() {
        daggerReactNativeBuilder = DaggerPosReactNativeComponent.builder()
                .posAppComponent(getPosAppComponent())
                .posReactNativeModule(new PosReactNativeModule(this));
    }

    private void initDaggerInjector() {
        getPosReactNativeComponent().inject(this);
    }

    private PosReactNativeComponent getPosReactNativeComponent() {
        if (posReactNativeComponent == null)
            posReactNativeComponent = daggerReactNativeBuilder.build();
        return posReactNativeComponent;
    }

    @Override
    public void onForceLogout(Activity activity) {
        PosSessionHandler.clearPosUserData(activity);
        PosCacheHandler.clearUserData(activity);
        SchedulerService.cancelCacheScheduler(activity.getApplicationContext());
    }

    @Override
    public void showTimezoneErrorSnackbar() {

    }

    @Override
    public void showMaintenancePage() {

    }

    @Override
    public void showForceLogoutDialog(Response response) {
        ServerErrorHandler.showForceLogoutDialog();
        ServerErrorHandler.sendForceLogoutAnalytics(response.request().url().toString());
    }

    @Override
    public void showForceLogoutTokenDialog(String response) {
        ServerErrorHandler.showForceLogoutDialog();
        ServerErrorHandler.sendForceLogoutAnalytics(response);
    }

    @Override
    public void showServerError(Response response) {

    }

    @Override
    public void gcmUpdate() throws IOException {

    }

    @Override
    public void refreshToken() throws IOException {

    }

    @Override
    public UserSession getSession() {
        if (userSession == null) userSession = new UserSessionImpl(this);

        return userSession;
    }

    @Override
    public void init() {

    }

    @Override
    public void registerShake(String screenName, Activity activity) {

    }

    @Override
    public void unregisterShake() {

    }

    @Override
    public CacheManager getGlobalCacheManager() {
        if (cacheManager == null) cacheManager = new GlobalCacheManager();
        return cacheManager;
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
                if(activity != null && !TextUtils.isEmpty(screenName)) {
                    ScreenTracking.sendScreen(activity, () -> screenName);
                }
            }

            @Override
            public void sendEnhancedEcommerce(Map<String, Object> trackingData) {
                // no-op
            }
        };
    }

    @Override
    public void showForceHockeyAppDialog() {
//        ServerErrorHandler.showForceHockeyAppDialog();
    }

    @Override
    public void logInvalidGrant(Response response) {
        AnalyticsLog.logInvalidGrant(response.request().url().toString());
    }

    @Override
    public void instabugCaptureUserStep(Activity activity, MotionEvent me) {

    }

    @Override
    public FingerprintModel getFingerprintModel() {
        return null;
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
    public boolean isLoginInactivePhoneLinkEnabled() {
        return false;
    }

    public PosAppComponent getPosAppComponent() {
        if (posAppComponent == null) {
            posAppComponent = DaggerPosAppComponent.builder().baseAppComponent(getBaseAppComponent()).build();
        }

        return posAppComponent;
    }

    @Override
    public Intent getSettingBankIntent(Context context) {
//        There is no setting bank in pos
        return null;
    }

    @Override
    public Intent getInboxTalkCallingIntent(Context context) {
        return null;
    }

    @Override
    public Intent getAutomaticResetPasswordIntent(Context context, String email) {
        return ForgotPasswordActivity.getAutomaticResetPasswordIntent(context, email);
    }
}
