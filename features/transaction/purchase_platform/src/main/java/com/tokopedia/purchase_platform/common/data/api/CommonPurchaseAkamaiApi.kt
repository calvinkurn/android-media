package com.tokopedia.purchase_platform.common.data.api

import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rx.Observable

interface CommonPurchaseAkamaiApi {

    @FormUrlEncoded
    @POST(CommonPurchaseApiUrl.PATH_CHECKOUT)
    fun checkout(@FieldMap params: Map<String, String>): Observable<Response<CartResponse>>
}