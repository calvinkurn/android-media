package com.tokopedia.one.click.checkout.common.domain.model.shipping

data class ShippingListModel(
        var services : List<ServicesItem> = emptyList()
)