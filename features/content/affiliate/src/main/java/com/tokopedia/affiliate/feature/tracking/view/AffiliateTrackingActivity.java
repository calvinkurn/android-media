package com.tokopedia.affiliate.feature.tracking.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.affiliate.feature.tracking.AffTrackingPresenter;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.tracking.di.AffTrackingComponent;
import com.tokopedia.affiliate.feature.tracking.di.AffTrackingModule;
import com.tokopedia.affiliate.feature.tracking.di.DaggerAffTrackingComponent;
import com.tokopedia.applink.ApplinkRouter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.hansel.pebbletracesdk.presets.UIPresets;

public class AffiliateTrackingActivity extends BaseActivity implements AffContract.View {


    public static final String TOKOPEDIA_HOME = "tokopedia://home";
    @Inject
    AffTrackingPresenter presenter;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiliate_tracking);
        progressBar = findViewById(R.id.loading);
        initInjector();
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
            }
        }catch (Exception e){
            e.printStackTrace();
            handleError();
        }
    }

    protected void initInjector(){
        AffTrackingComponent component = DaggerAffTrackingComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .affTrackingModule(new AffTrackingModule())
                .build();
        component.inject(this);
        component.inject(presenter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.attachView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.detachView();
    }

    @Override
    public void handleLink(String url) {
        if (!TextUtils.isEmpty(url)) {
            ApplinkRouter router = ((ApplinkRouter) getApplicationContext());
            if (router.isSupportApplink(url)) {
                router.goToApplinkActivity(this, url);
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
        ApplinkRouter router = ((ApplinkRouter) getApplicationContext());
        router.goToApplinkActivity(this, TOKOPEDIA_HOME);
        finishActivity();
    }
}
