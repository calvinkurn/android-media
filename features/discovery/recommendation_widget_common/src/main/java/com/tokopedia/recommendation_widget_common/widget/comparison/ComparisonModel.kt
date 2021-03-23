package com.tokopedia.recommendation_widget_common.widget.comparison

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.widget.comparison.specs.SpecsListModel

data class ComparisonModel(
    val specsModel: SpecsListModel,
    val productCardModel: ProductCardModel,
    val productApplink: String
)