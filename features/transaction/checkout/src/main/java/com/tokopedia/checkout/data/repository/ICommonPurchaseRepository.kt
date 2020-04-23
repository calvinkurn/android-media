package com.tokopedia.checkout.data.repository

import com.tokopedia.checkout.data.model.response.ShippingAddressDataResponse
import com.tokopedia.checkout.data.model.response.checkout.CheckoutResponse
import rx.Observable

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

interface ICommonPurchaseRepository {

    fun checkout(param: Map<String, String>): Observable<CheckoutResponse>

    fun setShippingAddress(param: Map<String, String>): Observable<ShippingAddressDataResponse>


}