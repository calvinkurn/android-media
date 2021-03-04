package com.tokopedia.digital.common.data.apiservice;

import com.google.gson.JsonObject;
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse;
import com.tokopedia.digital.newcart.data.entity.response.cart.ResponseCancelVoucherData;
import com.tokopedia.digital.newcart.data.entity.response.cart.ResponseCartData;
import com.tokopedia.digital.newcart.data.entity.response.cart.ResponseCheckoutData;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.data.model.response.DataResponse;

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

    @POST(TkpdBaseURL.DigitalApi.PATH_CANCEL_VOUCHER)
    @Headers({"Content-Type: application/json"})
    Observable<Response<DataResponse<ResponseCancelVoucherData>>> cancelVoucher(@Body JsonObject requestBody);
}
