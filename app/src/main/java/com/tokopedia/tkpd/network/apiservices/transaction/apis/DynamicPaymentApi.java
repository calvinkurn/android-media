package com.tokopedia.tkpd.network.apiservices.transaction.apis;

import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Default05 on 2/10/2016.
 */
public interface DynamicPaymentApi {

    @FormUrlEncoded
    @POST("/")
    Observable<Response<TkpdResponse>> openWebViewDynamicPayment(@FieldMap Map<String, String> params);
}
