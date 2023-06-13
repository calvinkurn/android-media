package com.tokopedia.promocheckoutmarketplace.presentation.uimodel

data class PromoSuggestionItemUiModel(
    var uiData: UiData
) {

    data class UiData(
        var promoCode: String = "",
        var promoTitle: String = ""
    )
}
