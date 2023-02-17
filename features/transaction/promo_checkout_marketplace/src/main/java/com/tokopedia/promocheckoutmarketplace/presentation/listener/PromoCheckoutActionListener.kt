package com.tokopedia.promocheckoutmarketplace.presentation.listener

import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.*

interface PromoCheckoutActionListener {

    fun onClickApplyRecommendedPromo()

    fun onClickPromoManualInputTextField()

    fun onClickApplyManualInputPromo(promoCode: String, isFromSuggestion: Boolean)

    fun onCLickClearManualInputPromo()

    fun onClickPromoListItem(element: PromoListItemUiModel, position: Int)

    fun onClickPromoItemDetail(element: PromoListItemUiModel)

    fun onClickEmptyStateButton(element: PromoEmptyStateUiModel)

    fun onClickErrorStateButton(destination: String)

    fun onTabSelected(element: PromoTabUiModel)

    fun onShowPromoItem(element: PromoListItemUiModel, position: Int)

    fun onShowPromoRecommendation(element: PromoRecommendationUiModel)
}
