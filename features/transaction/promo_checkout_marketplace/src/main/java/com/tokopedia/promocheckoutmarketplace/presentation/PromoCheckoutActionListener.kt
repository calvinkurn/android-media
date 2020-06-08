package com.tokopedia.promocheckoutmarketplace.presentation

import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.*

interface PromoCheckoutActionListener {

    fun onClickApplyRecommendedPromo()

    fun onClickPromoManualInputTextField()

    fun onTypePromoManualInput(promoCode: String)

    fun onClickApplyManualInputPromo(promoCode: String)

    fun onCLickClearManualInputPromo()

    fun onClickPromoListHeader(element: PromoListHeaderUiModel)

    fun onClickPromoListItem(element: PromoListItemUiModel)

    fun onClickPromoItemDetail(element: PromoListItemUiModel)

    fun onClickPromoEligibilityHeader(element: PromoEligibilityHeaderUiModel)

    fun onClickEmptyStateButton(element: PromoEmptyStateUiModel)
}