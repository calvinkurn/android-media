package com.tokopedia.opportunity.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.opportunity.analytics.OpportunityTrackingEventLabel;
import com.tokopedia.opportunity.fragment.OpportunityTncFragment;
import com.tokopedia.opportunity.viewmodel.opportunitylist.OpportunityItemViewModel;

public class OpportunityTncActivity extends BaseSimpleActivity implements OpportunityTncFragment.OnOpportunityFragmentListener{

    public static final String OPPORTUNITY_EXTRA_PARAM = "param_opp";

    private OpportunityItemViewModel opportunityItemViewModel;

    public static Intent createIntent(Context context, OpportunityItemViewModel opportunityItemViewModel) {
        Intent intent = new Intent(context, OpportunityTncActivity.class);
        intent.putExtra(OPPORTUNITY_EXTRA_PARAM, opportunityItemViewModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        opportunityItemViewModel = getIntent().getParcelableExtra(OPPORTUNITY_EXTRA_PARAM);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UnifyTracking.eventOpportunity(
                this,
                OpportunityTrackingEventLabel.EventName.CLICK_OPPORTUNITY_TAKE_NO,
                OpportunityTrackingEventLabel.EventCategory.OPPORTUNITY_FILTER,
                AppEventTracking.Action.CLICK,
                OpportunityTrackingEventLabel.EventLabel.NO
        );
    }

    @Override
    protected Fragment getNewFragment() {
        return OpportunityTncFragment.newInstance();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

    @Override
    public OpportunityItemViewModel getItemViewModel() {
        return opportunityItemViewModel;
    }
}
