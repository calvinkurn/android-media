package com.tokopedia.opportunity.domain.model;

import com.tokopedia.opportunity.data.OpportunityFilterModel;
import com.tokopedia.opportunity.data.OpportunityModel;

/**
 * @author by nisie on 6/2/17.
 */

public class OpportunityFirstTimeModel {
    final OpportunityModel opportunityModel;
    final OpportunityFilterModel opportunityFilterModel;


    public OpportunityFirstTimeModel(OpportunityModel opportunityModel,
                                     OpportunityFilterModel opportunityFilterModel) {
        this.opportunityModel = opportunityModel;
        this.opportunityFilterModel = opportunityFilterModel;
    }

    public OpportunityModel getOpportunityModel() {
        return opportunityModel;
    }

    public OpportunityFilterModel getOpportunityFilterModel() {
        return opportunityFilterModel;
    }
}
