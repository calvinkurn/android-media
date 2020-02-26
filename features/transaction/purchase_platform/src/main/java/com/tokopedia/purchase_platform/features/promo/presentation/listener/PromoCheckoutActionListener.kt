package com.tokopedia.purchase_platform.features.promo.presentation.listener

import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoListItemUiModel

interface PromoCheckoutActionListener {

    fun onClickApplyRecommendedPromo()

    fun onClickApplyManualInputPromo(promoCode: String)

    fun onClickPromoListHeader(itemPosition: Int)

    fun onClickPromoListItem(element: PromoListItemUiModel)
}