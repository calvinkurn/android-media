package com.tokopedia.oneclickcheckout.preference.edit.view.shipping.model

data class ShippingListModel(
        var services : List<ServicesItem> = emptyList()
)

sealed class ServicesItem

data class ServicesItemModelNoPrice(
        var serviceId: Int = -1,
        var servicesDuration: String = "",
        var isSelected: Boolean = false
) : ServicesItem()

data class ServicesItemModel(
        var servicesName: String? = null,
        var servicesId: Int = -1,
        var texts: TextsModel? = null,
        var isSelected: Boolean = false,
        var errorMessage: String = "",
        var errorId: String = ""
) : ServicesItem()

data class TextsModel(
        var textRangePrice: String? = null,
        var textsServiceDesc: String = "",
        var textEta: String? = null
)

data class LogisticPromoInfo(
        val imageUrl: String = "",
        val isNewLayout: Boolean = false
) : ServicesItem()