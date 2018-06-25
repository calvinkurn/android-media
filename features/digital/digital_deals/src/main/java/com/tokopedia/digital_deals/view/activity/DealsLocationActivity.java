package com.tokopedia.digital_deals.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.fragment.SelectLocationFragment;


public class DealsLocationActivity extends BaseSimpleActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_simple_deals;
    }

    @Override
    protected Fragment getNewFragment() {
        return SelectLocationFragment.createInstance();
    }
}
