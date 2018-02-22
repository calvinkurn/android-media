package com.tokopedia.core.router.transactionmodule;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.util.RouterUtils;

/**
 * @author anggaprasetiyo on 12/14/16.
 */

public class TransactionCartRouter {
    private final static String CART_ACTIVITY
            = "com.tokopedia.transaction.cart.activity.CartActivity";

    private final static String CART_ACTIVITY_NEW
            = "com.tokopedia.transaction.checkout.view.view.cartlist.CartActivity";

    public static Intent createInstanceCartActivity(Context context) {
        return RouterUtils.getActivityIntent(context, CART_ACTIVITY_NEW);
    }

    public static Class<?> createInstanceCartClass() throws ClassNotFoundException {
        return RouterUtils.getActivityClass(CART_ACTIVITY);
    }
}
