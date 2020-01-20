package com.tokopedia.purchase_platform.features.checkout.data.api

import com.tokopedia.purchase_platform.common.data.api.CartResponse
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

interface CheckoutApi {

    @GET(CheckoutApiUrl.PATH_SHIPMENT_ADDRESS_FORM_DIRECT)
    fun getShipmentAddressForm(@QueryMap params: Map<String, String>): Observable<Response<CartResponse>>

    @GET(CheckoutApiUrl.PATH_SHIPMENT_ADDRESS_ONE_CLICK_CHECKOUT)
    fun getShipmentAddressFormOneClickCheckout(@QueryMap params: Map<String, String>): Observable<Response<CartResponse>>

    @FormUrlEncoded
    @POST(CheckoutApiUrl.PATH_SAVE_SHIPMENT)
    fun postSaveShipmentState(@FieldMap params: Map<String, String>): Observable<Response<CartResponse>>

}