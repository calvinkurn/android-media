package com.tokopedia.affiliate.feature.tracking.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.analytics.AffiliateAnalytics;
import com.tokopedia.affiliate.feature.tracking.di.AffTrackingComponent;
import com.tokopedia.affiliate.feature.tracking.di.AffTrackingModule;
import com.tokopedia.affiliate.feature.tracking.di.DaggerAffTrackingComponent;
import com.tokopedia.affiliate.feature.tracking.view.contract.AffContract;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AffiliateTrackingActivity extends BaseActivity implements AffContract.View {

    private static final String EXTRA_APPLINK = "EXTRA_APPLINK";
    private static final String EXTRA_APPLINK_UNSUPPORTED = "EXTRA_APPLINK_UNSUPPORTED";

    @Inject
    AffContract.Presenter presenter;

    @Inject
    AffiliateAnalytics affiliateAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiliate_tracking);
        initInjector();
        presenter.attachView(this);
        handleIntent();
    }

    private void handleIntent() {
        try {
            Uri data = getIntent().getData();
            if (data != null && data.isHierarchical()) {
                List<String> path = new ArrayList<>();
                String affName = data.getPathSegments().get(0);
                for (int i = 1; i < data.getPathSegments().size(); i++) {
                    path.add(data.getPathSegments().get(i));
                }
                presenter.getTrackingUrl(affName, TextUtils.join("/", path));
                affiliateAnalytics.onAfterClickTokopediMe(data.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            handleError();
        }
    }

    protected void initInjector() {
        AffTrackingComponent component = DaggerAffTrackingComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .affTrackingModule(new AffTrackingModule())
                .build();
        component.inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void handleLink(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (RouteManager.isSupportApplink(this, url)) {
                startHomeActivity(url);
            } else {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        } else {
            handleError();
        }
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void handleError() {
        RouteManager.route(this, ApplinkConst.HOME);
        finishActivity();
    }

    private void startHomeActivity(String applink) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);

        Intent homeIntent = RouteManager.getIntent(this, ApplinkConst.HOME);
        taskStackBuilder.addNextIntent(homeIntent);

        Intent intent = RouteManager.getIntent(this, applink);
        taskStackBuilder.addNextIntent(intent);

        taskStackBuilder.startActivities();
    }
}
