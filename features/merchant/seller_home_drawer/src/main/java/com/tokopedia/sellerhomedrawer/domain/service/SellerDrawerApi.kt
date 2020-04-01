package com.tokopedia.sellerhomedrawer.domain.service

import com.tokopedia.network.data.model.response.GraphqlResponse
import com.tokopedia.sellerhomedrawer.data.SellerUserData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import rx.Observable

interface SellerDrawerApi {

    @POST("./")
    @Headers("Content-Type: application/json")
    fun getSellerDrawerData(@Body body: Map<String, Any>): Observable<Response<GraphqlResponse<SellerUserData>>>
}