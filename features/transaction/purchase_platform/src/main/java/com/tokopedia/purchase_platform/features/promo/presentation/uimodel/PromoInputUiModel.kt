package com.tokopedia.purchase_platform.features.promo.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.purchase_platform.features.promo.presentation.adapter.PromoCheckoutAdapterTypeFactory

data class PromoInputUiModel(
        var uiData: UiData,
        var uiState: UiState
) : Visitable<PromoCheckoutAdapterTypeFactory> {

    override fun type(typeFactory: PromoCheckoutAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PromoInputUiModel

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
            var promoCode: String = "",
            var exception: Throwable? = null
    )

    data class UiState(
            var isButtonSelectEnabled: Boolean = false,
            var isError: Boolean = false,
            var isLoading: Boolean = false
    )

}