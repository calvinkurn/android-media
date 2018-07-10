package com.tokopedia.posapp.product.management.view.activity;


import android.content.DialogInterface;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.posapp.product.management.view.fragment.ProductManagementFragment;
import com.tokopedia.posapp.product.management.view.listener.EditProductListener;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;

/**
 * @author okasurya on 3/12/18.
 */

public class ProductManagementActivity extends BaseSimpleActivity implements EditProductListener {
    @Override
    protected Fragment getNewFragment() {
        return ProductManagementFragment.newInstance();
    }

    @Override
    protected String getTagFragment() {
        return ProductManagementFragment.TAG;
    }

    public void onDialogDismiss(ProductViewModel productViewModel, int position) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getTagFragment());
        if(fragment != null && fragment instanceof ProductManagementFragment) {
            ((ProductManagementFragment) fragment).onSucessUpdateLocalPrice(productViewModel, position);
        }
    }
}
