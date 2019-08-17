package com.tokopedia.purchase_platform.features.checkout.data.repository

import com.tokopedia.purchase_platform.features.checkout.data.api.CheckoutApi
import com.tokopedia.purchase_platform.features.checkout.data.model.response.SaveShipmentStateResponse
import com.tokopedia.purchase_platform.features.checkout.data.model.response.ShippingAddressDataResponse
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.ShipmentAddressFormDataResponse
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.model.response.CartMultipleAddressDataListResponse
import rx.Observable
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

class CheckoutRepository @Inject constructor(val checkoutApi: CheckoutApi) : ICheckoutRepository {

    override fun getCartMultipleAddressList(param: Map<String, String>): Observable<CartMultipleAddressDataListResponse> {
        return checkoutApi.getCartList(param).map { cartResponseResponse ->
            cartResponseResponse.body()?.convertDataObj(CartMultipleAddressDataListResponse::class.java)
        }
    }

    override fun setShippingAddress(param: Map<String, String>): Observable<ShippingAddressDataResponse> {
        return checkoutApi.postSetShippingAddress(param).map { cartResponseResponse ->
            cartResponseResponse.body()?.convertDataObj(ShippingAddressDataResponse::class.java)
        }
    }

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