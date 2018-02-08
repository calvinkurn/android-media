package com.tokopedia.posapp.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by okasurya on 9/5/17.
 */

public interface ScroogeApi {
    @GET(TkpdBaseURL.Payment.PATH_PAYMENT_STATUS)
    Observable<Response<String>> getPaymentStatus(@QueryMap Map<String, String> param);
}
