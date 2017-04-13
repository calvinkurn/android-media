package com.tokopedia.core.base.common.service;

import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Kulomady on 12/9/16.
 */

public interface TopAdsService {

    @GET(TkpdBaseURL.TopAds.PATH_GET_PROMO_TOP_ADS)
    Observable<Response<String>> getTopAds(@QueryMap Map<String, Object> params);

    @GET(TkpdBaseURL.TopAds.PATH_GET_SHOP_TOP_ADS)
    Observable<Response<String>> getShopTopAds(@QueryMap Map<String, Object> params);

}


