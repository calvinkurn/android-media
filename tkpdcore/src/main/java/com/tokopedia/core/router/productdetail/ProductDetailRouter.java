package com.tokopedia.core.router.productdetail;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
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
    public static final String SHARE_DATA = "SHARE_DATA";
    public static final String IS_ADDING_PRODUCT = "IS_ADDING_PRODUCT";
    public static final String ARG_PARAM_PRODUCT_PASS_DATA = "ARG_PARAM_PRODUCT_PASS_DATA";
    public static final String ARG_FROM_DEEPLINK = "ARG_FROM_DEEPLINK";
    public static final String WISHLIST_STATUS_UPDATED_POSITION = "wishlistUpdatedPosition";
    public static final String WIHSLIST_STATUS_IS_WISHLIST = "isWishlist";

    public static Intent createInstanceProductDetailInfoActivity(Context context, ProductPass data) {
        Intent intent = RouterUtils.getActivityIntent(context, PRODUCT_DETAIL_INFO_ACTIVITY);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        intent.putExtra(EXTRA_PRODUCT_PASS, data);
        return intent;
    }

    public static Intent createInstanceProductDetailInfoActivity(Context context, String productId) {
        Intent intent = RouterUtils.getActivityIntent(context, PRODUCT_DETAIL_INFO_ACTIVITY);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        return intent;
    }

    public static Intent createInstanceProductDetailInfoActivity(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, PRODUCT_DETAIL_INFO_ACTIVITY);
        return intent;
    }

    public static Intent createAddProductDetailInfoActivity(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, PRODUCT_DETAIL_INFO_ACTIVITY);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_ADDING_PRODUCT, true);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent createInstanceProductDetailInfoActivity(
            Context context, ShareData shareData) {
        Intent intent = RouterUtils.getActivityIntent(context, PRODUCT_DETAIL_INFO_ACTIVITY);
        Bundle bundle = new Bundle();
        bundle.putParcelable(SHARE_DATA, shareData);
        intent.putExtras(bundle);
        return intent;
    }

    public static Fragment instanceDeeplink(Context context, @NonNull ProductPass productPass) {
        Fragment fragment = Fragment.instantiate(context, PRODUCT_DETAIL_FRAGMENT);
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_PRODUCT_PASS_DATA, productPass);
        args.putBoolean(ARG_FROM_DEEPLINK, true);
        fragment.setArguments(args);
        return fragment;
    }

}
