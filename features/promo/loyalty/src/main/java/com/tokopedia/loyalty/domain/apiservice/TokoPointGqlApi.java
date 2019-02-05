package com.tokopedia.loyalty.domain.apiservice;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by sachinbansal on 5/15/18.
 */

public interface TokoPointGqlApi {
    @POST("./")
    Observable<Response<String>> getPointDrawer(@Body String params);
}
