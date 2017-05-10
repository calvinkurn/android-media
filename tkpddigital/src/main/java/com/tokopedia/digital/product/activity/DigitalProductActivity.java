package com.tokopedia.digital.product.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.fragment.DigitalProductFragment;

/**
 * @author anggaprasetiyo on 4/25/17.
 */

public class DigitalProductActivity extends BasePresenterActivity {
    private static final String EXTRA_CATEGORY_PASS_DATA = "EXTRA_CATEGORY_PASS_DATA";
    private DigitalCategoryDetailPassData passData;

    public static Intent newInstance(Context context, DigitalCategoryDetailPassData passData) {
        return new Intent(context, DigitalProductActivity.class)
                .putExtra(EXTRA_CATEGORY_PASS_DATA, passData);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.passData = extras.getParcelable(EXTRA_CATEGORY_PASS_DATA);
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
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof DigitalProductFragment))
            getFragmentManager().beginTransaction().replace(R.id.container,
                    DigitalProductFragment.newInstance(passData.getCategoryId())).commit();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }


}
