package com.tokopedia.digital.product.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.fragment.DigitalProductFragment;

/**
 * @author anggaprasetiyo on 4/25/17.
 */

public class DigitalProductActivity extends BasePresenterActivity {
    private static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";
    private String categoryId;

    public static Intent newInstance(Context context, String categoryId) {
        return new Intent(context, DigitalProductActivity.class)
                .putExtra(EXTRA_CATEGORY_ID, categoryId);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.categoryId = extras.getString(EXTRA_CATEGORY_ID);
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
                    DigitalProductFragment.newInstance(categoryId)).commit();
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
