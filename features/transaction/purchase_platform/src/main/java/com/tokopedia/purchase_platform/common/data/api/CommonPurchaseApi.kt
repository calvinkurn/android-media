package com.tokopedia.purchase_platform.common.data.api

import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rx.Observable

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

interface CommonPurchaseApi {

    @FormUrlEncoded
    @POST(CommonPurchaseApiUrl.PATH_CHECKOUT)
    fun checkout(@FieldMap params: Map<String, String>): Observable<Response<CartResponse>>

    @FormUrlEncoded
    @POST(CommonPurchaseApiUrl.PATH_SHIPPING_ADDRESS)
    fun postSetShippingAddress(@FieldMap params: Map<String, String>): Observable<Response<CartResponse>>

}