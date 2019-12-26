package com.tokopedia.purchase_platform.features.cart.data.api

import com.tokopedia.purchase_platform.common.data.api.CartResponse
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rx.Observable

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

interface CartApi {

    @FormUrlEncoded
    @POST(CartApiUrl.PATH_UPDATE_CART)
    fun postUpdateCart(@FieldMap params: Map<String, String>): Observable<Response<CartResponse>>

}