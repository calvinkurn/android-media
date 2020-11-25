package com.tokopedia.promocheckoutmarketplace.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.promocheckoutmarketplace.presentation.adapter.PromoCheckoutAdapterTypeFactory

class PromoListHeaderUiModel(
        var uiData: UiData,
        var uiState: UiState
) : Visitable<PromoCheckoutAdapterTypeFactory> {

    override fun type(typeFactory: PromoCheckoutAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PromoListHeaderUiModel

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
            var title: String = "",
            var subTitle: String = "",
            var iconUrl: String = "",
            var identifierId: Int = 0,
            var tmpPromoItemList: List<PromoListItemUiModel> = emptyList()
    ) {
        companion object {
            const val PROMO_TYPE_GLOBAL = 1
            const val PROMO_TYPE_MERCHANT_OFFICIAL = 2
            const val PROMO_TYPE_POWER_MERCHANT = 3
        }
    }

    data class UiState(
            var isEnabled: Boolean = false,
            var isCollapsed: Boolean = false,
            var hasSelectedPromoItem: Boolean = false
    )

}