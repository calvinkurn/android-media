package com.tokopedia.recommendation_widget_common.widget.comparison

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.comparison.specs.SpecsListModel

data class ComparisonModel(
    val specsModel: SpecsListModel,
    val productCardModel: ProductCardModel,
    val recommendationItem: RecommendationItem,
    val isCurrentItem: Boolean
): ImpressHolder()