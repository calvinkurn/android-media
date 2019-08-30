package com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.api

import com.tokopedia.purchase_platform.common.data.common.api.CartResponse
import com.tokopedia.purchase_platform.common.data.common.api.CommonPurchaseApi
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.api.MultipleAddressApiUrl.Companion.PATH_CART_LIST_MULTIPLE_ADDRESS
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rx.Observable

/**
 * Created by Irfan Khoirul on 2019-08-30.
 */

interface MultipleAddressApi: CommonPurchaseApi {

    @FormUrlEncoded
    @POST(PATH_CART_LIST_MULTIPLE_ADDRESS)
    fun getMultipleAddressCartList(@FieldMap params: Map<String, String>): Observable<Response<CartResponse>>

}