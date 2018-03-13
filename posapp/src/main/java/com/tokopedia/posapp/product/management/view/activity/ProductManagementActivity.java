package com.tokopedia.posapp.product.management.view.activity;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.base.activity.ReactDrawerPresenterActivity;
import com.tokopedia.posapp.product.management.view.fragment.ProductManagementFragment;
import com.tokopedia.posapp.product.productlist.view.fragment.ReactProductListFragment;

/**
 * @author okasurya on 3/12/18.
 */

public class ProductManagementActivity extends ReactDrawerPresenterActivity {
    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initView() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(
                    R.id.container,
                    ProductManagementFragment.newInstance(),
                    ProductManagementFragment.TAG
            );
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.POS_PRODUCT_MANAGEMENET;
    }
}
