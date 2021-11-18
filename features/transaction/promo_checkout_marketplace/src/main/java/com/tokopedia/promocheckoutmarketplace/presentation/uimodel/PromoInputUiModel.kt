package com.tokopedia.promocheckoutmarketplace.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.promocheckoutmarketplace.presentation.adapter.PromoCheckoutAdapterTypeFactory

data class PromoInputUiModel(
        var uiData: UiData,
        var uiState: UiState
) : Visitable<PromoCheckoutAdapterTypeFactory> {

    override fun type(typeFactory: PromoCheckoutAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class UiData(
            var promoCode: String = "",
            var exception: Throwable? = null,
            var validLastSeenPromoCode: String = ""
    )

    data class UiState(
            var isButtonSelectEnabled: Boolean = false,
            var isError: Boolean = false,
            var isLoading: Boolean = false,
            var isValidLastSeenPromo: Boolean = false,
            var viewHeight: Int = 0
    )

}