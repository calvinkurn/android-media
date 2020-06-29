package com.tokopedia.opportunity.presenter;

import androidx.annotation.Nullable;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.opportunity.domain.interactor.GetOpportunityFilterUseCase;
import com.tokopedia.opportunity.domain.interactor.GetOpportunityFirstTimeUseCase;
import com.tokopedia.opportunity.domain.interactor.GetOpportunityUseCase;
import com.tokopedia.opportunity.presenter.subscriber.GetOpportunityFirstTimeSubscriber;
import com.tokopedia.opportunity.presenter.subscriber.GetOpportunitySubscriber;
import com.tokopedia.opportunity.viewmodel.opportunitylist.FilterPass;

import java.util.ArrayList;

/**
 * Created by nisie on 3/2/17.
 */

public class OpportunityListPresenterImpl extends OpportunityListPresenter {


    private GetOpportunityUseCase getOpportunityUseCase;
    private GetOpportunityFilterUseCase getFilterUseCase;
    private GetOpportunityFirstTimeUseCase getOpportunityFirstTimeUseCase;
    private SessionHandler sessionHandler;

    private static final int PAGE_ONE = 1;


    public OpportunityListPresenterImpl(GetOpportunityUseCase getOpportunityUseCase, GetOpportunityFilterUseCase getFilterUseCase, GetOpportunityFirstTimeUseCase getOpportunityFirstTimeUseCase, SessionHandler sessionHandler) {
        this.getOpportunityUseCase = getOpportunityUseCase;
        this.getFilterUseCase = getFilterUseCase;
        this.getOpportunityFirstTimeUseCase = getOpportunityFirstTimeUseCase;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void getOpportunity(@Nullable String query,
                               @Nullable ArrayList<FilterPass> listFilter) {
        getView().showLoadingList();
        getView().disableView();
        getOpportunityUseCase.execute(GetOpportunityUseCase.getRequestParam(
                getView().getPage(),
                query,
                listFilter
        ), new GetOpportunitySubscriber(getView()));

    }

    @Override
    public void unsubscribeObservable() {
        getOpportunityUseCase.unsubscribe();
        getFilterUseCase.unsubscribe();
        getOpportunityFirstTimeUseCase.unsubscribe();
    }

    @Override
    public void initOpportunityForFirstTime(@Nullable String query,
                                            @Nullable ArrayList<FilterPass> listFilter, String shopId) {
        getView().showLoadingList();
        getView().disableView();
        getOpportunityFirstTimeUseCase.execute(
                GetOpportunityFirstTimeUseCase.getRequestParam(
                        PAGE_ONE,
                        query,
                        listFilter,
                        shopId),
                new GetOpportunityFirstTimeSubscriber(getView()));
    }
}
