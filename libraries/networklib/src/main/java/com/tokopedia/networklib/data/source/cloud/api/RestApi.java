package com.tokopedia.networklib.data.source.cloud.api;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public interface RestApi {
    @GET()
    Observable<String> get(@Url String url, @QueryMap(encoded = true) Map<String, Object> queries, @HeaderMap Map<String, Object> headers);

    @POST()
    Observable<String> post(@Url String url, @Body String json, @QueryMap(encoded = true) Map<String, Object> queries, @HeaderMap Map<String, Object> headers);

    @Multipart
    @POST()
    Observable<Response<String>> postMultipart(@Url String url, @Part MultipartBody.Part file, @QueryMap(encoded = true) Map<String, Object> queries, @HeaderMap Map<String, Object> headers);

    @FormUrlEncoded
    @POST()
    Observable<String> post(@Url String url, @FieldMap(encoded = true) Map<String, Object> params, @QueryMap(encoded = true) Map<String, Object> queries, @HeaderMap Map<String, Object> headers);

    @PUT()
    Observable<String> put(@Url String url, @Body String json, @QueryMap(encoded = true) Map<String, Object> queries, @HeaderMap Map<String, Object> headers);

    @FormUrlEncoded
    @PUT()
    Observable<String> put(@Url String url, @FieldMap(encoded = true) Map<String, Object> params, @QueryMap(encoded = true) Map<String, Object> queries, @HeaderMap Map<String, Object> headers);

    @DELETE()
    Observable<String> delete(@Url String url, @QueryMap(encoded = true) Map<String, Object> queries, @HeaderMap Map<String, Object> headers);
}
