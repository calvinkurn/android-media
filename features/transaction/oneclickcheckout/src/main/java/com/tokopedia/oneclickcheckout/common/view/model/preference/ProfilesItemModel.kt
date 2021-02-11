package com.tokopedia.oneclickcheckout.common.view.model.preference

data class ProfilesItemModel(
        var addressModel: AddressModel = AddressModel(),
        var shipmentModel: ShipmentModel = ShipmentModel(),
        var profileId: Int = 0,
        var paymentModel: PaymentModel = PaymentModel(),
        var status: Int = 0,
        var enable: Boolean = false
) {
    companion object {
        internal const val MAIN_PROFILE_STATUS = 2
    }
}
