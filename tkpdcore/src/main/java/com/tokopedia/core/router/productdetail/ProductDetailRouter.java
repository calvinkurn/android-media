package com.tokopedia.core.router.productdetail;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.util.RouterUtils;

/**
 * @author anggaprasetiyo on 12/22/16.
 */

public class ProductDetailRouter {
    private final static String PRODUCT_DETAIL_INFO_ACTIVITY
            = "com.tokopedia.tkpdpdp.ProductInfoActivity";
    public static final String PRODUCT_DETAIL_FRAGMENT
            = "com.tokopedia.tkpdpdp.fragment.ProductDetailFragment";
    public static final String EXTRA_PRODUCT_PASS = "EXTRA_PRODUCT_PASS";
    public static final String EXTRA_PRODUCT_ITEM = "EXTRA_PRODUCT_ITEM";
    public static final String EXTRA_PRODUCT_ID = "product_id";
    public static final String IS_ADDING_PRODUCT = "IS_ADDING_PRODUCT";
    public static final String WISHLIST_STATUS_UPDATED_POSITION = "wishlistUpdatedPosition";
    public static final String WIHSLIST_STATUS_IS_WISHLIST = "isWishlist";

    public static Intent createInstanceProductDetailInfoActivity(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, PRODUCT_DETAIL_INFO_ACTIVITY);
        return intent;
    }

}
