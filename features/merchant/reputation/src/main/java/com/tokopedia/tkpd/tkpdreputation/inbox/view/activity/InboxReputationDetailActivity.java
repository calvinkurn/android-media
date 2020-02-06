package com.tokopedia.tkpd.tkpdreputation.inbox.view.activity;

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
import com.tokopedia.tkpd.tkpdreputation.analytic.ReputationTracking;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationDetailFragment;

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

        if(intentData != null && intentExtras != null) {
            isFromApplink = (intentData.getPathSegments().size() > 0);
            if(isFromApplink) {
                reputationId = intentData.getLastPathSegment();
            } else {
                if(intentExtras.getInt(ARGS_TAB, -1) != -1) {
                    tab = intentExtras.getInt(ARGS_TAB);
                }
                reputationId = intentExtras.getString(REPUTATION_ID, "");
            }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(getFragment() != null && getFragment() instanceof InboxReputationDetailFragment) {
            InboxReputationDetailFragment fragment = (InboxReputationDetailFragment) getFragment();
            reputationTracking.onClickBackButtonReputationDetailTracker(Objects.requireNonNull(fragment).getOrderId());
        }
        return super.onOptionsItemSelected(item);
    }


}
