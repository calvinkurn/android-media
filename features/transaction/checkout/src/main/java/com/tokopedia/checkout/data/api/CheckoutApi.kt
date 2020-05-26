package com.tokopedia.checkout.data.api

import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rx.Observable

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

interface CheckoutApi {

    @FormUrlEncoded
    @POST(CheckoutApiUrl.PATH_SAVE_SHIPMENT)
    fun postSaveShipmentState(@FieldMap params: Map<String, String>): Observable<Response<CartResponse>>

}