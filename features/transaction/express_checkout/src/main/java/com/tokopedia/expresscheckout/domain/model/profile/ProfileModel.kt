package com.tokopedia.expresscheckout.domain.model.profile

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class ProfileModel(
        var id: Int = 0,
        var status: Int = 0,
        var addressModel: AddressModel? = null,
        var paymentModel: PaymentModel? = null,
        var shipmentModel: ShipmentModel? = null
)