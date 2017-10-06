package com.tokopedia.core.base.common.service;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author ricoharisin .
 */

public interface CommonService {

    @GET
    Observable<String> get(@Url String Url, @QueryMap Map<String, String> params);

    @GET
    Observable<String> getParam(@Url String Url, @QueryMap String params);

    @FormUrlEncoded
    @POST
    Observable<String> post(@Url String Url, @FieldMap Map<String, String> params);

    @POST
    @Headers("Content-Type: application/json")
    Observable<String> postJson(@Url String Url, @Body String params);

    @POST
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Observable<String> postParam(@Url String Url, @Body String params);

    @POST
    Observable<String> post(@Url String Url);

    @DELETE
    Observable<String> delete(@Url String Url);

    @GET
    Observable<String> get(@Url String Url);

}
