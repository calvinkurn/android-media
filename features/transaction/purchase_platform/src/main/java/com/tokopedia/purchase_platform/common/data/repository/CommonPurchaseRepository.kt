package com.tokopedia.purchase_platform.common.data.repository

import com.tokopedia.purchase_platform.common.data.api.CommonPurchaseAkamaiApi
import com.tokopedia.purchase_platform.common.data.api.CommonPurchaseApi
import com.tokopedia.purchase_platform.features.checkout.data.model.response.ShippingAddressDataResponse
import com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout.CheckoutResponse
import rx.Observable
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

class CommonPurchaseRepository @Inject constructor(val commonPurchaseApi: CommonPurchaseApi, val commonPurchaseAkamaiApi: CommonPurchaseAkamaiApi) : ICommonPurchaseRepository {

    override fun checkout(param: Map<String, String>): Observable<CheckoutResponse> {
        return commonPurchaseAkamaiApi.checkout(param).map { cartResponseResponse ->
            cartResponseResponse.body()?.convertResponseObj(CheckoutResponse::class.java)
        }
    }

    override fun setShippingAddress(param: Map<String, String>): Observable<ShippingAddressDataResponse> {
        return commonPurchaseApi.postSetShippingAddress(param).map { cartResponseResponse ->
            cartResponseResponse.body()?.convertDataObj(ShippingAddressDataResponse::class.java)
        }
    }

}