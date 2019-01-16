package com.tokopedia.digital.product.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.digital.product.view.fragment.DigitalProductFragment;
import com.tokopedia.digital.product.view.model.DigitalCategoryDetailPassData;

import java.util.Objects;

import static com.tokopedia.digital.applink.DigitalApplinkConstant.DIGITAL_PRODUCT;
import static com.tokopedia.digital.categorylist.view.fragment.DigitalCategoryListFragment.PARAM_IS_COUPON_ACTIVE;

/**
 * @author anggaprasetiyo on 4/25/17.
 */

public class DigitalProductActivity extends BaseSimpleActivity
        implements DigitalProductFragment.ActionListener {

    private static final String KEY_IS_COUPON_APPLIED_APPLINK = "is_coupon_applied";
    public static final String EXTRA_CATEGORY_PASS_DATA = "EXTRA_CATEGORY_PASS_DATA";
    private static final String EXTRA_STATE_TITLE_TOOLBAR = "EXTRA_STATE_TITLE_TOOLBAR";

    private String titleToolbar;
    private DigitalCategoryDetailPassData passData;

    public static Intent newInstance(Context context, DigitalCategoryDetailPassData passData) {
        return new Intent(context, DigitalProductActivity.class)
                .putExtra(EXTRA_CATEGORY_PASS_DATA, passData);
    }

    public static Intent newInstance(Context context, DigitalCategoryDetailPassData passData, int isCouponApplied) {
        return new Intent(context, DigitalProductActivity.class)
                .putExtra(EXTRA_CATEGORY_PASS_DATA, passData)
                .putExtra(PARAM_IS_COUPON_ACTIVE, isCouponApplied);
    }

    @SuppressWarnings("unused")
    @DeepLink({ DIGITAL_PRODUCT })
    public static Intent getcallingIntent(Context context, Bundle extras) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        if (extras.getBoolean(DigitalRouter.Companion.getEXTRA_APPLINK_FROM_PUSH(), false)) {
            Intent homeIntent = ((DigitalRouter) context.getApplicationContext()).getHomeIntent(context);
            taskStackBuilder.addNextIntent(homeIntent);
        }
        boolean isFromWidget = false;
        if (!TextUtils.isEmpty(extras.getString(DigitalCategoryDetailPassData.PARAM_IS_FROM_WIDGET))) {
            isFromWidget = Boolean.valueOf(extras.getString(DigitalCategoryDetailPassData.PARAM_IS_FROM_WIDGET));
        }
        boolean isCouponApplied = false;
        if (!TextUtils.isEmpty(extras.getString(KEY_IS_COUPON_APPLIED_APPLINK))) {
            isCouponApplied = Objects.requireNonNull(extras.getString(KEY_IS_COUPON_APPLIED_APPLINK)).equals("1");
        }
        DigitalCategoryDetailPassData passData = new DigitalCategoryDetailPassData.Builder()
                .appLinks(uri.toString())
                .categoryId(extras.getString(DigitalCategoryDetailPassData.PARAM_CATEGORY_ID))
                .operatorId(extras.getString(DigitalCategoryDetailPassData.PARAM_OPERATOR_ID))
                .productId(extras.getString(DigitalCategoryDetailPassData.PARAM_PRODUCT_ID))
                .clientNumber(extras.getString(DigitalCategoryDetailPassData.PARAM_CLIENT_NUMBER))
                .isFromWidget(isFromWidget)
                .isCouponApplied(isCouponApplied)
                .build();

        Intent destination = DigitalProductActivity.newInstance(context, passData);
        destination.putExtra(DigitalRouter.Companion.getEXTRA_APPLINK_FROM_PUSH(), true);
        taskStackBuilder.addNextIntent(destination);
        return destination;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        passData = getIntent().getExtras().getParcelable(EXTRA_CATEGORY_PASS_DATA);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_STATE_TITLE_TOOLBAR, titleToolbar);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.titleToolbar = savedInstanceState.getString(EXTRA_STATE_TITLE_TOOLBAR);
        invalidateTitleToolBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateTitleToolBar();
        unregisterShake();
    }

    @Override
    public void updateTitleToolbar(String title) {
        this.titleToolbar = title;
        invalidateTitleToolBar();
    }

    private void invalidateTitleToolBar() {
        if (!TextUtils.isEmpty(titleToolbar)) toolbar.setTitle(titleToolbar);
    }

    @Override
    protected android.support.v4.app.Fragment getNewFragment() {
        return DigitalProductFragment.newInstance(
                passData.getCategoryId(),
                passData.getOperatorId(),
                passData.getProductId(),
                passData.getClientNumber(),
                passData.isFromWidget(),
                passData.isCouponApplied(),
                passData.getAdditionalETollBalance(),
                passData.getAdditionalETollLastUpdatedDate());
    }
}
