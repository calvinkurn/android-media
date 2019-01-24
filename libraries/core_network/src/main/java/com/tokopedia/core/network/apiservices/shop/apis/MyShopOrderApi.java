package com.tokopedia.core.network.apiservices.shop.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public interface MyShopOrderApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_SHIPPING_FORM)
    Observable<Response<TkpdResponse>> getEditShippingForm(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_ORDER_LIST)
    Observable<Response<TkpdResponse>> getOrderList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_ORDER_NEW)
    Observable<Response<TkpdResponse>> getOrderNew(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_ORDER_PROCESS)
    Observable<Response<TkpdResponse>> getOrderProcess(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_ORDER_STATUS)
    Observable<Response<TkpdResponse>> getOrderStatus(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_PROCEED_SHIPPING_FORM)
    Observable<Response<TkpdResponse>> getProcessShippingForm(@FieldMap Map<String, String> params);
}
