package com.tokopedia.checkout.view.feature.emptycart2.uimodel

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

data class RecommendationItemUiModel(
        var recommendationItem: RecommendationItem? = null,
        var isLastItem: Boolean = false
)