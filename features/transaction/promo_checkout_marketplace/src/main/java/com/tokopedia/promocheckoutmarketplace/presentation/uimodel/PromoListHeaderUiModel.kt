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

    data class UiData(
            var title: String = "",
            var subTitle: String = "",
            var iconUnify: String = "",
            var identifierId: Int = 0,
            var tabId: String = ""
    )

    data class UiState(
            var isEnabled: Boolean = false,
            var hasSelectedPromoItem: Boolean = false,
            var isFirstPromoHeader: Boolean = false
    )

}