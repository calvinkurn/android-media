package com.tokopedia.oneclickcheckout.common.view.model.preference

data class ProfilesItemModel(
        var addressModel: AddressModel = AddressModel(),
        var shipmentModel: ShipmentModel = ShipmentModel(),
        var profileId: Int = 0,
        var paymentModel: PaymentModel = PaymentModel(),
        var status: Int = 0
)
