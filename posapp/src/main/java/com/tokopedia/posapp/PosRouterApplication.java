package com.tokopedia.posapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.subscriber.ProfileCompletionSubscriber;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.posapp.view.activity.OutletActivity;
import com.tokopedia.posapp.deeplink.DeepLinkDelegate;
import com.tokopedia.posapp.view.drawer.DrawerPosHelper;

/**
 * Created by okasurya on 7/30/17.
 */

public class PosRouterApplication extends MainApplication implements
    TkpdCoreRouter {

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

    }

    @Override
    public void actionApplink(Activity activity, String linkUrl) {
        DeepLinkDelegate deepLinkDelegate = DeeplinkHandlerActivity.getDelegateInstance();
        Intent intent = activity.getIntent();
        intent.setData(Uri.parse(linkUrl));
        deepLinkDelegate.dispatchFrom(activity, intent);
    }

    @Override
    public void actionOpenGeneralWebView(Activity activity, String mobileUrl) {

    }

    @Override
    public Intent getHomeIntent(Context context) {
        return new Intent(context, OutletActivity.class);
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
}
