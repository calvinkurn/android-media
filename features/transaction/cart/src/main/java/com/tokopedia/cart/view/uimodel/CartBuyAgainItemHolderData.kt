package com.tokopedia.cart.view.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel

data class CartBuyAgainItemHolderData(
    val recommendationItem: RecommendationItem
) : CartBuyAgainItem, ImpressHolder()
