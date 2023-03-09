package com.tokopedia.promocheckoutmarketplace.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.promocheckoutmarketplace.data.response.SectionTab
import com.tokopedia.promocheckoutmarketplace.presentation.adapter.PromoCheckoutAdapterTypeFactory

class PromoTabUiModel(
    var uiData: UiData,
    var uiState: UiState
) : Visitable<PromoCheckoutAdapterTypeFactory> {

    override fun type(typeFactory: PromoCheckoutAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class UiData(
        var tabs: List<SectionTab> = emptyList()
    )

    data class UiState(
        var isInitialization: Boolean = false,
        var isSelectionAction: Boolean = false,
        var selectedTabPosition: Int = 0
    )
}
