package com.tokopedia.core.network.apiservices.kero.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.shipping.model.kero.Rates;


import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Herdi_WORK on 19.09.16.
 */
public interface KeroApi {
    @GET(TkpdBaseURL.Shipment.PATH_RATES)
    Observable<Response<Rates>> calculateShippingRate(@QueryMap TKPDMapParam<String, String> stringStringMap);
}
