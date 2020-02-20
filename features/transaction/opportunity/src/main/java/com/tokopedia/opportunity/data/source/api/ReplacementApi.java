package com.tokopedia.opportunity.data.source.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.opportunity.domain.entity.OpportunityDetail;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.opportunity.data.constant.OpportunityConstant;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by normansyahputa on 1/10/18.
 */

public interface ReplacementApi {
    @GET(OpportunityConstant.PATH_GET_OPPORTUNITY)
    Observable<Response<TkpdResponse>> getOpportunityList(@QueryMap Map<String, String> params);

    @GET(OpportunityConstant.PATH_GET_CATEGORY)
    Observable<Response<TkpdResponse>> getOpportunityCategory(@QueryMap Map<String, String> param);

    @GET(OpportunityConstant.NEW_PRICE_INFO)
    Observable<Response<TkpdResponse>> getOpportunityPriceInfo(@QueryMap Map<String, String> param);

    @GET(OpportunityConstant.PATH_GET_OPPORTUNITY_DETAIL)
    Observable<Response<DataResponse<OpportunityDetail>>> getOpportunityDetail(@QueryMap Map<String, String> param);
}
