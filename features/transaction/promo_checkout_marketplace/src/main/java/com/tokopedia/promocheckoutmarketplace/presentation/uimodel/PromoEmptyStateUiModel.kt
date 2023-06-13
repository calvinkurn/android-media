package com.tokopedia.promocheckoutmarketplace.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.promocheckoutmarketplace.presentation.adapter.PromoCheckoutAdapterTypeFactory

data class PromoEmptyStateUiModel(
    var uiData: UiData,
    var uiState: UiState
) : Visitable<PromoCheckoutAdapterTypeFactory> {

    override fun type(typeFactory: PromoCheckoutAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class UiData(
        var title: String = "",
        var description: String = "",
        var imageUrl: String = "",
        var buttonText: String = "",
        var emptyStateStatus: String = ""
    ) {
        companion object {
            const val LABEL_BUTTON_PHONE_VERIFICATION = "Verifikasi Nomor HP"
            const val LABEL_BUTTON_TRY_AGAIN = "Coba Lagi"
        }
    }

    data class UiState(
        var isShowButton: Boolean = false
    )
}
