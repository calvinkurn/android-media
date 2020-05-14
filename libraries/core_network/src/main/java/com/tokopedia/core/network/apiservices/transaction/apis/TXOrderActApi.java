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
    @POST(TkpdBaseURL.Transaction.PATH_DELIVERY_CONFIRM)
    Observable<Response<TkpdResponse>> deliveryConfirm(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_DELIVERY_FINISH_ORDER)
    Observable<Response<TkpdResponse>> deliveryFinishOrder(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_REORDER)
    Observable<Response<TkpdResponse>> reorder(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_REQUEST_CANCEL_ORDER)
    Observable<Response<TkpdResponse>> requestCancelOrder(@FieldMap TKPDMapParam<String, String> params);
}
