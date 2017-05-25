package com.tokopedia.core.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.base.di.component.AppComponent;

/**
 * Created by sebastianuskh on 12/8/16.
 * this class is temporary, after we done moving all the specific module from the core module
 * all the router will moved to the each module's router
 */
public interface TkpdCoreRouter {

    void startInstopedActivity(Context context);

    void startInstopedActivityForResult(Activity activity, int resultCode, int maxResult);

    void removeInstopedToken();

    void goToManageProduct(Context context);

    void clearEtalaseCache();

    Intent goToEditProduct(Context context, boolean isEdit, String productId);

    void resetAddProductCache(Context context);

    void goToWallet(Context context, Bundle bundle);

    void goToMerchantRedirect(Context context);

    Intent getHomeIntent(Context context);

    Class<?> getHomeClass(Context context) throws ClassNotFoundException;

    DrawerHelper getDrawer(AppCompatActivity activity,
                           SessionHandler sessionHandler,
                           LocalCacheHandler drawerCache);


    void onLogout(AppComponent appComponent);
}
