package com.tokopedia.posapp.outlet.data.source;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by okasurya on 8/4/17.
 */

public interface OutletApi {
    @GET("v4/people/" + TkpdBaseURL.User.PATH_GET_ADDRESS)
    Observable<Response<TkpdResponse>> getAddress(@QueryMap Map<String, String> params);
}
