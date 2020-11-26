package com.tokopedia.promocheckoutmarketplace.presentation.uimodel

data class PromoLastSeenUiModel(
        var uiData: UiData
) {

    data class UiData(
            var promoLastSeenItemUiModelList: List<PromoLastSeenItemUiModel> = emptyList()
    )

}