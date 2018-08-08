package com.tokopedia.core.network.apiservices.transaction.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public interface TXPaymentSprintAsiaApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_TX_PAYMENT_SPRINTASIA)
    Observable<Response<String>> paymentSprintAsia(@FieldMap Map<String, String> params);
}
