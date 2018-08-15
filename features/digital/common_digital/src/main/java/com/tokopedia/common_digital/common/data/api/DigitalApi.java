package com.tokopedia.common_digital.common.data.api;

import com.google.gson.JsonObject;
import com.tokopedia.common_digital.common.constant.DigitalUrl;
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse;

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
 * Created by Rizky on 13/08/18.
 */
public interface DigitalApi {

    @GET(DigitalUrl.PATH_STATUS)
    Observable<Response<TkpdDigitalResponse>> getStatus();

    @GET(DigitalUrl.PATH_CATEGORY_LIST)
    Observable<Response<TkpdDigitalResponse>> getCategoryList();

    @GET(DigitalUrl.PATH_OPERATOR)
    Observable<Response<TkpdDigitalResponse>> getOperatorList();

    @GET(DigitalUrl.PATH_PRODUCT)
    Observable<Response<TkpdDigitalResponse>> getProductList();

    @GET(DigitalUrl.PATH_FAVORITE_LIST)
    Observable<Response<TkpdDigitalResponse>> getFavoriteList(@QueryMap Map<String, String> params);

    @GET(DigitalUrl.PATH_GET_CART)
    Observable<Response<TkpdDigitalResponse>> getCart(@QueryMap Map<String, String> params);

    @GET(DigitalUrl.PATH_CHECK_VOUCHER)
    Observable<Response<TkpdDigitalResponse>> checkVoucher(@QueryMap Map<String, String> params);

    @POST(DigitalUrl.PATH_GET_CART)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TkpdDigitalResponse>> addToCart(
            @Body JsonObject requestBody,
            @Header("Idempotency-Key") String idemPotencyKeyHeader
    );

    @PATCH(DigitalUrl.PATH_PATCH_OTP_SUCCESS)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TkpdDigitalResponse>> patchOtpCart(@Body JsonObject requestBody);

    @FormUrlEncoded
    @DELETE(DigitalUrl.PATH_GET_CART)
    Observable<Response<TkpdDigitalResponse>> deleteCart(@QueryMap Map<String, String> params);

    @POST(DigitalUrl.PATH_CHECKOUT)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TkpdDigitalResponse>> checkout(@Body JsonObject requestBody);

    @POST(DigitalUrl.PATH_USSD)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TkpdDigitalResponse>> parsePulsaMessage(@Body JsonObject requestBody);

    @POST(DigitalUrl.PATH_CANCEL_VOUCHER)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TkpdDigitalResponse>> cancelVoucher(@Body JsonObject requestBody);

    @POST(DigitalUrl.PATH_SMARTCARD_INQUIRY)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TkpdDigitalResponse>> smartcardInquiry(@Body JsonObject requestBody);

    @POST(DigitalUrl.PATH_SMARTCARD_COMMAND)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TkpdDigitalResponse>> smartcardCommand(@Body JsonObject requestBody);

}
