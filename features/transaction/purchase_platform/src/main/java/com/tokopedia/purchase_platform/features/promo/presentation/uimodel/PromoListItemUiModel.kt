package com.tokopedia.purchase_platform.features.promo.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.purchase_platform.features.promo.presentation.adapter.PromoCheckoutAdapterTypeFactory

data class PromoListItemUiModel(
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
            var promoId: Int = 0,
            var title: String = "",
            var subTitle: String = "",
            var errorMessage: String = "",
            var imageResourceUrl: String = "",
            var parentIdentifierId: Int = 0
    )

    data class UiState(
            var isEnabled: Boolean = false,
            var isSellected: Boolean = false,
            var isVisible: Boolean = true
    )

    companion object {

        fun clone(oldData: PromoListItemUiModel): PromoListItemUiModel {
            return PromoListItemUiModel(
                    uiData = UiData().apply {
                        promoId = oldData.uiData.promoId
                        title = oldData.uiData.title
                        subTitle = oldData.uiData.subTitle
                        errorMessage = oldData.uiData.errorMessage
                        imageResourceUrl = oldData.uiData.imageResourceUrl
                        parentIdentifierId = oldData.uiData.parentIdentifierId
                    },
                    uiState = UiState().apply {
                        isEnabled = oldData.uiState.isEnabled
                        isSellected = oldData.uiState.isSellected
                        isVisible = oldData.uiState.isVisible
                    }
            )
        }

    }

}