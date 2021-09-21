package com.tokopedia.promocheckoutmarketplace.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.promocheckoutmarketplace.presentation.adapter.PromoCheckoutAdapterTypeFactory

data class PromoListItemUiModel(
        var id: String = "",
        var uiData: UiData,
        var uiState: UiState
) : Visitable<PromoCheckoutAdapterTypeFactory> {

    override fun type(typeFactory: PromoCheckoutAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PromoListItemUiModel

        if (uiData != other.uiData) return false
        if (uiState != other.uiState) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uiData.hashCode()
        result = 31 * result + uiState.hashCode()
        return result
    }

    data class UiData(
            var uniqueId: String = "",
            var shopId: Int = 0,
            var title: String = "",
            var subTitle: String = "",
            var errorMessage: String = "",
            var imageResourceUrls: List<String> = emptyList(),
            var parentIdentifierId: Int = 0,
            var benefitAmount: Int = 0,
            var promoCode: String = "",
            var couponAppLink: String = "",
            var currencyDetailStr: String = "",
            var coachMark: UiCoachmarkData = UiCoachmarkData(),
            // Store clashing info data from backend.
            // This should not be changed. Initialize once after get data response
            var clashingInfo: MutableMap<String, String> = mutableMapOf(),
            // Store current applied promo causing this promo clash and can't be selected, based on data from #clashingInfo
            var currentClashingPromo: MutableList<String> = mutableListOf()
    )

    data class UiCoachmarkData(
            val isShown: Boolean = false,
            val title: String = "",
            val content: String = ""
    )

    data class UiState(
            var isParentEnabled: Boolean = false,
            var isSelected: Boolean = false,
            var isAttempted: Boolean = false,
            var isCausingOtherPromoClash: Boolean = false,
            var isRecommended: Boolean = false,
            var isDisabled: Boolean = false
    ) {
        companion object {
            const val STATE_IS_ENABLED = "enabled"
            const val STATE_IS_DISABLED = "disabled"
        }
    }

}