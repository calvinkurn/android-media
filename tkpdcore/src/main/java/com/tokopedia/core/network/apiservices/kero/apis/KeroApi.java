package com.tokopedia.core.network.apiservices.kero.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Herdi_WORK on 19.09.16.
 */
public interface KeroApi {
    @GET(TkpdBaseURL.Shipment.PATH_RATES)
    Observable<Response<String>> calculateShippingRate(@QueryMap TKPDMapParam<String, String> stringStringMap);

    @GET(TkpdBaseURL.Shipment.PATH_DISTRICT_RECOMMENDATION)
    Observable<Response<String>> getDistrictRecommendation(
            @QueryMap TKPDMapParam<String, String> params
    );

}
