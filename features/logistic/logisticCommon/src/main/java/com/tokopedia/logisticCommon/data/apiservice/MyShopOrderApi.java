package com.tokopedia.logisticCommon.data.apiservice;


import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.network.constant.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface MyShopOrderApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_SHIPPING_FORM)
    Observable<Response<TokopediaWsV4Response>> getEditShippingForm(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_ORDER_LIST)
    Observable<Response<TokopediaWsV4Response>> getOrderList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_ORDER_NEW)
    Observable<Response<TokopediaWsV4Response>> getOrderNew(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_ORDER_PROCESS)
    Observable<Response<TokopediaWsV4Response>> getOrderProcess(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_ORDER_STATUS)
    Observable<Response<TokopediaWsV4Response>> getOrderStatus(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_PROCEED_SHIPPING_FORM)
    Observable<Response<TokopediaWsV4Response>> getProcessShippingForm(@FieldMap Map<String, String> params);
}
