package com.tokopedia.purchase_platform.features.promo.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.purchase_platform.features.promo.presentation.adapter.PromoCheckoutAdapterTypeFactory

data class FragmentUiModel(
        var uiData: UiData,
        var uiState: UiState
) {

    data class UiData(
            var promoInputViewHeight: Int = 0
    )

    data class UiState(
            var hasPresellectedPromo: Boolean = false,
            var hasAnyPromoSelected: Boolean = false,
            var hasFailedToLoad: Boolean = false
    )

}