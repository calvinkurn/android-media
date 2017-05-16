package com.tokopedia.sellerapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.drawer.DrawerVariable;
import com.tokopedia.core.inboxreputation.listener.SellerFragmentReputation;
import com.tokopedia.core.router.TkpdFragmentWrapper;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.welcome.WelcomeActivity;
import com.tokopedia.payment.activity.TopPayActivity;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.logout.TkpdSellerLogout;
import com.tokopedia.seller.instoped.InstopedActivity;
import com.tokopedia.seller.instoped.presenter.InstagramMediaPresenterImpl;
import com.tokopedia.seller.myproduct.ManageProduct;
import com.tokopedia.seller.myproduct.presenter.AddProductPresenterImpl;
import com.tokopedia.seller.product.view.activity.ProductEditActivity;
import com.tokopedia.seller.reputation.view.fragment.SellerReputationFragment;
import com.tokopedia.sellerapp.drawer.DrawerVariableSeller;
import com.tokopedia.sellerapp.home.view.SellerHomeActivity;

/**
 * Created by normansyahputa on 12/15/16.
 */

public class SellerRouterApplication extends MainApplication
        implements TkpdCoreRouter, SellerModuleRouter, SellerFragmentReputation {
    public static final String COM_TOKOPEDIA_SELLERAPP_HOME_VIEW_SELLER_HOME_ACTIVITY = "com.tokopedia.sellerapp.home.view.SellerHomeActivity";
    public static final String COM_TOKOPEDIA_CORE_WELCOME_WELCOME_ACTIVITY = "com.tokopedia.core.welcome.WelcomeActivity";

    @Override
    public DrawerVariable getDrawer(AppCompatActivity activity) {
        return new DrawerVariableSeller(activity);
    }

    @Override
    public void startInstopedActivity(Context context) {
        InstopedActivity.startInstopedActivity(context);
    }

    @Override
    public void startInstopedActivityForResult (Activity activity, int resultCode, int maxResult){
        InstopedActivity.startInstopedActivityForResult(activity, resultCode,maxResult);
    }

    @Override
    public void removeInstopedToken() {
        InstagramMediaPresenterImpl.removeToken();
    }

    @Override
    public void goToManageProduct(Context context) {
        Intent intent = new Intent(context, ManageProduct.class);
        startActivity(intent);
    }

    @Override
    public void clearEtalaseCache() {
        AddProductPresenterImpl.clearEtalaseCache(getApplicationContext());
    }

    @Override
    public Intent goToEditProduct(Context context, boolean isEdit, String productId) {
        return ProductEditActivity.createInstance(context, productId);
    }

    @Override
    public void resetAddProductCache(Context context) {
        AddProductPresenterImpl.clearEtalaseCache(context);
        AddProductPresenterImpl.clearDepartementCache(context);
    }

    @Override
    public void goToWallet(Context context, Bundle bundle) {
        //no route to wallet on seller, go to default
        goToDefaultRoute(context);
    }

    @Override
    public void goToMerchantRedirect(Context context) {
        //no route to merchant redirect on seller, go to default
        goToDefaultRoute(context);
    }

    @Override
    public Intent getHomeIntent(Context context) {
        if (SessionHandler.isV4Login(context)) {
            return new Intent(context, SellerHomeActivity.class);
        } else {
            return new Intent(context, WelcomeActivity.class);
        }
    }

    @Override
    public Class<?> getHomeClass(Context context) throws ClassNotFoundException {
        if (SessionHandler.isV4Login(context)) {
            return Class.forName(COM_TOKOPEDIA_SELLERAPP_HOME_VIEW_SELLER_HOME_ACTIVITY);
        } else {
            return Class.forName(COM_TOKOPEDIA_CORE_WELCOME_WELCOME_ACTIVITY);
        }
    }

    @Override
    public void onLogout(AppComponent appComponent) {
        TkpdSellerLogout.onLogOut(appComponent);
    }

    @Override
    public void goToHome(Context context) {
        Intent intent = getHomeIntent(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void goToProductDetail(Context context, String productUrl) {
        DeepLinkChecker.openProduct(productUrl, context);
    }

    @Override
    public void goToTkpdPayment(Context context, String url, String parameter, String callbackUrl, Integer paymentId) {
        PaymentPassData paymentPassData = new PaymentPassData();
        paymentPassData.setRedirectUrl(url);
        paymentPassData.setQueryString(parameter);
        paymentPassData.setCallbackSuccessUrl(callbackUrl);
        paymentPassData.setPaymentId(String.valueOf(paymentId));
        Intent intent = TopPayActivity.createInstance(context, paymentPassData);
        context.startActivity(intent);

    }

    private void goToDefaultRoute(Context context) {
        Intent intent = new Intent(context,
                SellerHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public TkpdFragmentWrapper getSellerReputationFragment(Context context) {
        return new TkpdFragmentWrapper(
                context.getString(R.string.header_review_reputation),
                SellerReputationFragment.TAG,
                SellerReputationFragment.createInstance());
    }
}
