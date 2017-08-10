package com.tokopedia.core.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.subscriber.ProfileCompletionSubscriber;
import com.tokopedia.core.drawer2.view.subscriber.ProfileSubscriber;
import com.tokopedia.core.util.SessionHandler;

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

    void goToManageEtalase(Context context);

    void clearEtalaseCache();

    Intent goToEditProduct(Context context, boolean isEdit, String productId);

    void resetAddProductCache(Context context);

    void goToWallet(Context context, String url);

    void goToMerchantRedirect(Context context);

    void actionAppLink(Context context, String linkUrl);

    Intent getHomeIntent(Context context);

    Class<?> getHomeClass(Context context) throws ClassNotFoundException;

    DrawerHelper getDrawer(AppCompatActivity activity,
                           SessionHandler sessionHandler,
                           LocalCacheHandler drawerCache,
                           GlobalCacheManager globalCacheManager);


    void onLogout(AppComponent appComponent);

    void goToProfileCompletion(Context context);

    void goToCreateMerchantRedirect(Context context);

    void goToRegister(Context context);

    Intent getLoginIntent(Context context);

    Intent getRegisterIntent(Context context);

    void getUserInfo(RequestParams empty, ProfileCompletionSubscriber profileSubscriber);

    Intent getInboxReputationIntent(Context context);
}
