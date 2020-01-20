package com.tokopedia.purchase_platform.features.cart.data.repository

import com.tokopedia.purchase_platform.features.cart.data.model.response.DeleteCartDataResponse
import com.tokopedia.purchase_platform.features.cart.data.model.response.UpdateCartDataResponse
import rx.Observable

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

interface ICartRepository {

    fun deleteCartData(param: Map<String, String>): Observable<DeleteCartDataResponse>

    fun updateCartData(param: Map<String, String>): Observable<UpdateCartDataResponse>

}