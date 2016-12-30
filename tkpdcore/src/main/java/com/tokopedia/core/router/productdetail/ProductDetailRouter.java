package com.tokopedia.core.router.productdetail;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.util.RouterUtils;

/**
 * @author anggaprasetiyo on 12/22/16.
 */

public class ProductDetailRouter {
    private final static String PRODUCT_DETAIL_INFO_ACTIVITY
            = "com.tokopedia.core.product.activity.ProductInfoActivity";
    public static final String EXTRA_PRODUCT_PASS = "EXTRA_PRODUCT_PASS";
    public static final String EXTRA_PRODUCT_ITEM = "EXTRA_PRODUCT_ITEM";
    public static final String EXTRA_PRODUCT_ID = "product_id";

    public static Intent createInstanceProductDetailInfoActivity(Context context, ProductPass data) {
        Intent intent = RouterUtils.getActivityIntent(context, PRODUCT_DETAIL_INFO_ACTIVITY);
        intent.putExtra(EXTRA_PRODUCT_PASS, data);
        return intent;
    }

    public static Intent createInstanceProductDetailInfoActivity(Context context, String productId) {
        Intent intent = RouterUtils.getActivityIntent(context, PRODUCT_DETAIL_INFO_ACTIVITY);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        return intent;
    }
}
