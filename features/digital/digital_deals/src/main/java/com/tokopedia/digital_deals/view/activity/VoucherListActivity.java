package com.tokopedia.digital_deals.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital_deals.view.fragment.VoucherListFragment;

public class VoucherListActivity extends BaseSimpleActivity{
    @Override
    protected Fragment getNewFragment() {
        return VoucherListFragment.createInstance();
    }
}
