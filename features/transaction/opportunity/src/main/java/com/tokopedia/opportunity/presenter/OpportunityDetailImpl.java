package com.tokopedia.opportunity.presenter;

import com.tokopedia.opportunity.listener.OpportunityDetailView;

/**
 * Created by hangnadi on 2/27/17.
 */
public class OpportunityDetailImpl implements OpportunityDetailPresenter {

    private final OpportunityDetailView viewListener;

    public OpportunityDetailImpl(OpportunityDetailView viewListener) {
        this.viewListener = viewListener;
    }
}
