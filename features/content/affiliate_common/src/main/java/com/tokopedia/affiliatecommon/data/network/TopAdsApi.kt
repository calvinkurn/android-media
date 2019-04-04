package com.tokopedia.affiliatecommon.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Url
import rx.Observable

/**
 * @author by milhamj on 10/17/18.
 */
interface TopAdsApi {

    @GET()
    fun trackWithUrl(@Url url:String, @QueryMap params: HashMap<String, Any>): Observable<Response<String>>
}
