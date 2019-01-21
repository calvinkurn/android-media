package com.tokopedia.core.network.apiservices.product.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author by errysuprayogi on 8/23/17.
 */

public interface PromoTopAdsApi {

    @GET(TkpdBaseURL.TopAds.URL_CHECK_PROMO)
    Observable<Response<String>> checkPromoAds(@QueryMap Map<String, String> params);

}
