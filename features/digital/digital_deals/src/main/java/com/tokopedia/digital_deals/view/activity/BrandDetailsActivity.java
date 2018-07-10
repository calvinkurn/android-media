package com.tokopedia.digital_deals.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.fragment.BrandDetailsFragment;
import com.tokopedia.digital_deals.view.fragment.DealDetailsFragment;

public class BrandDetailsActivity extends BaseSimpleActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_simple_deals;
    }

    @Override
    protected Fragment getNewFragment() {
        return BrandDetailsFragment.createInstance(getIntent().getExtras());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setVisibility(View.GONE);

    }
}