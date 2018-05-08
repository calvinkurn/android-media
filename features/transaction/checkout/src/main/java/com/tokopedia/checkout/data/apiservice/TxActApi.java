package com.tokopedia.checkout.data.apiservice;

import com.tokopedia.checkout.data.ConstantApiUrl;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author anggaprasetiyo on 08/05/18.
 */
public interface TxActApi {

    @GET(ConstantApiUrl.TransactionAction.PATH_GET_PARAMETER_DYNAMIC_PAYMENT)
    Observable<Response<TkpdResponse>> getParameterDynamicPayment(@QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(ConstantApiUrl.TransactionAction.PATH_THANKS_DYNAMIC_PAYMENT)
    Observable<Response<TkpdResponse>> getThanksDynamicPayment(@FieldMap Map<String, String> params);
}
