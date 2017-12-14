package com.tokopedia.core.router;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.util.RouterUtils;

/**
 * Created by ricoharisin on 11/10/16.
 */

public class SellerRouter {

    @Deprecated // use the variable in com.tokopedia.seller module instead, because the extras belong to each module, this just a copy
    public interface ShopSettingConstant{
        String FRAGMENT_TO_SHOW = "FragmentToShow";
        String CREATE_SHOP_FRAGMENT_TAG = "CreateShopFragment";
    }

    public static Intent getActivityShopCreateEdit(Context context, boolean isCreate, boolean logOutOnBack){
        return RouterUtils.getRouterFromContext(context).getIntentCreateEditShop(context, isCreate, logOutOnBack);
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

    public static Intent getActivitySellingTransactionOpportunity(Context context) {
        return RouterUtils.getRouterFromContext(context).getActivitySellingTransactionOpportunity(context);
    }
}