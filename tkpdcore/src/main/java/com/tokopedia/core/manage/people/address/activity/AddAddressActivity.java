package com.tokopedia.core.manage.people.address.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.fragment.AddAddressFragment;
import com.tokopedia.core.manage.people.address.model.AddressModel;

/**
 * Created by nisie on 9/6/16.
 */
public class AddAddressActivity extends BasePresenterActivity implements ManageAddressConstant {

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_ADD_ADDRESS_FORM;
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
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        Bundle bundle = new Bundle();
        if (getSupportActionBar() != null
                && getIntent().getExtras() != null
                && getIntent().getExtras().getBoolean(IS_EDIT)) {
            bundle = getIntent().getExtras();
            getSupportActionBar().setTitle(getString(R.string.title_update_address));
        }
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            AddAddressFragment fragment = AddAddressFragment.createInstance(bundle);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        }
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

    public static Intent createInstance(Activity activity) {
        return new Intent(activity, AddAddressActivity.class);
    }

    public static Intent createInstance(Activity activity, AddressModel data) {
        Intent intent = new Intent(activity, AddAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ManageAddressConstant.EDIT_PARAM, data.convertToDestination());
        bundle.putBoolean(ManageAddressConstant.IS_EDIT, true);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
