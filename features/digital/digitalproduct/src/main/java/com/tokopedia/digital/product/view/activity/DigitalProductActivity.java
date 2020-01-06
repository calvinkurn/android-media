package com.tokopedia.digital.product.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.common_digital.common.constant.DigitalExtraParam;
import com.tokopedia.common_digital.common.presentation.model.DigitalCategoryDetailPassData;
import com.tokopedia.digital.product.view.fragment.DigitalProductFragment;

import java.util.Objects;

/**
 * @author anggaprasetiyo on 4/25/17.
 */

public class DigitalProductActivity extends BaseSimpleActivity
        implements DigitalProductFragment.ActionListener {

    private static final String KEY_IS_COUPON_APPLIED_APPLINK = "is_coupon_applied";
    private static final String EXTRA_STATE_TITLE_TOOLBAR = "EXTRA_STATE_TITLE_TOOLBAR";

    private String titleToolbar;
    private DigitalCategoryDetailPassData passData;

    public static Intent newInstance(Context context, DigitalCategoryDetailPassData passData) {
        return new Intent(context, DigitalProductActivity.class)
                .putExtra(DigitalExtraParam.EXTRA_CATEGORY_PASS_DATA, passData);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Uri uriData = getIntent().getData();
        if (uriData != null && uriData.getQueryParameterNames().size() > 0) {
            boolean isFromWidget = false;
            if (!TextUtils.isEmpty(uriData.getQueryParameter(DigitalCategoryDetailPassData.PARAM_IS_FROM_WIDGET))) {
                isFromWidget = Boolean.valueOf(uriData.getQueryParameter(DigitalCategoryDetailPassData.PARAM_IS_FROM_WIDGET));
            }
            boolean isCouponApplied = false;
            if (!TextUtils.isEmpty(uriData.getQueryParameter(KEY_IS_COUPON_APPLIED_APPLINK))) {
                isCouponApplied = Objects.requireNonNull(uriData.getQueryParameter(KEY_IS_COUPON_APPLIED_APPLINK)).equals("1");
            }
            DigitalCategoryDetailPassData passData = new DigitalCategoryDetailPassData.Builder()
                    .appLinks(uriData.toString())
                    .categoryId(uriData.getQueryParameter(DigitalCategoryDetailPassData.PARAM_CATEGORY_ID))
                    .operatorId(uriData.getQueryParameter(DigitalCategoryDetailPassData.PARAM_OPERATOR_ID))
                    .productId(uriData.getQueryParameter(DigitalCategoryDetailPassData.PARAM_PRODUCT_ID))
                    .clientNumber(uriData.getQueryParameter(DigitalCategoryDetailPassData.PARAM_CLIENT_NUMBER))
                    .isFromWidget(isFromWidget)
                    .isCouponApplied(isCouponApplied)
                    .build();
            this.passData = passData;
        } else {
            passData = getIntent().getExtras().getParcelable(DigitalExtraParam.EXTRA_CATEGORY_PASS_DATA);
        }
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
        if (!TextUtils.isEmpty(titleToolbar)) {
            updateTitle(titleToolbar);
        }
    }

    @Override
    protected androidx.fragment.app.Fragment getNewFragment() {
        return DigitalProductFragment.newInstance(
                passData.getCategoryId(),
                passData.getOperatorId(),
                passData.getProductId(),
                passData.getClientNumber(),
                passData.isFromWidget(),
                passData.isCouponApplied(),
                passData.getAdditionalETollBalance(),
                passData.getAdditionalETollLastUpdatedDate(),
                passData.getAdditionalETollOperatorName());
    }
}
