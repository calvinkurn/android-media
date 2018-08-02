package com.tokopedia.digital_deals.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.fragment.CategoryDetailHomeFragment;
import com.tokopedia.digital_deals.view.fragment.FavouriteDealsFragment;

public class FavouriteDealsActivity extends DealsBaseActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_simple_deals_appbar;
    }

    @Override
    protected Fragment getNewFragment() {
        return FavouriteDealsFragment.createInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
