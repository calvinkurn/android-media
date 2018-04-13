package com.tokopedia.core.network.apiservices.logistics.apis;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by sachinbansal on 4/6/18.
 */

public interface LogisticsApi {

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<String>> getLogisticsData(@Body String requestBody);
}
