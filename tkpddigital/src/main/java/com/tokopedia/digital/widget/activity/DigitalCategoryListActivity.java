package com.tokopedia.digital.widget.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.digital.R;
import com.tokopedia.digital.widget.fragment.DigitalCategoryListFragment;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public class DigitalCategoryListActivity extends BasePresenterActivity {

    public static Intent newInstance(Context context) {
        return new Intent(context, DigitalCategoryListActivity.class);
    }

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
        return R.layout.activity_digital_category_list_digital_module;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof DigitalCategoryListFragment))
            getFragmentManager().beginTransaction().replace(R.id.container,
                    DigitalCategoryListFragment.newInstance()).commit();
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }
}
