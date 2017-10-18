package com.tokopedia.digital.tokocash.network.apiservice;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.digital.tokocash.network.request.RequestHelpHistory;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by nabillasabbaha on 10/11/17.
 */

public interface HistoryTokoCashApi {

    @GET(TkpdBaseURL.Wallet.GET_HISTORY)
    Observable<Response<TkpdDigitalResponse>> getHistoryTokocash(@QueryMap Map<String, String> params);

    @POST(TkpdBaseURL.Wallet.POST_COMPLAINT)
    Observable<Response<TkpdDigitalResponse>> postHelpHistory(@Body RequestHelpHistory requestHelpHistory);
}
