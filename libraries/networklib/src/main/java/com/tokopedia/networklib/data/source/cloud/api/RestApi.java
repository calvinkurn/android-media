package com.tokopedia.networklib.data.source.cloud.api;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public interface RestApi {
    @GET()
    Observable<String> get(@Url String url, @QueryMap Map<String, String> queries, @HeaderMap Map<String, String> headers);

    @POST()
    Observable<String> post(@Url String url, @Body String json, @QueryMap Map<String, String> queries, @HeaderMap Map<String, String> headers);

    @PUT()
    Observable<String> put(@Url String url, @Body String json, @QueryMap Map<String, String> queries, @HeaderMap Map<String, String> headers);

    @DELETE()
    Observable<String> delete(@Url String url, @QueryMap Map<String, String> queries, @HeaderMap Map<String, String> headers);
}
