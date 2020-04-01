package com.tokopedia.opportunity.domain.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.opportunity.domain.entity.OpportunityDetail;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.opportunity.data.AcceptReplacementModel;
import com.tokopedia.opportunity.data.OpportunityFilterModel;
import com.tokopedia.opportunity.data.OpportunityModel;
import com.tokopedia.opportunity.data.OpportunityNewPriceData;
import com.tokopedia.opportunity.data.source.CloudActionReplacementSource;
import com.tokopedia.opportunity.data.source.CloudGetFilterOpportunitySource;
import com.tokopedia.opportunity.data.source.CloudGetListOpportunitySource;

import rx.Observable;

/**
 * Created by hangnadi on 3/3/17.
 */

public class ReplacementRepositoryImpl implements ReplacementRepository {

    private CloudGetListOpportunitySource cloudGetListOpportunitySource;
    private CloudGetFilterOpportunitySource cloudGetFilterOpportunitySource;
    private CloudActionReplacementSource cloudActionReplacementSource;

    public ReplacementRepositoryImpl(CloudGetListOpportunitySource cloudGetListOpportunitySource,
                                     CloudGetFilterOpportunitySource cloudGetFilterOpportunitySource,
                                     CloudActionReplacementSource cloudActionReplacementSource){
        this.cloudGetListOpportunitySource = cloudGetListOpportunitySource;
        this.cloudGetFilterOpportunitySource = cloudGetFilterOpportunitySource;
        this.cloudActionReplacementSource = cloudActionReplacementSource;
    }

    @Override
    public Observable<AcceptReplacementModel> acceptReplacement(TKPDMapParam<String, Object> parameters) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(parameters);

        return cloudActionReplacementSource
                .acceptReplacement(requestParams);
    }

    @Override
    public Observable<OpportunityModel> getOpportunityListFromNetwork(TKPDMapParam<String, Object> parameters) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(parameters);
        return cloudGetListOpportunitySource.getOpportunityList(requestParams);
    }

    @Override
    public Observable<OpportunityFilterModel> getOpportunityCategoryFromNetwork(TKPDMapParam<String, Object> parameters) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(parameters);

        return cloudGetFilterOpportunitySource.getFilter(requestParams);
    }

    @Override
    public Observable<OpportunityNewPriceData> getOpportunityReplacementNewPrice(RequestParams parameters) {
        return cloudGetListOpportunitySource.getOpportunityNewPrice(parameters);
    }

    @Override
    public Observable<OpportunityDetail> getOpportunityDetail(RequestParams parameters) {
        return cloudGetListOpportunitySource.getOpportunityDetail(parameters);
    }

}
