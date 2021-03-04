package com.tokopedia.review.feature.inbox.buyerreview.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.review.feature.inbox.buyerreview.view.fragment.InboxReputationReportFragment;


/**
 * @author by nisie on 9/13/17.
 */

public class InboxReputationReportActivity extends BaseSimpleActivity
        implements HasComponent {

    public static final String ARGS_SHOP_ID = "ARGS_SHOP_ID";
    public static final String ARGS_REVIEW_ID = "ARGS_REVIEW_ID";

    @Nullable
    @Override
    protected Fragment getNewFragment() {
        String reviewId = getIntent().getExtras().getString(ARGS_REVIEW_ID, "");
        int shopId = getIntent().getExtras().getInt(ARGS_SHOP_ID);

        return InboxReputationReportFragment.createInstance(reviewId, shopId);
    }

    @Override
    public BaseAppComponent getComponent() {
        return ((BaseMainApplication) getApplication()).getBaseAppComponent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public static Intent getCallingIntent(Context context, int shopId, String reviewId) {
        Intent intent = new Intent(context, InboxReputationReportActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_SHOP_ID, shopId);
        bundle.putString(ARGS_REVIEW_ID, reviewId);
        intent.putExtras(bundle);
        return intent;
    }

}
