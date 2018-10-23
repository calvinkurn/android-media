package com.tokopedia.core.router;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.util.RouterUtils;

/**
 * Created by ricoharisin on 11/10/16.
 */
@Deprecated
public class SellerRouter {

    public static Intent getActivityShopCreateEdit(Context context){
        return RouterUtils.getRouterFromContext(context).getIntentCreateShop(context);
    }


    public static Intent getActivityManageShop(Context context) {
        return RouterUtils.getRouterFromContext(context).getIntentManageShop(context);
    }

    public static Fragment getFragmentShopSettings(Context context) {
        return RouterUtils.getRouterFromContext(context).getFragmentShopSettings();
    }

    public static Fragment getFragmentSellingNewOrder(Context context) {
        return RouterUtils.getRouterFromContext(context).getFragmentSellingNewOrder();
    }

    public static Intent getActivitySplashScreenActivity(Context context) {
        return RouterUtils.getRouterFromContext(context).getSplashScreenIntent(context);
    }

    public static Class<?> getSellingActivityClass() {
        return RouterUtils.getDefaultRouter().getSellingActivityClass();
    }

    public static Intent getActivitySellingTransactionNewOrder(Context context) {
        return RouterUtils.getRouterFromContext(context).getActivitySellingTransactionNewOrder(context);
    }

    public static Intent getActivitySellingTransactionConfirmShipping(Context context) {
        return RouterUtils.getRouterFromContext(context).getActivitySellingTransactionConfirmShipping(context);
    }

    public static Intent getActivitySellingTransactionShippingStatus(Context context) {
        return RouterUtils.getRouterFromContext(context).getActivitySellingTransactionShippingStatus(context);
    }

    public static Intent getActivitySellingTransactionList(Context context) {
        return RouterUtils.getRouterFromContext(context).getActivitySellingTransactionList(context);
    }

    public static Intent getActivitySellingTransactionOpportunity(Context context, String query) {
        return RouterUtils.getRouterFromContext(context).getActivitySellingTransactionOpportunity(context, query);
    }


}