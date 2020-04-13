package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference

data class ProfilesItemModel(
        var addressModel: AddressModel? = null,
        var shipmentModel: ShipmentModel? = null,
        var profileId: Int? = null,
        var paymentModel: PaymentModel? = null,
        var status: Int? = null
)
