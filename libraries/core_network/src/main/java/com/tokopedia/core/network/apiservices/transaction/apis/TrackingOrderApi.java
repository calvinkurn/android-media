package com.tokopedia.core.network.apiservices.transaction.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public interface TrackingOrderApi {

    @GET(TkpdBaseURL.Transaction.PATH_TRACK_ORDER)
    Observable<Response<TkpdResponse>> trackOrder(@QueryMap Map<String, String> params);
}
