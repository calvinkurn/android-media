package com.tokopedia.promocheckoutmarketplace.presentation.listener

import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoEligibilityHeaderUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoEmptyStateUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListHeaderUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListItemUiModel

interface PromoCheckoutActionListener {

    fun onClickApplyRecommendedPromo()

    fun onClickPromoManualInputTextField()
    
    fun onClickApplyManualInputPromo(promoCode: String, isFromLastSeen: Boolean)

    fun onCLickClearManualInputPromo()

    fun onClickPromoListHeader(element: PromoListHeaderUiModel)

    fun onClickPromoListItem(element: PromoListItemUiModel, position: Int)

    fun onClickPromoItemDetail(element: PromoListItemUiModel)

    fun onClickPromoEligibilityHeader(element: PromoEligibilityHeaderUiModel)

    fun onClickEmptyStateButton(element: PromoEmptyStateUiModel)
}