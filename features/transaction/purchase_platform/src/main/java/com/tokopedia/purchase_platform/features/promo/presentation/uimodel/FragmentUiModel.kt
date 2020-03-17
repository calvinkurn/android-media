package com.tokopedia.purchase_platform.features.promo.presentation.uimodel

import com.tokopedia.purchase_platform.common.feature.tokopointstnc.TokoPointsTncUiModel

data class FragmentUiModel(
        var uiData: UiData,
        var uiState: UiState
) {

    data class UiData(
            var pageSource: Int = 0,
            var totalBenefit: Int = 0,
            var usedPromoCount: Int = 0,
            var exception: Throwable? = null,
            var tokopointsTncLabel: String = "",
            var tokopointsTncTitle: String = "",
            var tokopointsTncDetails: ArrayList<TokoPointsTncUiModel> = ArrayList(),
            var preAppliedPromoCode: List<String> = emptyList()
    )

    data class UiState(
            var isLoading: Boolean = false,
            var hasPreAppliedPromo: Boolean = false,
            var hasAnyPromoSelected: Boolean = false,
            var hasFailedToLoad: Boolean = false
    )

}