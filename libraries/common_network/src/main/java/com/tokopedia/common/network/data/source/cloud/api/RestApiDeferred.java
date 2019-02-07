package com.tokopedia.common.network.data.source.cloud.api;

import java.util.Map;

import kotlinx.coroutines.experimental.Deferred;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public interface RestApiDeferred {
    @GET()
    Deferred<Response<String>> getDeferred(@Url String url, @QueryMap(encoded = true) Map<String, Object> queries, @HeaderMap Map<String, Object> headers);

    @POST()
    Deferred<Response<String>> postDeferred(@Url String url, @Body String json, @QueryMap(encoded = true) Map<String, Object> queries, @HeaderMap Map<String, Object> headers);

    @Multipart
    @POST()
    Deferred<Response<String>> postMultipartDeferred(@Url String url, @Part MultipartBody.Part file, @QueryMap(encoded = true) Map<String, Object> queries, @HeaderMap Map<String, Object> headers);

    @Multipart
    @POST()
    Deferred<Response<String>> postMultipartDeferred(@Url String url, @PartMap Map<String, RequestBody> partMap, @QueryMap(encoded = true) Map<String, Object> queries, @HeaderMap Map<String, Object> headers);

    @FormUrlEncoded
    @POST()
    Deferred<Response<String>> postDeferred(@Url String url, @FieldMap(encoded = true) Map<String, Object> params, @QueryMap(encoded = true) Map<String, Object> queries, @HeaderMap Map<String, Object> headers);

    @PUT()
    Deferred<Response<String>> putDeferred(@Url String url, @Body String json, @QueryMap(encoded = true) Map<String, Object> queries, @HeaderMap Map<String, Object> headers);

    @FormUrlEncoded
    @PUT()
    Deferred<Response<String>> putDeferred(@Url String url, @FieldMap(encoded = true) Map<String, Object> params, @QueryMap(encoded = true) Map<String, Object> queries, @HeaderMap Map<String, Object> headers);

    @DELETE()
    Deferred<Response<String>> deleteDeferred(@Url String url, @QueryMap(encoded = true) Map<String, Object> queries, @HeaderMap Map<String, Object> headers);

    @Multipart
    @PUT()
    Deferred<Response<String>> putMultipartDeferred(@Url String url, @Part MultipartBody.Part file, @QueryMap(encoded = true) Map<String, Object> queries, @HeaderMap Map<String, Object> headers);

    @Multipart
    @PUT()
    Deferred<Response<String>> putMultipartDeferred(@Url String url, @PartMap Map<String, RequestBody> partMap, @QueryMap(encoded = true) Map<String, Object> queries, @HeaderMap Map<String, Object> headers);


    @PUT()
    Deferred<Response<String>> putRequestBodyDeferred(@Url String url, @Body RequestBody requestBody, @HeaderMap Map<String, Object> headers);

}
