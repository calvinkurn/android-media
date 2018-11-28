package com.tokopedia.topads.dashboard.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;

/**
 * Created by hadi.putra on 16/05/18.
 */

public class SellerCenterActivity extends BaseSimpleActivity {
    private static final String SELLER_CENTER_URL = "https://seller.tokopedia.com/topads/";

    @Override
    protected Fragment getNewFragment() {
        return BaseSessionWebViewFragment.newInstance(SELLER_CENTER_URL);
    }
}
