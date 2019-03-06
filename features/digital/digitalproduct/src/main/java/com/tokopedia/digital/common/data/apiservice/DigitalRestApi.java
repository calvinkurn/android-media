package com.tokopedia.digital.common.data.apiservice;

import com.google.gson.JsonObject;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common_digital.cart.data.entity.response.ResponseCartData;
import com.tokopedia.common_digital.cart.data.entity.response.ResponseCheckoutData;
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse;
import com.tokopedia.network.constant.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author anggaprasetiyo on 2/23/17.
 */
public interface DigitalRestApi {

    @GET(TkpdBaseURL.DigitalApi.PATH_STATUS)
    Observable<Response<TkpdDigitalResponse>> getStatus();

    @GET(TkpdBaseURL.DigitalApi.PATH_CATEGORY_LIST)
    Observable<Response<TkpdDigitalResponse>> getCategoryList(@QueryMap Map<String, Object> params);

    @GET(TkpdBaseURL.DigitalApi.PATH_GET_CART)
    Observable<Response<DataResponse<ResponseCartData>>> getCart(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.DigitalApi.PATH_CHECK_VOUCHER)
    Observable<Response<TkpdDigitalResponse>> checkVoucher(@QueryMap Map<String, String> params);

    @PATCH(TkpdBaseURL.DigitalApi.PATH_PATCH_OTP_SUCCESS)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TkpdDigitalResponse>> patchOtpCart(@Body JsonObject requestBody);

    @POST(TkpdBaseURL.DigitalApi.PATH_CHECKOUT)
    @Headers({"Content-Type: application/json"})
    Observable<Response<DataResponse<ResponseCheckoutData>>> checkout(@Body JsonObject requestBody);

    @POST(TkpdBaseURL.DigitalApi.PATH_USSD)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TkpdDigitalResponse>> parsePulsaMessage(@Body JsonObject requestBody);

    @POST(TkpdBaseURL.DigitalApi.PATH_CANCEL_VOUCHER)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TkpdDigitalResponse>> cancelVoucher(@Body JsonObject requestBody);

    @POST(TkpdBaseURL.DigitalApi.PATH_SMARTCARD_INQUIRY)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TkpdDigitalResponse>> smartcardInquiry(@Body JsonObject requestBody);

    @POST(TkpdBaseURL.DigitalApi.PATH_SMARTCARD_COMMAND)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TkpdDigitalResponse>> smartcardCommand(@Body JsonObject requestBody);

}
