/*
 * Created By Kulomady on 11/25/16 11:54 PM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/25/16 11:54 PM
 */

package com.tokopedia.core.discovery.dynamicfilter.facade;

import com.tokopedia.core.discovery.dynamicfilter.facade.models.HadesV1Model;

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
@Deprecated
public interface HadesApi {
    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json"
    })
    @GET("/v1/categories/{depId}")
    Observable<Response<HadesV1Model>> getCategory(@Path("depId") String taskId, @Query("filter") String filter);
}
