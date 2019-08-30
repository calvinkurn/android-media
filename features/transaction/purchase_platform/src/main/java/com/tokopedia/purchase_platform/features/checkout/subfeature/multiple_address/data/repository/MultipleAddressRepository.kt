package com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.repository

import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.api.MultipleAddressApi
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.model.response.CartMultipleAddressDataListResponse
import rx.Observable
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-08-30.
 */

class MultipleAddressRepository @Inject constructor(val multipleAddressApi: MultipleAddressApi): IMultipleAddressRepository {

    override fun getCartMultipleAddressList(param: Map<String, String>): Observable<CartMultipleAddressDataListResponse> {
        return multipleAddressApi.getMultipleAddressCartList(param).map { cartResponseResponse ->
            cartResponseResponse.body()?.convertDataObj(CartMultipleAddressDataListResponse::class.java)
        }
    }

}