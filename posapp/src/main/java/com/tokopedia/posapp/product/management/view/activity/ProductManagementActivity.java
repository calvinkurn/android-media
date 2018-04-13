package com.tokopedia.posapp.product.management.view.activity;


import android.content.DialogInterface;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.posapp.product.management.view.fragment.ProductManagementFragment;

/**
 * @author okasurya on 3/12/18.
 */

public class ProductManagementActivity extends BaseSimpleActivity implements DialogInterface.OnDismissListener {
    @Override
    protected Fragment getNewFragment() {
        return ProductManagementFragment.newInstance();
    }

    @Override
    protected String getTagFragment() {
        return ProductManagementFragment.TAG;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getTagFragment());
        if(fragment != null && fragment instanceof ProductManagementFragment) {
            ((ProductManagementFragment) fragment).reloadData();
        }
    }
}
