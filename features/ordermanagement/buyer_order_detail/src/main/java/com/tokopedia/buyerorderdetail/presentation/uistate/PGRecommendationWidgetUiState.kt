package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.buyerorderdetail.presentation.model.PGRecommendationWidgetUiModel

sealed interface PGRecommendationWidgetUiState {
    object Loading : PGRecommendationWidgetUiState

    sealed interface HasData : PGRecommendationWidgetUiState {
        val data: PGRecommendationWidgetUiModel

        data class Reloading(
            override val data: PGRecommendationWidgetUiModel
        ) : HasData

        data class Showing(
            override val data: PGRecommendationWidgetUiModel
        ) : HasData
    }

    data class Error(
        val throwable: Throwable?
    ) : PGRecommendationWidgetUiState
}

