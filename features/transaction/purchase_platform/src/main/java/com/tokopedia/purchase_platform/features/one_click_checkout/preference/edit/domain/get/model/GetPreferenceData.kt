package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.get.model

import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.AddressModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.PaymentModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.ShipmentModel

data class GetPreferenceData(
        var addressModel: AddressModel? = null,
        var shipmentModel: ShipmentModel? = null,
        var profileId: Int? = null,
        var paymentModel: PaymentModel? = null,
        var status: Int? = null
)