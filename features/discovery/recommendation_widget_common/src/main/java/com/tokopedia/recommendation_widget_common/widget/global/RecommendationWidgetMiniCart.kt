package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource

data class RecommendationWidgetMiniCart(
    val shopId: String = "",
    val miniCartSource: MiniCartSource? = null,
) {

    val hasValidShopId
        get() = shopId.isNotEmpty() && shopId != Int.ZERO.toString()
}
