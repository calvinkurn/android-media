package com.tokopedia.core.dynamicfilter.facade;

import com.tokopedia.core.dynamicfilter.facade.models.HadesV1Model;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by noiz354 on 7/12/16.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public interface HadesApi {
    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json"
    })
    @GET("/v1/categories/{depId}")
    Observable<Response<HadesV1Model>> getCategory(@Path("depId") String taskId, @Query("filter") String filter);
}
