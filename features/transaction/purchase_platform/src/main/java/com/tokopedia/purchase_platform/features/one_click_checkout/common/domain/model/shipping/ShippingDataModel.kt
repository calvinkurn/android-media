package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shipping

import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.shipping.ServicesItem

class ShippingDataModel (
//    val promoStacking: PromoStacking? = null,
    val id: String? = null,
    val services: List<ServicesItem?>? = null,
    val type: String? = null,
    val ratesId: Long? = null
)