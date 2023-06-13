package com.tokopedia.promocheckoutmarketplace.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.promocheckoutmarketplace.presentation.adapter.PromoCheckoutAdapterTypeFactory

data class PromoEligibilityHeaderUiModel(
    var uiData: UiData,
    var uiState: UiState
) : Visitable<PromoCheckoutAdapterTypeFactory> {

    override fun type(typeFactory: PromoCheckoutAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class UiData(
        var title: String = "",
        var subTitle: String = "",
        var tabId: String = ""
    )

    data class UiState(
        var isEnabled: Boolean = false
    )
}
