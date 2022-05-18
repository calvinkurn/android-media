package com.tokopedia.promocheckoutmarketplace.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.promocheckoutmarketplace.data.response.BenefitDetail
import com.tokopedia.promocheckoutmarketplace.data.response.ClashingInfo
import com.tokopedia.promocheckoutmarketplace.data.response.PromoCoachmark
import com.tokopedia.promocheckoutmarketplace.data.response.PromoInfo
import com.tokopedia.promocheckoutmarketplace.presentation.adapter.PromoCheckoutAdapterTypeFactory

data class PromoListItemUiModel(
        var id: String = "",
        var uiData: UiData,
        var uiState: UiState
) : Visitable<PromoCheckoutAdapterTypeFactory> {

    override fun type(typeFactory: PromoCheckoutAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class UiData(
            var promoId: String = "",
            var uniqueId: String = "",
            var shopId: Int = 0,
            var parentIdentifierId: Int = 0,
            var title: String = "",
            var currencyDetailStr: String = "",

            var errorMessage: String = "",
            var errorIcon: String = "",
            var benefitAmount: Int = 0,
            var promoCode: String = "",
            var couponAppLink: String = "",
            var coachMark: PromoCoachmark = PromoCoachmark(),
            // Store clashing info data from backend.
            // This should not be changed. Initialize once after get data response
            var clashingInfos: List<ClashingInfo> = emptyList(),
            // Store current applied promo causing this promo clash and can't be selected, based on data from #clashingInfo
            var currentClashingPromo: MutableList<String> = mutableListOf(),
            var promoInfos: List<PromoInfo> = emptyList(),
            var remainingPromoCount: Int = 0,
            var tabId: String = "",
            var shippingOptions: String = "",
            var paymentOptions: String = "",
            var benefitDetail: BenefitDetail = BenefitDetail()
    )

    data class UiState(
            var isParentEnabled: Boolean = false,
            var isSelected: Boolean = false,
            var isAttempted: Boolean = false,
            var isCausingOtherPromoClash: Boolean = false,
            var isRecommended: Boolean = false,
            var isDisabled: Boolean = false,
            var isHighlighted: Boolean = false,
            var isUpdateSelectionStateAction: Boolean = false,
            var isLastPromoItem: Boolean = false
    )

}