package com.tokopedia.promocheckoutmarketplace.presentation.listener

import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoSuggestionItemUiModel

interface PromoCheckoutSuggestionListener {

    fun onClickItem(model: PromoSuggestionItemUiModel)
}
