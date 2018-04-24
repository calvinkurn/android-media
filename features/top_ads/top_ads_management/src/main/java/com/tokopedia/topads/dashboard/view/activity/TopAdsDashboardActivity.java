package com.tokopedia.topads.dashboard.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.topads.R;
import com.tokopedia.topads.TopAdsModuleRouter;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashboardFragment;

/**
 * Created by hadi.putra on 23/04/2018.
 */
public class TopAdsDashboardActivity extends DrawerPresenterActivity implements HasComponent<TopAdsComponent> {
    public static final String TAG = TopAdsDashboardActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new TopAdsDashboardFragment(), TAG).commit();
        }
    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.SELLER_TOP_ADS;
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
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {
        actionSendAnalyticsIfFromPushNotif();
    }

    private void actionSendAnalyticsIfFromPushNotif() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.EXTRA_FROM_PUSH)) {
            if (intent.getBooleanExtra(Constants.EXTRA_FROM_PUSH, false)) {
                UnifyTracking.eventOpenTopadsPushNotification(
                        getIntent().getStringExtra(UnifyTracking.EXTRA_LABEL)
                );
            }
        }
    }


    @Override
    public TopAdsComponent getComponent() {
        return ((TopAdsModuleRouter) getApplication()).getTopAdsComponent();
    }
}
