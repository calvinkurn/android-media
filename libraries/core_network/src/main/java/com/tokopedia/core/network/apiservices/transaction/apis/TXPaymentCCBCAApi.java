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

@Deprecated
public interface TXPaymentCCBCAApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_TX_PAYMENT_CC_BCA)
    Observable<Response<TkpdResponse>> paymentCCBCA(@FieldMap Map<String, String> params);
}
