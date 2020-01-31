package com.tokopedia.core.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.subscriber.ProfileCompletionSubscriber;
import com.tokopedia.core.util.SessionHandler;

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

    DrawerHelper getDrawer(AppCompatActivity activity,
                           SessionHandler sessionHandler,
                           LocalCacheHandler drawerCache,
                           GlobalCacheManager globalCacheManager);


    void onLogout(AppComponent appComponent);

    void goToCreateMerchantRedirect(Context context);

    Intent getLoginIntent(Context context);

    Intent getRegisterIntent(Context context);

    void getUserInfo(RequestParams empty, ProfileCompletionSubscriber profileSubscriber);

    String getFlavor();

    boolean isSupportedDelegateDeepLink(String appLinks);

    Intent getSplashScreenIntent(Context context);

    Class getDeepLinkClass();

    android.app.Fragment getFragmentShopSettings();

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

    Intent getHomeHotlistIntent(Context context);

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

    Intent getTopProfileIntent(Context context, String userId);

    Intent getGroupChatIntent(Context context, String channelUrl);

    Intent getInboxChannelsIntent(Context context);

    Intent getInboxTalkCallingIntent(Context context);

    Intent getManageAdressIntent(Context context);

    Intent getCreateResCenterActivityIntent(Context context, String orderId);
}