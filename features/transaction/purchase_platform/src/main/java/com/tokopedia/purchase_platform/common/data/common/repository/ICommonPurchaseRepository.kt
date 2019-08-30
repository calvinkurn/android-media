package com.tokopedia.purchase_platform.common.data.common.repository

import com.tokopedia.purchase_platform.features.checkout.data.model.response.SaveShipmentStateResponse
import com.tokopedia.purchase_platform.features.checkout.data.model.response.ShippingAddressDataResponse
import com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout.CheckoutDataResponse
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.ShipmentAddressFormDataResponse
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.model.response.CartMultipleAddressDataListResponse
import rx.Observable

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

interface ICommonPurchaseRepository {

    fun checkout(param: Map<String, String>): Observable<CheckoutDataResponse>

    fun setShippingAddress(param: Map<String, String>): Observable<ShippingAddressDataResponse>


}