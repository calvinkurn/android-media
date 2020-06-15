package com.tokopedia.oneclickcheckout.common.domain.model.shipping

sealed class ServicesItem

data class ServicesItemModelNoPrice(
        var serviceCode: String? = "",
        var serviceId: Int = -1,
        var servicesDuration: String? = "",
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
        var textNotes: String? = null,
        var textsServiceDesc: String = ""
)

data class LogisticPromoInfo(
        val imageUrl: String = ""
) : ServicesItem()