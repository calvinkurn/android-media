package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingprice

data class ShippingDataModel (
        var services: List<ServicesItemModel> = emptyList()
)

sealed class ServicesItem

class ServicesItemModelNoPrice (
        var serviceCode: String? = null,
        var serviceId: Int = -1,
        var servicesDuration: String? = null,
        var shipperIds: Int? = null,
        var spids: Int? = null,
        var isSelected: Boolean = false
) : ServicesItem()

class ServicesItemModel (
        var servicesName: String? = null,
        var servicesId: Int = -1,
        var texts: TextsModel? = null,
        var isSelected: Boolean = false

) : ServicesItem()

data class TextsModel (
        var textRangePrice: String? = null,
        var textNotes: String? = null,
        var textsServiceDesc: String = ""
)