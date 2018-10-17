package com.tokopedia.common_digital.common.data.api;

import com.google.gson.JsonObject;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common_digital.cart.data.entity.response.ResponseCartData;
import com.tokopedia.common_digital.cart.data.entity.response.ResponseCheckoutData;
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
public interface DigitalRestApi {

    @GET(DigitalUrl.PATH_CATEGORY_LIST)
    Observable<Response<TkpdDigitalResponse>> getCategoryList();

    @POST(DigitalUrl.PATH_GET_CART)
    @Headers({"Content-Type: application/json"})
    Observable<Response<DataResponse<ResponseCartData>>> addToCart(
            @Body JsonObject requestBody,
            @Header("Idempotency-Key") String idemPotencyKeyHeader
    );

    @POST(DigitalUrl.PATH_CHECKOUT)
    @Headers({"Content-Type: application/json"})
    Observable<Response<DataResponse<ResponseCheckoutData>>> checkout(@Body JsonObject requestBody);

    @POST(DigitalUrl.PATH_USSD)
    @Headers({"Content-Type: application/json"})
    Observable<Response<TkpdDigitalResponse>> parsePulsaMessage(@Body JsonObject requestBody);

}
