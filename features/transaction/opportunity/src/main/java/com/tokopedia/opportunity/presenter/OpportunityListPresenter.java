package com.tokopedia.opportunity.presenter;

import androidx.annotation.Nullable;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.opportunity.listener.OpportunityListView;
import com.tokopedia.opportunity.viewmodel.opportunitylist.FilterPass;

import java.util.ArrayList;

/**
 * Created by nisie on 3/2/17.
 */

public abstract class OpportunityListPresenter extends BaseDaggerPresenter<OpportunityListView> {
    public abstract void getOpportunity(@Nullable String query,
                        @Nullable ArrayList<FilterPass> listFilter);

    public abstract void unsubscribeObservable();

    public abstract void initOpportunityForFirstTime(@Nullable String query,
                                     @Nullable ArrayList<FilterPass> listFilter);
}
