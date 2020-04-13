package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingnoprice

import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingprice.ServicesItem

data class ShippingListModel(
        var services : List<ServicesItem> = emptyList()
)