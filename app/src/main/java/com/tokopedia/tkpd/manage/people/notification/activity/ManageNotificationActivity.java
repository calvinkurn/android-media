package com.tokopedia.tkpd.manage.people.notification.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.app.BasePresenterActivity;
import com.tokopedia.tkpd.manage.people.notification.fragment.ManageNotificationFragment;

/**
 * Created by Nisie on 6/22/16.
 */
public class ManageNotificationActivity extends BasePresenterActivity {

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
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
        }
        ManageNotificationFragment fragment = ManageNotificationFragment.createInstance(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
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
