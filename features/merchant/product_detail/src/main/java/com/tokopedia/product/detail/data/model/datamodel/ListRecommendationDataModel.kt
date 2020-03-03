package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class ListRecommendationDataModel(
        val recomData: List<RecommendationWidget>? = null,
        val cardModel: List<ProductCardModel>? = null
)