package com.tokopedia.core.network.apiservices.payment.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by kris on 9/12/17. Tokopedia
 */

public interface PaymentTransactionApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Payment.PATH_GET_CANCEL_TRANSACTION_DIALOG)
    Observable<Response<String>>
    showPaymentCancelDialog(@FieldMap TKPDMapParam<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Payment.PATH_CANCEL_TRANSACTION)
    Observable<Response<String>>
    cancelTransaction(@FieldMap TKPDMapParam<String, String> params);

}
