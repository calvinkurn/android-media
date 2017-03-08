package com.tokopedia.sellerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.drawer.DrawerVariable;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.instoped.InstopedActivity;
import com.tokopedia.seller.instoped.presenter.InstagramMediaPresenterImpl;
import com.tokopedia.seller.myproduct.ManageProduct;
import com.tokopedia.seller.myproduct.ProductActivity;
import com.tokopedia.seller.myproduct.presenter.AddProductPresenterImpl;
import com.tokopedia.sellerapp.drawer.DrawerVariableSeller;
import com.tokopedia.sellerapp.home.view.SellerHomeActivity;

/**
 * Created by normansyahputa on 12/15/16.
 */

public class SellerRouterApplication extends MainApplication implements TkpdCoreRouter, SellerModuleRouter {
    @Override
    public DrawerVariable getDrawer(AppCompatActivity activity) {
        return new DrawerVariableSeller(activity);
    }

    @Override
    public void startInstopedActivity(Context context) {
        InstopedActivity.startInstopedActivity(context);
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
        return ProductActivity.moveToEditFragment(context, isEdit, productId);
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
    public void goToHome(Context context) {
        Intent intent = new Intent(context,
                SellerHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void goToProductDetail(Context context, String productUrl) {
        DeepLinkChecker.openProduct(productUrl, context);
    }

    private void goToDefaultRoute(Context context) {
        Intent intent = new Intent(context,
                SellerHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
