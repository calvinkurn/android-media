package com.tokopedia.core.network.apiservices.digital;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author anggaprasetiyo on 2/23/17.
 */

public interface DigitalApi {

    @GET(TkpdBaseURL.DigitalApi.PATH_STATUS)
    Observable<Response<TkpdDigitalResponse>> getStatus(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.DigitalApi.PATH_CATEGORY)
    Observable<Response<TkpdDigitalResponse>> getCategoryList(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.DigitalApi.PATH_OPERATOR)
    Observable<Response<TkpdDigitalResponse>> getOperatorList(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.DigitalApi.PATH_PRODUCT)
    Observable<Response<TkpdDigitalResponse>> getProductList(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.DigitalApi.PATH_BANNER)
    Observable<Response<TkpdDigitalResponse>> getBanner(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.DigitalApi.PATH_RECENT_NUMBER)
    Observable<Response<TkpdDigitalResponse>> getRecentNumber(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.DigitalApi.PATH_LAST_ORDER)
    Observable<Response<TkpdDigitalResponse>> getLastOrder(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.DigitalApi.PATH_ORDER)
    Observable<Response<TkpdDigitalResponse>> getOrder(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.DigitalApi.PATH_GET_CART)
    Observable<Response<TkpdDigitalResponse>> getCart(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.DigitalApi.PATH_CHECK_VOUCHER)
    Observable<Response<TkpdDigitalResponse>> checkVoucher(@QueryMap Map<String, String> params);

    @POST(TkpdBaseURL.DigitalApi.PATH_GET_CART)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TkpdDigitalResponse>> addToCart(
            @Body JsonObject requestBody,
            @Header("Idempotency-Key") String idemPotencyKeyHeader
    );

    @PATCH(TkpdBaseURL.DigitalApi.PATH_GET_CART)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TkpdDigitalResponse>> patchOtpCart(@Body JsonObject requestBody);

    @FormUrlEncoded
    @DELETE(TkpdBaseURL.DigitalApi.PATH_GET_CART)
    Observable<Response<TkpdDigitalResponse>> deleteCart(@QueryMap Map<String, String> params);

    @POST(TkpdBaseURL.DigitalApi.PATH_CHECKOUT)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TkpdDigitalResponse>> checkout(@Body JsonObject requestBody);

}
