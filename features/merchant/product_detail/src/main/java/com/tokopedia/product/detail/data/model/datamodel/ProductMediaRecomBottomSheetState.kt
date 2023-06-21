package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

sealed interface ProductMediaRecomBottomSheetState {
    object Dismissed : ProductMediaRecomBottomSheetState
    data class Loading(
        val title: String
    ) : ProductMediaRecomBottomSheetState

    data class ShowingData(
        val title: String,
        val recomWidgetData: RecommendationWidget
    ) : ProductMediaRecomBottomSheetState

    data class ShowingError(
        val title: String,
        val error: Throwable
    ) : ProductMediaRecomBottomSheetState
}
