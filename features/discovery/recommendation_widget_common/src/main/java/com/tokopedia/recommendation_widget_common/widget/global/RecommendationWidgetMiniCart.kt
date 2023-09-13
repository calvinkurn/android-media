package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.minicart.common.domain.usecase.MiniCartSource

data class RecommendationWidgetMiniCart(
    val miniCartSource: MiniCartSource? = null,
)
