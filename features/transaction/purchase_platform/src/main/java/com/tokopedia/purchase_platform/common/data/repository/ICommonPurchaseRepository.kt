package com.tokopedia.purchase_platform.common.data.repository

import com.tokopedia.purchase_platform.features.checkout.data.model.response.ShippingAddressDataResponse
import com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout.CheckoutDataResponse
import rx.Observable

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

interface ICommonPurchaseRepository {

    fun checkout(param: Map<String, String>): Observable<CheckoutDataResponse>

    fun setShippingAddress(param: Map<String, String>): Observable<ShippingAddressDataResponse>


}