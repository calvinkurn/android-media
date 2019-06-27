package com.tokopedia.core.network.apiservices.transaction.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */

@Deprecated
public interface TXOrderApi {

    @GET(TkpdBaseURL.Transaction.PATH_GET_CANCEL_PAYMENT_FORM)
    Observable<Response<TkpdResponse>> getCancelPaymentForm(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Transaction.PATH_GET_CONFIRM_PAYMENT_FORM)
    Observable<Response<TkpdResponse>> getConfirmPaymentForm(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Transaction.PATH_GET_EDIT_PAYMENT_FORM)
    Observable<Response<TkpdResponse>> getEditPaymentForm(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Transaction.PATH_GET_REORDER_FORM)
    Observable<Response<TkpdResponse>> getReorderForm(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Transaction.PATH_GET_TX_ORDER_DELIVER)
    Observable<Response<TkpdResponse>> getTXOrderDeliver(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Transaction.PATH_GET_TX_ORDER_LIST)
    Observable<Response<TkpdResponse>> getTXOrderList(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Transaction.PATH_GET_TX_ORDER_PAYMENT_CONFIRMATION)
    Observable<Response<TkpdResponse>> getTXOrderPaymentConfirmation(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Transaction.PATH_GET_TX_ORDER_PAYMENT_CONFIRMED)
    Observable<Response<TkpdResponse>> getTXOrderPaymentConfirmed(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Transaction.PATH_GET_TX_ORDER_PAYMENT_CONFIRMED_DETAIL)
    Observable<Response<TkpdResponse>> getTXOrderPaymentConfirmedDetail(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Transaction.PATH_GET_TX_ORDER_STATUS)
    Observable<Response<TkpdResponse>> getTXOrderStatus(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Transaction.PATH_GET_UPLOAD_PROOF_BY_PAYMENT)
    Observable<Response<TkpdResponse>> getUploadProofByPaymentForm(@QueryMap Map<String, String> params);
}
