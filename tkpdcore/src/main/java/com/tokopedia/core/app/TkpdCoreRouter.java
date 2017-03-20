package com.tokopedia.core.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tokopedia.core.drawer.DrawerVariable;

/**
 * Created by sebastianuskh on 12/8/16.
 * this class is temporary, after we done moving all the specific module from the core module
 * all the router will moved to the each module's router
 */
public interface TkpdCoreRouter {
    DrawerVariable getDrawer(AppCompatActivity activity);

    void startInstopedActivity(Context context);

    void removeInstopedToken();

    void goToManageProduct(Context context);

    void clearEtalaseCache();

    Intent goToEditProduct(Context context, boolean isEdit, String productId);

    void resetAddProductCache(Context context);

    void goToWallet(Context context, Bundle bundle);

    void goToMerchantRedirect(Context context);

    Intent getHomeIntent(Context context);

    Class<?> getHomeClass() throws ClassNotFoundException;
}
