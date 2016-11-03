package com.tokopedia.tkpd.network.apiservices.kero.apis;

import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.shipping.model.kero.Rates;


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
