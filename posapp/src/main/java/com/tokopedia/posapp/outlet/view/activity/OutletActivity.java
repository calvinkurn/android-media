package com.tokopedia.posapp.outlet.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.base.activity.PosDrawerActivity;
import com.tokopedia.posapp.outlet.view.fragment.OutletFragment;

/**
 * @author okasurya on 7/31/17
 */
public class OutletActivity extends PosDrawerActivity {

    public static Intent newTopIntent(Context context) {
        Intent intent = new Intent(context, OutletActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    public void onGetNotificationDrawer(DrawerNotification notification) {

    }

    @Override
    public void onGetNotif() {

    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.POS_OUTLET;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        setupToolbar();

        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }

        OutletFragment fragment = OutletFragment.createInstance(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentByTag(
                OutletFragment.class.getSimpleName()) == null) {
            fragmentTransaction.replace(R.id.container,
                    fragment,
                    fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(R.id.container,
                    getSupportFragmentManager().findFragmentByTag(
                            OutletFragment.class.getSimpleName()));
        }

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
