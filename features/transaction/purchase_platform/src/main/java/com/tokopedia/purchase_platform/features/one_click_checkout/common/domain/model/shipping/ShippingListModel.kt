package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shipping

data class ShippingListModel(
        var services : List<ServicesItem> = emptyList()
)