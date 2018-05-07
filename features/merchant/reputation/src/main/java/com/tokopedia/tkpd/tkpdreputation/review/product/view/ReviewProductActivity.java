package com.tokopedia.tkpd.tkpdreputation.review.product.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;

import com.tokopedia.tkpd.tkpdreputation.R;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ReviewProductActivity extends BaseSimpleActivity implements HasComponent<AppComponent> {

    public static final String EXTRA_PRODUCT_NAME = "x_prd_nm";

    public static Intent createIntent(Context context, String productId, String productName) {
        Intent intent = new Intent(context, ReviewProductActivity.class);
        intent.putExtra(ReviewProductFragment.EXTRA_PRODUCT_ID, productId);
        intent.putExtra(EXTRA_PRODUCT_NAME, productName);
        return intent;
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        if (getSupportActionBar()!= null) {
            String productName = getIntent().getExtras().getString(EXTRA_PRODUCT_NAME);
            getSupportActionBar().setTitle(getString(R.string.title_activity_reputation) + " " + productName);
        }
    }

    @Override
    protected Fragment getNewFragment() {
        String productId = getIntent().getExtras().getString(ReviewProductFragment.EXTRA_PRODUCT_ID);
        return ReviewProductFragment.getInstance(productId);
    }

    @Override
    public AppComponent getComponent() {
        return ((MainApplication) getApplication()).getAppComponent();
    }
}
