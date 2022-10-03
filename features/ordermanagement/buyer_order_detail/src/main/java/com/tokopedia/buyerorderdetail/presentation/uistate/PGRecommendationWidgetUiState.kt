package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.buyerorderdetail.presentation.model.PGRecommendationWidgetUiModel

sealed class PGRecommendationWidgetUiState {
    object Loading : PGRecommendationWidgetUiState()

    data class Showing(
        val data: PGRecommendationWidgetUiModel
    ) : PGRecommendationWidgetUiState()

    data class Error(
        val throwable: Throwable
    ) : PGRecommendationWidgetUiState()
}

