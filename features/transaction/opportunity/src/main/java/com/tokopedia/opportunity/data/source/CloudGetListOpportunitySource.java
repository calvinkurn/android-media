package com.tokopedia.opportunity.data.source;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.core.base.common.util.GetData;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.opportunity.domain.entity.OpportunityDetail;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.opportunity.data.OpportunityModel;
import com.tokopedia.opportunity.data.OpportunityNewPriceData;
import com.tokopedia.opportunity.data.mapper.OpportunityListMapper;
import com.tokopedia.opportunity.data.mapper.OpportunityNewPriceMapper;
import com.tokopedia.opportunity.data.source.api.ReplacementApi;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 1/10/18.
 */

public class CloudGetListOpportunitySource {

    private ReplacementApi replacementApi;
    private OpportunityListMapper opportunityListMapper;
    private OpportunityNewPriceMapper opportunityNewPriceMapper;
    private Context context;

    @Inject
    public CloudGetListOpportunitySource(ReplacementApi replacementApi, OpportunityListMapper opportunityListMapper,
                                         OpportunityNewPriceMapper opportunityNewPriceMapper,
                                         @ApplicationContext Context context) {
        this.replacementApi = replacementApi;
        this.opportunityListMapper = opportunityListMapper;
        this.opportunityNewPriceMapper = opportunityNewPriceMapper;
        this.context = context;
    }

    public Observable<OpportunityModel> getOpportunityList(RequestParams requestParams) {
        requestParams.putAll((HashMap<String, String>) AuthUtil.generateParams(context));
        return replacementApi
                .getOpportunityList(requestParams.getParamsAllValueInString())
                .map(opportunityListMapper);
    }

    public Observable<OpportunityNewPriceData> getOpportunityNewPrice(RequestParams requestParams){
        requestParams.putAll((HashMap<String, String>) AuthUtil.generateParams(context));
        return replacementApi
                .getOpportunityPriceInfo(requestParams.getParamsAllValueInString())
                .map(opportunityNewPriceMapper);
    }

    public Observable<OpportunityDetail> getOpportunityDetail(RequestParams requestParams) {
        requestParams.putAll(AuthUtil.generateParamsNetwork(context));
        return replacementApi
                .getOpportunityDetail(requestParams.getParamsAllValueInString())
                .map(new GetData<DataResponse<OpportunityDetail>>())
                .map(new Func1<DataResponse<OpportunityDetail>, OpportunityDetail>() {
                    @Override
                    public OpportunityDetail call(DataResponse<OpportunityDetail> opportunityDetailDataResponse) {
                        return opportunityDetailDataResponse.getData();
                    }
                });
    }
}
