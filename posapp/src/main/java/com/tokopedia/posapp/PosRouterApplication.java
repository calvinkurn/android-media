package com.tokopedia.posapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.subscriber.ProfileCompletionSubscriber;
import com.tokopedia.core.gcm.ApplinkUnsupported;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.router.posapp.PosAppDataGetter;
import com.tokopedia.core.router.reactnative.IReactNativeRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.applink.DeepLinkDelegate;
import com.tokopedia.posapp.applink.DeeplinkHandlerActivity;
import com.tokopedia.posapp.cache.PosCacheHandler;
import com.tokopedia.posapp.di.component.DaggerReactNativeComponent;
import com.tokopedia.posapp.di.component.ReactNativeComponent;
import com.tokopedia.posapp.di.module.PosReactNativeModule;
import com.tokopedia.posapp.auth.login.view.activity.PosLoginActivity;
import com.tokopedia.posapp.outlet.view.activity.OutletActivity;
import com.tokopedia.posapp.product.productlist.view.activity.ProductListActivity;
import com.tokopedia.posapp.base.drawer.DrawerPosHelper;
import com.tokopedia.posapp.cache.view.service.SchedulerService;
import com.tokopedia.tkpdreactnative.react.ReactUtils;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 7/30/17.
 */

public class PosRouterApplication extends MainApplication implements
        TkpdCoreRouter, IDigitalModuleRouter, IReactNativeRouter, ReactApplication, PosAppDataGetter {

    private DaggerReactNativeComponent.Builder daggerReactNativeBuilder;
    private ReactNativeComponent reactNativeComponent;
    @Inject
    ReactNativeHost reactNativeHost;
    @Inject
    ReactUtils reactUtils;

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
    public void startInstopedActivityForResult(Activity activity, int resultCode, int maxResult) {

    }

    @Override
    public void startInstopedActivityForResult(Context context, Fragment fragment, int resultCode, int maxResult) {

    }

    @Override
    public void removeInstopedToken() {

    }

    @Override
    public void goToManageProduct(Context context) {

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
        if(isOutletSelected(context)) {
            intent = new Intent(this, ProductListActivity.class);
        } else {
            intent = new Intent(this, OutletActivity.class);
        }
        return intent;
    }

    @Override
    public Intent getOnBoardingActivityIntent(Context context) {
        return null;
    }

    @Override
    public Intent getPhoneVerificationActivityIntent(Context context) {
        return null;
    }

    public Class getHomeActivity(Context context){
        if(isOutletSelected(context)) {
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
    public void goToCreateMerchantRedirect(Context context) {

    }

    @Override
    public Intent getLoginIntent(Context context) {
        return PosLoginActivity.getPosLoginIntent(context);
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
    public BroadcastReceiver getBroadcastReceiverTokocashPending() {
        return null;
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
    public Observable<TokoCashData> getTokoCashBalance() {
        return null;
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
        daggerReactNativeBuilder = DaggerReactNativeComponent.builder()
                .appComponent(getApplicationComponent())
                .posReactNativeModule(new PosReactNativeModule(this));
    }

    private void initDaggerInjector() {
        getReactNativeComponent().inject(this);
    }

    private ReactNativeComponent getReactNativeComponent() {
        if (reactNativeComponent == null)
            reactNativeComponent = daggerReactNativeBuilder.build();
        return reactNativeComponent;
    }
}
