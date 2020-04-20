package com.tokopedia.favorite.data.source.apis.service;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author Kulomady on 12/9/16.
 */

public interface TopAdsService {

    @GET("promo/v1.3/display/ads")
    Observable<Response<String>> getShopTopAds(@QueryMap Map<String, Object> params);

    @GET
    Observable<Response<String>> productWishlistUrl(@Url String wishlistUrl);

}


