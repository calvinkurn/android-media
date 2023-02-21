package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class ViewToViewDataModel(
    val id: String,
    val shopId: String,
    val productName: String,
    val minOrder: Int,
    val price: String,
    val productModel: ProductCardModel,
    val recommendationItem: RecommendationItem,
)

