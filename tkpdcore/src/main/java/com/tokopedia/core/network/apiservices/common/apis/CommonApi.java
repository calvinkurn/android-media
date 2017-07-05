package com.tokopedia.core.network.apiservices.common.apis;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author ricoharisin .
 */

public interface CommonApi {

    @GET
    Observable<String> get(@Url String Url, @QueryMap Map<String, String> params);

    @POST
    Observable<String> post(@Url String Url, @FieldMap Map<String, String> params);

}
