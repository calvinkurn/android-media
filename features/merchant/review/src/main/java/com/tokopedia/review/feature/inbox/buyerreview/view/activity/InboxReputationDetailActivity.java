package com.tokopedia.review.feature.inbox.buyerreview.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.review.feature.inbox.buyerreview.analytics.ReputationTracking;
import com.tokopedia.review.feature.inbox.buyerreview.view.fragment.InboxReputationDetailFragment;


import java.util.Objects;


/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailActivity extends BaseSimpleActivity implements HasComponent {

    public static final String ARGS_POSITION = "ARGS_POSITION";
    public static final String ARGS_TAB = "ARGS_TAB";
    public static final String ARGS_IS_FROM_APPLINK = "ARGS_IS_FROM_APPLINK";
    public static final String REPUTATION_ID = "reputation_id";
    public static final String CACHE_PASS_DATA = InboxReputationDetailActivity.class.getName() + "-passData";
    public static final String DEFAULT_REPUTATION_ID = "0";

    private ReputationTracking reputationTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reputationTracking = new ReputationTracking();
    }

    @Nullable
    @Override
    protected Fragment getNewFragment() {

        int tab = -1;
        boolean isFromApplink = false;
        String reputationId = DEFAULT_REPUTATION_ID;
        Uri intentData = getIntent().getData();
        Bundle intentExtras = getIntent().getExtras();

        // if from applink
        if(intentData != null && intentData.getPathSegments().size() >= 2) {
            isFromApplink = true;
            reputationId = intentData.getPathSegments().get(1);
        }

        if(intentExtras != null && !isFromApplink) {
            if(intentExtras.getInt(ARGS_TAB, -1) != -1) {
                tab = intentExtras.getInt(ARGS_TAB);
            }
            reputationId = intentExtras.getString(REPUTATION_ID, "");
            isFromApplink = intentExtras.getBoolean(ARGS_IS_FROM_APPLINK, false);
        }

        return InboxReputationDetailFragment.createInstance(tab, isFromApplink, reputationId);
    }

    @Override
    public BaseAppComponent getComponent() {
        return ((BaseMainApplication) getApplication()).getBaseAppComponent();
    }

    public static Intent getCallingIntent(Context context,
                                          int adapterPosition, int tab) {
        Intent intent = new Intent(context, InboxReputationDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(InboxReputationDetailActivity.ARGS_POSITION, adapterPosition);
        bundle.putInt(InboxReputationDetailActivity.ARGS_TAB, tab);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getCallingIntent(Context context,
                                          String reputationId) {
        Intent intent = new Intent(context, InboxReputationDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(REPUTATION_ID, reputationId);
        bundle.putBoolean(ARGS_IS_FROM_APPLINK, true);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(getFragment() != null && getFragment() instanceof InboxReputationDetailFragment) {
            InboxReputationDetailFragment fragment = (InboxReputationDetailFragment) getFragment();
            reputationTracking.onClickBackButtonReputationDetailTracker(Objects.requireNonNull(fragment).getOrderId());
        }
        return super.onOptionsItemSelected(item);
    }


}
