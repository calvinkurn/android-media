package com.tokopedia.core.network.apiservices.digital;

import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author anggaprasetiyo on 2/23/17.
 */

public interface DigitalApi {

    @GET(TkpdBaseURL.DigitalApi.PATH_GET_CART)
    Observable<Response<String>> getCart(@QueryMap Map<String, String> params);

}
