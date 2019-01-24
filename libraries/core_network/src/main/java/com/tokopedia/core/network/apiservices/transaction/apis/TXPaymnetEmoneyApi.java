package com.tokopedia.core.network.apiservices.transaction.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public interface TXPaymnetEmoneyApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_TX_PAYMENT_EMONEY)
    Observable<Response<TkpdResponse>> paymentEMoney(@FieldMap Map<String, String> params);
}
