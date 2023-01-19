package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class ViewToViewRecommendationResult(
    val widget: RecommendationWidget? = null,
    val data: List<ViewToViewDataModel> = emptyList(),
)
