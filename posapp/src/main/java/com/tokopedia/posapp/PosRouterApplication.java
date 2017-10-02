package com.tokopedia.posapp;

import android.app.Activity;
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
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.subscriber.ProfileCompletionSubscriber;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.router.reactnative.IReactNativeRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.deeplink.DeepLinkDelegate;
import com.tokopedia.posapp.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.posapp.di.component.DaggerReactNativeComponent;
import com.tokopedia.posapp.di.component.ReactNativeComponent;
import com.tokopedia.posapp.di.module.PosReactNativeModule;
import com.tokopedia.posapp.view.activity.OutletActivity;
import com.tokopedia.posapp.view.activity.ProductListActivity;
import com.tokopedia.posapp.view.drawer.DrawerPosHelper;
import com.tokopedia.posapp.view.service.SchedulerService;
import com.tokopedia.tkpdreactnative.react.ReactUtils;

import javax.inject.Inject;

/**
 * Created by okasurya on 7/30/17.
 */

public class PosRouterApplication extends MainApplication implements
        TkpdCoreRouter, IDigitalModuleRouter, IReactNativeRouter, ReactApplication {

    private DaggerReactNativeComponent.Builder daggerReactNativeBuilder;
    private ReactNativeComponent reactNativeComponent;
    @Inject
    ReactNativeHost reactNativeHost;
    @Inject
    ReactUtils reactUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeDagger();
        initDaggerInjector();
    }

    @Override
    public void startInstopedActivity(Context context) {

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
    public void goToManageEtalase(Context context) {

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
    public void actionApplink(Activity activity, String linkUrl, String extras) {
        // TODO: 9/29/17 implement in other apps too
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
    }

    @Override
    public void goToProfileCompletion(Context context) {

    }

    @Override
    public void goToCreateMerchantRedirect(Context context) {

    }

    @Override
    public void goToRegister(Context context) {

    }

    @Override
    public Intent getLoginIntent(Context context) {
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
    public Intent instanceIntentTokoCashActivation() {
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
