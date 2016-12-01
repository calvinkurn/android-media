package com.tokopedia.core.router;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.GCMListenerService;
import com.tokopedia.core.util.RouterUtils;

/**
 * Created by ricoharisin on 11/10/16.
 */

public class SellerRouter {

    private static final String ACTIVITY_SELLING_TRANSACTION = "com.tokopedia.seller.selling.view.activity.ActivitySellingTransaction";

    private static final String FRAGMENT_SELLING_NEW_ORDER = "com.tokopedia.seller.selling.view.fragment.FragmentSellingNewOrder";

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