package com.tokopedia.core.network.apiservices.transaction.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by kris on 1/5/17. Tokopedia
 */

public interface TokoCashApi {

    @GET(TkpdBaseURL.TopCash.PATH_WALLET)
    Observable<Response<TkpdResponse>> getTokoCash();

    @GET(TkpdBaseURL.TopCash.PATH_CASH_BACK_DOMAIN)
    Observable<Response<TkpdDigitalResponse>> getTokoCashPending(@QueryMap Map<String, String> params);
}
