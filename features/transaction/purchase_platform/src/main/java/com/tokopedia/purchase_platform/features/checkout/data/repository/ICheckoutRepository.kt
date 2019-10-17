package com.tokopedia.purchase_platform.features.checkout.data.repository

import com.tokopedia.purchase_platform.features.checkout.data.model.response.SaveShipmentStateResponse
import com.tokopedia.purchase_platform.features.checkout.data.model.response.ShippingAddressDataResponse
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.ShipmentAddressFormDataResponse
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.model.response.CartMultipleAddressDataListResponse
import rx.Observable

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

interface ICheckoutRepository {

    fun getShipmentAddressForm(param: Map<String, String>): Observable<ShipmentAddressFormDataResponse>

    fun getShipmentAddressFormOneClickCheckout(param: Map<String, String>): Observable<ShipmentAddressFormDataResponse>

    fun saveShipmentState(params: Map<String, String>): Observable<SaveShipmentStateResponse>

}