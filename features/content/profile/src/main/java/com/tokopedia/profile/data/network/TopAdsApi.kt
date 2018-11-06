package com.tokopedia.profile.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap
import rx.Observable

/**
 * @author by milhamj on 10/17/18.
 */
interface TopAdsApi {
    @GET(AFFILIATE_TRACKER)
    fun track(@QueryMap params: HashMap<String, Any>): Observable<Response<String>>
}
