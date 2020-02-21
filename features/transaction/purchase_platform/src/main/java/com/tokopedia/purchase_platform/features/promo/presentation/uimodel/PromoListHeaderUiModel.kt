package com.tokopedia.purchase_platform.features.promo.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.purchase_platform.features.promo.presentation.adapter.PromoCheckoutAdapterTypeFactory

class PromoListHeaderUiModel(
        var uiData: UiData,
        var uiState: UiState
) : Visitable<PromoCheckoutAdapterTypeFactory> {

    override fun type(typeFactory: PromoCheckoutAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class UiData(
            var title: String = "",
            var description: String = "",
            var imageResourceUrl: String = "",
            var identifierId: Int = 0
    )

    data class UiState(
            var isCollapsed: Boolean = false
    )

}