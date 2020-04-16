package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shipping

class ShippingDataModel1 (
//    val promoStacking: PromoStacking? = null,
    var id: String? = null,
    var services: ArrayList<ServicesItemModel?>? = null,
    var type: String? = null,
    var ratesId: Long? = null
)