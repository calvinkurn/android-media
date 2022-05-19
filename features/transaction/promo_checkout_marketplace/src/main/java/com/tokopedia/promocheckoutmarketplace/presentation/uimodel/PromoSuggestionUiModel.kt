package com.tokopedia.promocheckoutmarketplace.presentation.uimodel

data class PromoSuggestionUiModel(
        var uiData: UiData
) {

    data class UiData(
            var promoSuggestionItemUiModelList: List<PromoSuggestionItemUiModel> = emptyList()
    )

}