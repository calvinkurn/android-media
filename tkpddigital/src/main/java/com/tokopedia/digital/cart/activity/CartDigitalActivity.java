package com.tokopedia.digital.cart.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.router.digitalmodule.passdata.ExampPassData;
import com.tokopedia.digital.R;
import com.tokopedia.digital.cart.fragment.CartDigitalFragment;

/**
 * @author anggaprasetiyo on 2/21/17.
 */

public class CartDigitalActivity extends BasePresenterActivity {
    private static final String EXTRA_PASS_DIGITAL_CART_DATA = "EXTRA_PASS_DIGITAL_CART_DATA";
    private ExampPassData passData;


    public static Intent newInstance(Context context, ExampPassData passData) {
        return new Intent(context, CartDigitalActivity.class)
                .putExtra(EXTRA_PASS_DIGITAL_CART_DATA, passData);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        passData = extras.getParcelable(EXTRA_PASS_DIGITAL_CART_DATA);
    }

    @Override
    protected void initialPresenter() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cart_digital_module;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {
        getFragmentManager().beginTransaction().replace(R.id.container,
                CartDigitalFragment.newInstance(passData)).commit();

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }


}
