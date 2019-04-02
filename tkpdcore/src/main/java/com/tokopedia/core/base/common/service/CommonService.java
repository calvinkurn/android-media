package com.tokopedia.core.base.common.service;

import java.util.HashMap;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author ricoharisin .
 */

public interface CommonService {

    String CONTENT_TYPE_JSON = "Content-Type: application/json";
    String CONTENT_TYPE_FORM_URLENCODED = "Content-Type: application/x-www-form-urlencoded";

    @GET
    Observable<String> get(@Url String Url);

    @GET
    Observable<String> get(@Url String Url, @QueryMap Map<String, String> params);

    @GET
    Observable<String> getParam(@Url String Url, @QueryMap String params);

    @FormUrlEncoded
    @POST
    Observable<String> post(@Url String Url, @FieldMap Map<String, String> params);

    @POST
    @Headers(CONTENT_TYPE_JSON)
    Observable<String> postJson(@Url String Url, @Body String params);

    @POST
    @Headers(CONTENT_TYPE_FORM_URLENCODED)
    Observable<String> postParam(@Url String Url, @Body String params);

    @POST
    Observable<String> post(@Url String Url);

    @DELETE
    Observable<String> delete(@Url String Url);

    @DELETE
    Observable<String> delete(@Url String Url, @QueryMap Map<String, String> params);

    @PUT
    Observable<String> put(@Url String Url, @FieldMap Map<String, String> params);

    @HEAD
    Observable<String> head(@Url String url, @QueryMap HashMap<String, String> params);
}
