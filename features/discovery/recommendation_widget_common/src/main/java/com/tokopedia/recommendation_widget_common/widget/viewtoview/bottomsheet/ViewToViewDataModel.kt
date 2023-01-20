package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

sealed interface ViewToViewDataModel {
    data class Loading(
        val hasAtc: Boolean,
    ) : ViewToViewDataModel

    data class Product(
        val id: String,
        val shopId: String,
        val productName: String,
        val minOrder: Int,
        val price: String,
        val productModel: ProductCardModel,
        val recommendationItem: RecommendationItem,
    ) : ImpressHolder(), ViewToViewDataModel
}

