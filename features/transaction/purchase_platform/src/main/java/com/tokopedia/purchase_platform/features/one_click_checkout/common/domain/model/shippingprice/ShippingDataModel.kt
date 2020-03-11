package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingprice

data class ShippingDataModel (
        var services: List<ServicesItemModel> = emptyList()
)

data class ServicesItemModel (
        var servicesName: String? = null,
        var servicesId: Int? = -1,
        var texts: TextsModel? = null,
        var isSelected: Boolean = false

)

data class TextsModel (
        var textRangePrice: String? = null,
        var textNotes: String? = null,
        var textsServiceDesc: String? = null
)