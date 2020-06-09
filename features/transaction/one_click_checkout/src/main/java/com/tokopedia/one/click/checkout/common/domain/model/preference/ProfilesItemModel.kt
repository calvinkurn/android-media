package com.tokopedia.one.click.checkout.common.domain.model.preference

data class ProfilesItemModel(
        var addressModel: AddressModel? = null,
        var shipmentModel: ShipmentModel? = null,
        var profileId: Int? = null,
        var paymentModel: PaymentModel? = null,
        var status: Int? = null
)
