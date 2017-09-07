package com.tokopedia.core.network.apiservices.transaction.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by kris on 1/5/17. Tokopedia
 */

public interface TokoCashApi {

    @GET(TkpdBaseURL.TokoCash.PATH_WALLET)
    Observable<Response<TkpdResponse>> getTokoCash();

    @GET(TkpdBaseURL.TokoCash.PATH_CASH_BACK_DOMAIN)
    Observable<Response<TkpdDigitalResponse>> getTokoCashPending(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.TokoCash.PATH_REQUEST_OTP_WALLET)
    Observable<Response<TkpdResponse>> requestOtpWallet();

    @GET(TkpdBaseURL.TokoCash.PATH_LINK_WALLET_TO_TOKOCASH)
    Observable<Response<TkpdResponse>> linkedWalletToTokocash(@Query("otp") String otp);

    @GET(TkpdBaseURL.TokoCash.GET_HISTORY)
    Observable<Response<TkpdDigitalResponse>> getHistoryTokocash(
            @Query("type") String type,
            @Query("start_date") String startDate,
            @Query("end_date") String endDate,
            @Query("after_id") String afterId
    );
}
