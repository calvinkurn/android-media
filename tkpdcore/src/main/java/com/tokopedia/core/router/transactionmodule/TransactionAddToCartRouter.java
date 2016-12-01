package com.tokopedia.core.router.transactionmodule;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.util.RouterUtils;

/**
 * @author anggaprasetiyo on 11/18/16.
 */

public class TransactionAddToCartRouter {
    private final static String ADD_TO_CART_ACTIVITY
            = "com.tokopedia.transaction.addtocart.activity.AddToCartActivity";
    public static final String EXTRA_PRODUCT_CART = "EXTRA_PRODUCT_CART";

    public static Intent createInstanceAddToCartActivity(Context context, ProductCartPass data) {
        Intent intent = RouterUtils.getActivityIntent(context, ADD_TO_CART_ACTIVITY);
        intent.putExtra(EXTRA_PRODUCT_CART, data);
        return intent;
    }

}
