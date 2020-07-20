package com.tokopedia.checkout.data.repository

import com.tokopedia.checkout.data.model.response.SaveShipmentStateResponse
import com.tokopedia.checkout.data.model.response.shipment_address_form.ShipmentAddressFormDataResponse
import rx.Observable

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

interface ICheckoutRepository {

    fun saveShipmentState(params: Map<String, String>): Observable<SaveShipmentStateResponse>

}