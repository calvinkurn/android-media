package com.tokopedia.promocheckoutmarketplace.presentation

import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoLastSeenItemUiModel

interface PromoCheckoutLastSeenListener {

    fun onClickItem(model: PromoLastSeenItemUiModel)

}