package com.tokopedia.checkout.view.helper

import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrder
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest

data class ShipmentValidatePromoHolderData(
    val validateUsePromoRequest: ValidateUsePromoRequest? = null,
    val cartPosition: Int = 0,
    val cartString: String = "",
    val promoCode: String = "",
    val newCourierItemData: CourierItemData? = null,
    val clearPromoOrder: ClearPromoOrder? = null
)
