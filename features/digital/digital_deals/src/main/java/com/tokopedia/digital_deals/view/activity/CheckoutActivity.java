package com.tokopedia.digital_deals.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital_deals.view.fragment.CategoryDetailHomeFragment;
import com.tokopedia.digital_deals.view.fragment.CheckoutHomeFragment;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;

public class CheckoutActivity extends BaseSimpleActivity {


    @Override
    protected Fragment getNewFragment() {
        return CheckoutHomeFragment.createInstance(getIntent().getExtras());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}

