package com.tokopedia.digital.common.data.apiservice;

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
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author anggaprasetiyo on 2/23/17.
 */

public interface DigitalApi {

    @GET(TkpdBaseURL.DigitalApi.PATH_STATUS)
    Observable<Response<TkpdDigitalResponse>> getStatus();

    @GET(TkpdBaseURL.DigitalApi.PATH_CATEGORY + "/{categoryId}")
    Observable<Response<TkpdDigitalResponse>> getCategory(
            @Path("categoryId") String categoryId, @QueryMap Map<String, String> params
    );

    @GET(TkpdBaseURL.DigitalApi.PATH_CATEGORY_LIST)
    Observable<Response<TkpdDigitalResponse>> getCategoryList();

    @GET(TkpdBaseURL.DigitalApi.PATH_OPERATOR)
    Observable<Response<TkpdDigitalResponse>> getOperatorList();

    @GET(TkpdBaseURL.DigitalApi.PATH_PRODUCT)
    Observable<Response<TkpdDigitalResponse>> getProductList();

    @GET(TkpdBaseURL.DigitalApi.PATH_FAVORITE_LIST)
    Observable<Response<TkpdDigitalResponse>> getFavoriteList(@QueryMap Map<String, String> params);

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

    @PATCH(TkpdBaseURL.DigitalApi.PATH_PATCH_OTP_SUCCESS)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TkpdDigitalResponse>> patchOtpCart(@Body JsonObject requestBody);

    @FormUrlEncoded
    @DELETE(TkpdBaseURL.DigitalApi.PATH_GET_CART)
    Observable<Response<TkpdDigitalResponse>> deleteCart(@QueryMap Map<String, String> params);

    @POST(TkpdBaseURL.DigitalApi.PATH_CHECKOUT)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TkpdDigitalResponse>> checkout(@Body JsonObject requestBody);

    @POST(TkpdBaseURL.DigitalApi.PATH_USSD)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TkpdDigitalResponse>> parsePulsaMessage(@Body JsonObject requestBody);

}
