package com.tokopedia.core.network.apiservices.transaction.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
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
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public interface TXActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_STEP_1_PROCESS_CREDIT_CARD)
    Observable<Response<TkpdResponse>> step1ProcessCreditCard(@FieldMap Map<String, String> params);

    @GET(TkpdBaseURL.Transaction.PATH_GET_PARAMETER_DYNAMIC_PAYMENT)
    Observable<Response<TkpdResponse>> getParameterDynamicPayment(@QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_THANKS_DYNAMIC_PAYMENT)
    Observable<Response<TkpdResponse>> getThanksDynamicPayment(@FieldMap Map<String, String> params);
}
