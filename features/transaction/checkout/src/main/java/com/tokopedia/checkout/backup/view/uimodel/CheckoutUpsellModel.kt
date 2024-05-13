package com.tokopedia.checkout.backup.view.uimodel

import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel

data class CheckoutUpsellModel(
    override val cartStringGroup: String = "",
    val upsell: ShipmentNewUpsellModel
) : CheckoutItem
