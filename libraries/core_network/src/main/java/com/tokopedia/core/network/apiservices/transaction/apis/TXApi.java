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

@Deprecated
public interface TXApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_DO_PAYMENT)
    Observable<Response<TkpdResponse>> doPayment(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @GET(TkpdBaseURL.Transaction.GET_COUPON_LIST)
    Observable<Response<String>> getCouponList(@QueryMap Map<String, String> params);
}
