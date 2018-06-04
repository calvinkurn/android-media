package com.tokopedia.transactiondata.apiservice;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.transactiondata.constant.TransactionDataApiUrl;

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

    @GET(TransactionDataApiUrl.TransactionAction.PATH_GET_PARAMETER_DYNAMIC_PAYMENT)
    Observable<Response<TokopediaWsV4Response>> getParameterDynamicPayment(@QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TransactionDataApiUrl.TransactionAction.PATH_THANKS_DYNAMIC_PAYMENT)
    Observable<Response<TokopediaWsV4Response>> getThanksDynamicPayment(@FieldMap Map<String, String> params);
}
