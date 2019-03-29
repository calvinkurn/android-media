package com.tokopedia.core.network.apiservices.transaction.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

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
public interface TXOrderActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_CANCEL_PAYMENT)
    Observable<Response<TkpdResponse>> cancelPayment(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_CONFIRM_PAYMENT)
    Observable<Response<TkpdResponse>> confirmPayment(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_DELIVERY_CONFIRM)
    Observable<Response<TkpdResponse>> deliveryConfirm(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_DELIVERY_FINISH_ORDER)
    Observable<Response<TkpdResponse>> deliveryFinishOrder(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_DELIVERY_REJECT)
    Observable<Response<TkpdResponse>> deliveryReject(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_EDIT_PAYMENT)
    Observable<Response<TkpdResponse>> editPaymnet(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_REORDER)
    Observable<Response<TkpdResponse>> reorder(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_UPLOAD_VALID_PROOF_BY_PAYMENT)
    Observable<Response<TkpdResponse>> uploadValidProofByPayment(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_REQUEST_CANCEL_ORDER)
    Observable<Response<TkpdResponse>> requestCancelOrder(@FieldMap TKPDMapParam<String, String> params);
}
