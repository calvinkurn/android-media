package com.tokopedia.core.router;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.util.RouterUtils;

/**
 * Created by ricoharisin on 11/10/16.
 */

public class SellerRouter {

    private static final String ACTIVITY_SELLING_TRANSACTION = "com.tokopedia.seller.selling.view.activity.ActivitySellingTransaction";

    private static final String FRAGMENT_SELLING_NEW_ORDER = "com.tokopedia.seller.selling.view.fragment.FragmentSellingNewOrder";

    private static final String ACTIVITY_SHOP_CREATE_EDIT = "com.tokopedia.seller.shop.ShopEditorActivity";

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

    public static Intent getActivitySellingTransaction(Context context) {
        return RouterUtils.getActivityIntent(context, ACTIVITY_SELLING_TRANSACTION);
    }

    public static Fragment getFragmentSellingNewOrder(Context context) {
        return Fragment.instantiate(context, FRAGMENT_SELLING_NEW_ORDER);
    }

    public static ComponentName getActivitySellingTransactionName(Context context) {
        return RouterUtils.getActivityComponentName(context, ACTIVITY_SELLING_TRANSACTION);
    }
}