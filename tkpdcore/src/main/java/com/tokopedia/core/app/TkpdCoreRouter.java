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

    Intent getRegisterIntent(Context context);

    boolean isSupportedDelegateDeepLink(String appLinks);

    Intent getSplashScreenIntent(Context context);

    Intent getPhoneVerificationActivationIntent(Context context);

    /**
     * Only for sellerapp
     */
    @Deprecated
    Intent getLoginGoogleIntent(Context context);

    /**
     * Only for sellerapp
     */
    @Deprecated
    Intent getLoginFacebookIntent(Context context);

    /**
     * Only for sellerapp
     */
    @Deprecated
    Intent getLoginWebviewIntent(Context context, String name, String url);

    Intent getCreateResCenterActivityIntent(Context context, String orderId);
}