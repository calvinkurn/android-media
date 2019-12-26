package com.tokopedia.purchase_platform.features.cart.data.repository

import com.tokopedia.purchase_platform.features.cart.data.model.response.updatecart.UpdateCartDataResponse
import rx.Observable

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

interface ICartRepository {

    fun updateCartData(param: Map<String, String>): Observable<UpdateCartDataResponse>

}