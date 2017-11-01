package com.tokopedia.core.manage.people.address.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.manage.people.address.fragment.DistrictRecomendationFragment;

public class DistrictRecommendationActivity extends BasePresenterActivity {

    private static final String TAG = "DISTRICT_RECOMMENDATION_FRAGMENT";

    public static Intent createInstance(Activity activity) {
        return new Intent(activity, DistrictRecommendationActivity.class);
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
        DistrictRecomendationFragment fragment = DistrictRecomendationFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.add(R.id.container, fragment, TAG);
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

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_clear_24dp);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
