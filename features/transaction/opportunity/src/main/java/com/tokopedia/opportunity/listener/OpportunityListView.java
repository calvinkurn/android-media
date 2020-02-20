package com.tokopedia.opportunity.listener;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.opportunity.adapter.OpportunityListAdapter;
import com.tokopedia.opportunity.viewmodel.opportunitylist.OpportunityFilterViewModel;
import com.tokopedia.opportunity.viewmodel.opportunitylist.OpportunityViewModel;

/**
 * Created by nisie on 3/2/17.
 */
public interface OpportunityListView extends CustomerView {
    void showLoadingList();

    void onSuccessGetOpportunity(OpportunityViewModel viewModel);

    String getString(int resId);

    void onErrorGetOpportunity(String errorMessage);

    OpportunityListAdapter getAdapter();

    Activity getActivity();

    boolean isFilterEmpty();

    int getPage();

    void disableView();

    void onErrorFirstTime(String errorMessage);

    void onSuccessFirstTime(OpportunityViewModel opportunityViewModel, OpportunityFilterViewModel opportunityFilterViewModel);
}
