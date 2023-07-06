package com.tokopedia.favorite.data.source.apis.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * @author Kulomady on 12/9/16.
 */
@JvmSuppressWildcards
interface TopAdsService {

    @GET("promo/v1.3/display/ads")
    suspend fun suspendGetShopTopAds(@QueryMap params: Map<String, Any?>?): Response<String?>?

}
