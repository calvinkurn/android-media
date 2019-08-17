package com.tokopedia.purchase_platform.features.cart.data.api

import com.tokopedia.purchase_platform.common.data.common.api.CartResponse
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

interface CartApi {

    @GET(CartCommonPurchaseApiUrl.PATH_SHOP_GROUP_LIST)
    fun getShopGroupList(@QueryMap params: Map<String, String>): Observable<Response<CartResponse>>

    @FormUrlEncoded
    @POST(CartCommonPurchaseApiUrl.PATH_REMOVE_FROM_CART)
    fun postDeleteCart(@FieldMap params: Map<String, String>): Observable<Response<CartResponse>>

    @FormUrlEncoded
    @POST(CartCommonPurchaseApiUrl.PATH_UPDATE_CART)
    fun postUpdateCart(@FieldMap params: Map<String, String>): Observable<Response<CartResponse>>

}