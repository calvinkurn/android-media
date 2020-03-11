package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingnoprice

import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingprice.ServicesItemModel

data class ShippingListModel(
        var services : List<ServicesItemModelNoPrice> = emptyList(),
        var servicesPrice : List<ServicesItemModel> = emptyList()
)