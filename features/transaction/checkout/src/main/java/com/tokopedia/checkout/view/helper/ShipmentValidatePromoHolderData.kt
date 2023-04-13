package com.tokopedia.checkout.view.helper

import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest

data class ShipmentValidatePromoHolderData(
    val validateUsePromoRequest: ValidateUsePromoRequest,
    val cartPosition: Int,
    val cartString: String,
    val promoCode: String,
    val newCourierItemData: CourierItemData?
)
