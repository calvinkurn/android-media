package com.tokopedia.sellerapp.dashboard.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.dashboard.presenter.FCMRegistrationPresenter;
import com.tokopedia.sellerapp.dashboard.view.fragment.DashboardFragment;

import javax.inject.Inject;

/**
 * Created by nathan on 9/5/17.
 */

public class DashboardActivity extends DrawerPresenterActivity {

    public static final String TAG = DashboardActivity.class.getSimpleName();

    @Inject
    FCMRegistrationPresenter fcmRegistrationPresenter;

    public static Intent createInstance(Activity activity) {
        Intent intent = new Intent(activity, DashboardActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, DashboardFragment.newInstance(), TAG)
                    .commit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDrawerData();
        NotificationModHandler.showDialogNotificationIfNotShowing(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fcmRegistrationPresenter.detachView();
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
        return 0;
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

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.SELLER_INDEX_HOME;
    }
}
