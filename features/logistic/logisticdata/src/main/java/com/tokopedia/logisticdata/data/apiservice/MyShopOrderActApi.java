package com.tokopedia.logisticdata.data.apiservice;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.network.constant.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by kris on 12/28/17. Tokopedia
 */

public interface MyShopOrderActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_EDIT_SHIPPING_REF)
    Observable<Response<TokopediaWsV4Response>> editShippingRef(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_PROCEED_ORDER)
    Observable<Response<TokopediaWsV4Response>> proceedOrder(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_PROCEED_SHIPPING)
    Observable<Response<TokopediaWsV4Response>> proceedShipping(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_RETRY_PICKUP)
    Observable<Response<TokopediaWsV4Response>> retryPickUp(@FieldMap Map<String, String> params);
}
