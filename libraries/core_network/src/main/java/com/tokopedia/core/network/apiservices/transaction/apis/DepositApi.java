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
public interface DepositApi {

    @GET(TkpdBaseURL.Transaction.PATH_GET_DEPOSIT)
    Observable<Response<TkpdResponse>> getDeposit(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Transaction.PATH_GET_SUMMARY)
    Observable<Response<TkpdResponse>> getSummary(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Transaction.PATH_GET_WITHDRAW_FORM)
    Observable<Response<TkpdResponse>> getWithDrawForm(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Transaction.PATH_GET_DEPOSIT)
    Observable<Response<TkpdResponse>> getDeposit2(@QueryMap Map<String, Object> params);
}
