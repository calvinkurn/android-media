package com.tokopedia.core.router;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;

/**
 * Created by ricoharisin on 11/10/16.
 */

public class SellerRouter {

    public static final String EXTRA_STATE_TAB_POSITION = "tab";

    public static final int TAB_POSITION_SELLING_OPPORTUNITY = 1;
    public final static int TAB_POSITION_SELLING_NEW_ORDER = 2;
    public final static int TAB_POSITION_SELLING_CONFIRM_SHIPPING = 3;
    public final static int TAB_POSITION_SELLING_SHIPPING_STATUS = 4;
    public final static int TAB_POSITION_SELLING_TRANSACTION_LIST = 5;

    @Deprecated // use the variable in com.tokopedia.seller module instead, because the extras belong to each module, this just a copy
    public interface ShopSettingConstant{
        String FRAGMENT_TO_SHOW = "FragmentToShow";
        String CREATE_SHOP_FRAGMENT_TAG = "CreateShopFragment";
    }

    private static TkpdCoreRouter getRouterFromContext (Context context) {
        Application application = null;
        if (context instanceof Activity) {
            application = ((Activity) context).getApplication();
        } else if (context instanceof Service) {
            application = ((Service) context).getApplication();
        } else if (context instanceof Application) {
            application = (Application) context;
        }
        if (application == null) {
            return getDefaultRouter();
        } else {
            return (TkpdCoreRouter) application;
        }
    }

    private static TkpdCoreRouter getDefaultRouter() {
        return (TkpdCoreRouter) MainApplication.getInstance();
    }

    public static Intent getActivityShopCreateEdit(Context context, boolean isCreate, boolean logOutOnBack){
        return getRouterFromContext(context).getIntentCreateEditShop(context, isCreate, logOutOnBack);
    }

    public static Intent getActivityManageShop(Context context) {
        return getRouterFromContext(context).getIntentManageShop(context);
    }

    public static Fragment getFragmentShopSettings(Context context) {
        return getRouterFromContext(context).getFragmentShopSettings();
    }

    public static Fragment getFragmentSellingNewOrder(Context context) {
        return getRouterFromContext(context).getFragmentSellingNewOrder();
    }

    public static Intent getActivitySplashScreenActivity(Context context) {
        return getRouterFromContext(context).getIntentSellerSplashScreen(context);
    }

    public static Class<?> getSellingActivityClass() {
        return getDefaultRouter().getSellingActivityClass();
    }

    public static Intent getActivitySellingTransactionNewOrder(Context context) {
        return getRouterFromContext(context).getActivitySellingTransactionNewOrder(context);
    }

    public static Intent getActivitySellingTransactionConfirmShipping(Context context) {
        return getRouterFromContext(context).getActivitySellingTransactionConfirmShipping(context);
    }

    public static Intent getActivitySellingTransactionShippingStatus(Context context) {
        return getRouterFromContext(context).getActivitySellingTransactionShippingStatus(context);
    }

    public static Intent getActivitySellingTransactionList(Context context) {
        return getRouterFromContext(context).getActivitySellingTransactionList(context);
    }

    public static Intent getActivitySellingTransactionOpportunity(Context context) {
        return getRouterFromContext(context).getActivitySellingTransactionOpportunity(context);
    }
}