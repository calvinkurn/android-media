package com.tokopedia.editshipping.domain.model

data class ValidateShippingModel (
        var data: Data = Data()
)

data class Data(
        var showPopup: Boolean = false,
        var tickerTitle: String = "",
        var tickerContent: String = "",
        var popupContent: List<String> = listOf()
)