package com.tokopedia.purchase_platform.features.promo.presentation.listener

import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoEligibilityHeaderUiModel
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoListHeaderUiModel
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoListItemUiModel
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoRecommendationUiModel

interface PromoCheckoutActionListener {

    fun onClickApplyRecommendedPromo(element: PromoRecommendationUiModel)

    fun onClickApplyManualInputPromo(promoCode: String)

    fun onClickPromoListHeader(element: PromoListHeaderUiModel)

    fun onClickPromoListItem(element: PromoListItemUiModel)

    fun onClickPromoItemDetail(element: PromoListItemUiModel)

    fun onClickPromoEligibilityHeader(position: Int, element: PromoEligibilityHeaderUiModel)

}