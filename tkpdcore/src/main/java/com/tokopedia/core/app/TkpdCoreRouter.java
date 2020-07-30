package com.tokopedia.core.app;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.base.di.component.AppComponent;

/**
 * Created by sebastianuskh on 12/8/16.
 * this class is temporary, after we done moving all the specific module from the core module
 * all the router will moved to the each module's router
 */
public interface TkpdCoreRouter {

    void resetAddProductCache(Context context);

    Intent getHomeIntent(Context context);

    void onLogout(AppComponent appComponent);

    boolean isSupportedDelegateDeepLink(String appLinks);

    Intent getSplashScreenIntent(Context context);
}