package com.tokopedia.posapp.payment.otp.data.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.common.PosUrl;
import com.tokopedia.posapp.payment.otp.data.pojo.transaction.CheckTransactionResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by okasurya on 10/17/17.
 */

public interface PaymentApi {
    String CONTENT_TYPE_APPLICATION_JSON = "Content-Type: application/json";

    @POST(PosUrl.Payment.CREATE_ORDER)
    @Headers(CONTENT_TYPE_APPLICATION_JSON)
    Observable<Response<TkpdResponse>> createOrder(@Body String json);

    @GET(PosUrl.Payment.GET_PAYMENT_STATUS)
    Observable<Response<TkpdResponse>> getPaymentStatus(@QueryMap Map<String, String> param);

    @POST(PosUrl.Payment.CHECK_TRANSACTION_STATUS)
    Observable<Response<DataResponse<CheckTransactionResponse>>> checkTransaction(@Body String json);
}
