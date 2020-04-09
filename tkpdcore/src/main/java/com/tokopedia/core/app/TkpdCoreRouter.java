package com.tokopedia.core.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.base.di.component.AppComponent;

/**
 * Created by sebastianuskh on 12/8/16.
 * this class is temporary, after we done moving all the specific module from the core module
 * all the router will moved to the each module's router
 */
public interface TkpdCoreRouter {

    void goToManageProduct(Context context);

    void clearEtalaseCache();

    void resetAddProductCache(Context context);

    /**
     * deprecated
     *
     * @param activity activity context
     * @param linkUrl  applinkScheme
     * @see #actionApplinkFromActivity(Activity, String)
     */
    @Deprecated
    void actionApplink(Activity activity, String linkUrl);

    Intent getHomeIntent(Context context);

    void onLogout(AppComponent appComponent);

    void goToCreateMerchantRedirect(Context context);

    Intent getLoginIntent(Context context);

    Intent getRegisterIntent(Context context);

    boolean isSupportedDelegateDeepLink(String appLinks);

    Intent getSplashScreenIntent(Context context);

    Class getSellingActivityClass();

    /**
     * Only for sellerapp
     */
    @Deprecated
    Intent getActivitySellingTransactionNewOrder(Context context);

    /**
     * Only for sellerapp
     */
    @Deprecated
    Intent getActivitySellingTransactionConfirmShipping(Context context);

    Intent getActivitySellingTransactionShippingStatus(Context context);

    Intent getActivitySellingTransactionList(Context context);

    Intent getActivitySellingTransactionOpportunity(Context context, String query);

    Intent getInboxReputationIntent(Context context);

    /**
     * Only for sellerapp
     */
    @Deprecated
    Intent getResolutionCenterIntentSeller(Context context);

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

    Intent getShopPageIntent(Context context, String shopId);

    Intent getGroupChatIntent(Context context, String channelUrl);

    Intent getCreateResCenterActivityIntent(Context context, String orderId);
}