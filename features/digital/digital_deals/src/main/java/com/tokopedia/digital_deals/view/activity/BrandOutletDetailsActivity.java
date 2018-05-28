package com.tokopedia.digital_deals.view.activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital_deals.R;

public class BrandOutletDetailsActivity extends BaseSimpleActivity {
    @Override
    protected Fragment getNewFragment() {
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_redeemed_voucher_code;
    }
}
