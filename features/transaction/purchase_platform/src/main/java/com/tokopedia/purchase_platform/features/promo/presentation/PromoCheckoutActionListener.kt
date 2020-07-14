package com.tokopedia.purchase_platform.features.promo.presentation

import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.*

interface PromoCheckoutActionListener {

    fun onClickApplyRecommendedPromo()

    fun onClickApplyManualInputPromo(promoCode: String)

    fun onCLickClearManualInputPromo()

    fun onClickPromoListHeader(element: PromoListHeaderUiModel)

    fun onClickPromoListItem(element: PromoListItemUiModel)

    fun onClickPromoItemDetail(element: PromoListItemUiModel)

    fun onClickPromoEligibilityHeader(element: PromoEligibilityHeaderUiModel)

    fun onClickEmptyStateButton(element: PromoEmptyStateUiModel)
}