package com.tokopedia.checkout.data.repository

import com.tokopedia.checkout.data.api.CheckoutApi
import com.tokopedia.checkout.data.model.response.SaveShipmentStateResponse
import rx.Observable
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

class CheckoutRepository @Inject constructor(val checkoutApi: CheckoutApi) : ICheckoutRepository {

    override fun saveShipmentState(params: Map<String, String>): Observable<SaveShipmentStateResponse> {
        return checkoutApi.postSaveShipmentState(params).map { cartResponseResponse ->
            cartResponseResponse.body()?.convertDataObj(SaveShipmentStateResponse::class.java)
        }
    }

}