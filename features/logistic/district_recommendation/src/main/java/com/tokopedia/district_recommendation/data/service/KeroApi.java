package com.tokopedia.district_recommendation.data.service;

import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.utils.TKPDMapParam;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Herdi_WORK on 19.09.16.
 */
public interface KeroApi {

    @GET(TkpdBaseURL.Shipment.PATH_DISTRICT_RECOMMENDATION)
    Observable<Response<String>> getDistrictRecommendation(
            @QueryMap TKPDMapParam<String, String> params
    );

}
