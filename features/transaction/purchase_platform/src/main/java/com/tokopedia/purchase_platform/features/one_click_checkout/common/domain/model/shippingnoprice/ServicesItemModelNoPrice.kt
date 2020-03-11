package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingnoprice

data class ServicesItemModelNoPrice (
    var serviceCode: String? = null,
    var serviceId: Int = -1,
    var servicesDuration: String? = null,
    var shipperIds: Int? = null,
    var spids: Int? = null,
    var isSelected: Boolean = false
)