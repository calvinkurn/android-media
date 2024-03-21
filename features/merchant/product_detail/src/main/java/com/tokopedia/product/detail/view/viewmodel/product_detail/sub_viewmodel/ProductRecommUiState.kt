package com.tokopedia.product.detail.view.viewmodel.product_detail.sub_viewmodel

import com.tokopedia.product.detail.view.viewmodel.product_detail.event.ViewState
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class ProductRecommUiState(
    val data: ViewState<RecommendationWidget> = ViewState.Loading(true),
    val pageName: String = "",
    val alreadyCollected: Boolean = false
)
