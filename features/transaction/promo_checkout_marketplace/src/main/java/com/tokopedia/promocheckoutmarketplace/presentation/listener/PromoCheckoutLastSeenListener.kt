package com.tokopedia.promocheckoutmarketplace.presentation.listener

import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoLastSeenItemUiModel

interface PromoCheckoutLastSeenListener {

    fun onClickItem(model: PromoLastSeenItemUiModel)

}