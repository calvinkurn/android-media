package com.tokopedia.favorite.data.source.apis.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Url
import rx.Observable

/**
 * @author Kulomady on 12/9/16.
 */
@JvmSuppressWildcards
interface TopAdsService {

    @GET("promo/v1.3/display/ads")
    fun getShopTopAds(@QueryMap params: Map<String, Any?>?): Observable<Response<String?>?>

    @GET
    fun productWishlistUrl(@Url wishlistUrl: String?): Observable<Response<String?>?>?

}
