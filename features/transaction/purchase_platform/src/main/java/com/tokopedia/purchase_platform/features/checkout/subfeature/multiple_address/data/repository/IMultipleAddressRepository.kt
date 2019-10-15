package com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.repository

import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.model.response.CartMultipleAddressDataListResponse
import rx.Observable

/**
 * Created by Irfan Khoirul on 2019-08-30.
 */

interface IMultipleAddressRepository {

    fun getCartMultipleAddressList(param: Map<String, String>): Observable<CartMultipleAddressDataListResponse>

}