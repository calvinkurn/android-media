package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingnoprice

data class ServicesItemModelNoPrice (
    var serviceCode: String? = null,
    var serviceId: Int? = null,
    var servicesDuration: String? = null,
    var shipperIds: List<ShipperIds?>? = null,
    var spids: List<Spids?>? = null
)