package com.tokopedia.opportunity.domain.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.opportunity.domain.entity.OpportunityDetail;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.opportunity.data.AcceptReplacementModel;
import com.tokopedia.opportunity.data.OpportunityFilterModel;
import com.tokopedia.opportunity.data.OpportunityModel;
import com.tokopedia.opportunity.data.OpportunityNewPriceData;

import rx.Observable;

/**
 * Created by hangnadi on 3/3/17.
 */
public interface ReplacementRepository {

    Observable<AcceptReplacementModel> acceptReplacement(TKPDMapParam<String, Object> parameters);

    Observable<OpportunityModel> getOpportunityListFromNetwork(TKPDMapParam<String, Object> parameters);

    Observable<OpportunityFilterModel> getOpportunityCategoryFromNetwork(TKPDMapParam<String, Object> parameters);

    Observable<OpportunityNewPriceData> getOpportunityReplacementNewPrice(RequestParams parameters);

    Observable<OpportunityDetail> getOpportunityDetail(RequestParams parameters);

}
