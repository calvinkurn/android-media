package com.tokopedia.purchase_platform.features.promo.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.purchase_platform.features.promo.presentation.adapter.PromoCheckoutAdapterTypeFactory

data class FragmentUiModel(
        var uiState: UiState
) {

    data class UiState(
            var hasAnyPromoSellected: Boolean = false,
            var hasFailedToLoad: Boolean = false
    )

}