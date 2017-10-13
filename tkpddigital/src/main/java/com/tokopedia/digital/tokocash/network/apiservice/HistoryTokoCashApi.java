package com.tokopedia.digital.tokocash.network.apiservice;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by nabillasabbaha on 10/11/17.
 */

public interface HistoryTokoCashApi {

    @GET(TkpdBaseURL.Wallet.GET_HISTORY)
    Observable<Response<TkpdDigitalResponse>> getHistoryTokocash(
            @Query("type") String type,
            @Query("start_date") String startDate,
            @Query("end_date") String endDate,
            @Query("after_id") String afterId
    );
}
