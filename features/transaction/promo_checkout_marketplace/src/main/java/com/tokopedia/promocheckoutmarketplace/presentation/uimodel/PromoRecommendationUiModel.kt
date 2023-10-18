package com.tokopedia.promocheckoutmarketplace.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.promocheckoutmarketplace.presentation.adapter.PromoCheckoutAdapterTypeFactory

data class PromoRecommendationUiModel(
    var uiData: UiData,
    var uiState: UiState
) : Visitable<PromoCheckoutAdapterTypeFactory> {

    override fun type(typeFactory: PromoCheckoutAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class UiData(
        var promoCodes: List<String> = emptyList(),
        var promoCount: Int = 0,
        var promoTotalBenefit: Int = 0
    )

    data class UiState(
        var isInitialization: Boolean = false,
        var isButtonSelectEnabled: Boolean = false
    )
}
