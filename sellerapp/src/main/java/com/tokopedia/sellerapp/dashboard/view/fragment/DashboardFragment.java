package com.tokopedia.sellerapp.dashboard.view.fragment;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;

/**
 * Created by nathan on 9/6/17.
 */

public class DashboardFragment extends BaseDaggerFragment {

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
