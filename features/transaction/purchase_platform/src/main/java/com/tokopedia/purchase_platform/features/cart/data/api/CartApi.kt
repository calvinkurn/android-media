package com.tokopedia.purchase_platform.features.cart.data.api

import com.tokopedia.purchase_platform.common.data.api.CartResponse
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

interface CartApi {

    @FormUrlEncoded
    @POST(CartApiUrl.PATH_REMOVE_FROM_CART)
    fun postDeleteCart(@FieldMap params: Map<String, String>): Observable<Response<CartResponse>>

    @FormUrlEncoded
    @POST(CartApiUrl.PATH_UPDATE_CART)
    fun postUpdateCart(@FieldMap params: Map<String, String>): Observable<Response<CartResponse>>

}