package com.tokopedia.checkout.data.repository

import com.tokopedia.checkout.data.api.CheckoutApi
import com.tokopedia.checkout.data.model.response.SaveShipmentStateResponse
import com.tokopedia.checkout.data.model.response.shipment_address_form.ShipmentAddressFormDataResponse
import rx.Observable
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

class CheckoutRepository @Inject constructor(val checkoutApi: CheckoutApi) : ICheckoutRepository {

    override fun getShipmentAddressForm(param: Map<String, String>): Observable<ShipmentAddressFormDataResponse> {
        return checkoutApi.getShipmentAddressForm(param).map { cartResponseResponse ->
            cartResponseResponse.body()?.convertDataObj(ShipmentAddressFormDataResponse::class.java)
        }
    }

    override fun getShipmentAddressFormOneClickCheckout(param: Map<String, String>): Observable<ShipmentAddressFormDataResponse> {
        return checkoutApi.getShipmentAddressFormOneClickCheckout(param).map { cartResponseResponse ->
            cartResponseResponse.body()?.convertDataObj(ShipmentAddressFormDataResponse::class.java)
        }
    }

    override fun saveShipmentState(params: Map<String, String>): Observable<SaveShipmentStateResponse> {
        return checkoutApi.postSaveShipmentState(params).map { cartResponseResponse ->
            cartResponseResponse.body()?.convertDataObj(SaveShipmentStateResponse::class.java)
        }
    }

}