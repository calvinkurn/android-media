package com.tokopedia.opportunity.viewmodel.opportunitylist;

import com.tokopedia.core.database.model.PagingHandler;

import java.util.ArrayList;

/**
 * Created by nisie on 3/6/17.
 */

public class OpportunityViewModel {
    ArrayList<OpportunityItemViewModel> listOpportunity;
    PagingHandler.PagingHandlerModel pagingHandlerModel;

    public ArrayList<OpportunityItemViewModel> getListOpportunity() {
        return listOpportunity;
    }

    public void setListOpportunity(ArrayList<OpportunityItemViewModel> listOpportunity) {
        this.listOpportunity = listOpportunity;
    }

    public PagingHandler.PagingHandlerModel getPagingHandlerModel() {
        return pagingHandlerModel;
    }

    public void setPagingHandlerModel(PagingHandler.PagingHandlerModel pagingHandlerModel) {
        this.pagingHandlerModel = pagingHandlerModel;
    }
}
