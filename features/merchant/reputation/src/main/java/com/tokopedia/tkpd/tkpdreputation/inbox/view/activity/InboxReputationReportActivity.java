package com.tokopedia.tkpd.tkpdreputation.inbox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationReportFragment;


/**
 * @author by nisie on 9/13/17.
 */

public class InboxReputationReportActivity extends BasePresenterActivity
        implements HasComponent {

    public static final String ARGS_SHOP_ID = "ARGS_SHOP_ID";
    public static final String ARGS_REVIEW_ID = "ARGS_REVIEW_ID";

    @Override
    public BaseAppComponent getComponent() {
        return getBaseAppComponent();
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

        String reviewId = getIntent().getExtras().getString(ARGS_REVIEW_ID, "");
        int shopId = getIntent().getExtras().getInt(ARGS_SHOP_ID);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (InboxReputationReportFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = InboxReputationReportFragment.createInstance(reviewId, shopId);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,
                fragment,
                fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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

    public static Intent getCallingIntent(Context context, int shopId, String reviewId) {
        Intent intent = new Intent(context, InboxReputationReportActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_SHOP_ID, shopId);
        bundle.putString(ARGS_REVIEW_ID, reviewId);
        intent.putExtras(bundle);
        return intent;
    }


    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
