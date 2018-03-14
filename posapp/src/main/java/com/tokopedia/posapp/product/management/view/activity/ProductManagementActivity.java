package com.tokopedia.posapp.product.management.view.activity;


import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.posapp.product.management.view.fragment.ProductManagementFragment;

/**
 * @author okasurya on 3/12/18.
 */

public class ProductManagementActivity extends BaseSimpleActivity {
    @Override
    protected Fragment getNewFragment() {
        return ProductManagementFragment.newInstance();
    }

    @Override
    protected String getTagFragment() {
        return ProductManagementFragment.TAG;
    }
}
