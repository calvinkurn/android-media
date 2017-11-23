package com.tokopedia.core.router;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.util.RouterUtils;
import com.tokopedia.core.util.SessionHandler;

import static com.facebook.internal.NativeProtocol.EXTRA_USER_ID;

/**
 * Created by ricoharisin on 11/10/16.
 */

public class SellerRouter {

    private static final String ACTIVITY_SELLING_TRANSACTION = "com.tokopedia.seller.selling.view.activity.ActivitySellingTransaction";
    private static final String ACTIVITY_TOPADS_DASHBOARD = "com.tokopedia.seller.topads.dashboard.view.activity.TopAdsDashboardActivity";
    private static final String ACTIVITY_MANAGE_SHOP = "com.tokopedia.seller.shopsettings.ManageShopActivity";

    private static final String FRAGMENT_SELLING_NEW_ORDER = "com.tokopedia.seller.selling.view.fragment.FragmentSellingNewOrder";
    private static final String FRAGMENT_SHOP_SETTINGS = "com.tokopedia.seller.shopsettings.FragmentSettingShop";

    private static final String ACTIVITY_SHOP_CREATE_EDIT = "com.tokopedia.seller.shop.ShopEditorActivity";

    private static final String ACTIVITY_SPLASH_SCREEN = "com.tokopedia.sellerapp.SplashScreenActivity";

    public static final String EXTRA_STATE_TAB_POSITION = "tab";
    public static final String EXTRA_USER_ID = "user_id";


    public static final int TAB_POSITION_SELLING_OPPORTUNITY = 1;
    public final static int TAB_POSITION_SELLING_NEW_ORDER = 2;
    public final static int TAB_POSITION_SELLING_CONFIRM_SHIPPING = 3;
    public final static int TAB_POSITION_SELLING_SHIPPING_STATUS = 4;
    public final static int TAB_POSITION_SELLING_TRANSACTION_LIST = 5;

    public interface ShopSettingConstant{
        String FRAGMENT_TO_SHOW = "FragmentToShow";

        String EDIT_SHOP_FRAGMENT_TAG = "EditShopFragment";
        String CREATE_SHOP_FRAGMENT_TAG = "CreateShopFragment";

        String ON_BACK = "ON_BACK";
        String LOG_OUT = "LOG_OUT";
        String FINISH = "FINISH";
    }

    public static Intent getAcitivityShopCreateEdit(Context context){
        return RouterUtils.getActivityIntent(context, ACTIVITY_SHOP_CREATE_EDIT);
    }

    public static Intent getActivityManageShop(Context context) {
        return RouterUtils.getActivityIntent(context, ACTIVITY_MANAGE_SHOP);
    }

    public static Intent getActivitySellingTransaction(Context context) {
        return RouterUtils.getActivityIntent(context, ACTIVITY_SELLING_TRANSACTION);
    }

    public static Fragment getFragmentShopSettings(Context context) {
        return Fragment.instantiate(context, FRAGMENT_SHOP_SETTINGS);
    }

    public static Fragment getFragmentSellingNewOrder(Context context) {
        return Fragment.instantiate(context, FRAGMENT_SELLING_NEW_ORDER);
    }

    public static Intent getActivityTopadsDashboard(Context context) {
        return RouterUtils.getActivityIntent(context, ACTIVITY_TOPADS_DASHBOARD);
    }

    public static ComponentName getActivitySellingTransactionName(Context context) {
        return RouterUtils.getActivityComponentName(context, ACTIVITY_SELLING_TRANSACTION);
    }

    public static Intent getAcitivitySplashScreenActivity(Context context) {
        return RouterUtils.getActivityIntent(context, ACTIVITY_SPLASH_SCREEN);
    }

    public static Class<?> getSellingActivityClass() {
        Class<?> parentIndexHomeClass = null;
        try {
            parentIndexHomeClass = RouterUtils.getActivityClass(ACTIVITY_SELLING_TRANSACTION);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return parentIndexHomeClass;
    }

    public static Intent getActivitySellingTransactionNewOrder(Context context) {
        Intent intent = getActivitySellingTransaction(context);
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_STATE_TAB_POSITION, TAB_POSITION_SELLING_NEW_ORDER);
        bundle.putString(EXTRA_USER_ID, SessionHandler.getLoginID(context));
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getActivitySellingTransactionConfirmShipping(Context context) {
        Intent intent = getActivitySellingTransaction(context);
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_STATE_TAB_POSITION, TAB_POSITION_SELLING_CONFIRM_SHIPPING);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getActivitySellingTransactionShippingStatus(Context context) {
        Intent intent = getActivitySellingTransaction(context);
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_STATE_TAB_POSITION, TAB_POSITION_SELLING_SHIPPING_STATUS);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getActivitySellingTransactionList(Context context) {
        Intent intent = getActivitySellingTransaction(context);
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_STATE_TAB_POSITION, TAB_POSITION_SELLING_TRANSACTION_LIST);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getActivitySellingTransactionOpportunity(Context context) {
        Intent intent = getActivitySellingTransaction(context);
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_STATE_TAB_POSITION, TAB_POSITION_SELLING_OPPORTUNITY);
        intent.putExtras(bundle);
        return intent;
    }
}