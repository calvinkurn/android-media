package com.tokopedia.product.detail.data.model.addtocartrecommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.detail.view.adapter.AddToCartDoneTypeFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class AddToCartDoneRecommendationCarouselDataModel (
        val recommendationWidget: AddToCartDoneRecommendationWidgetDataModel,
        val shopId: Int
) : Visitable<AddToCartDoneTypeFactory> {
    override fun type(typeFactory: AddToCartDoneTypeFactory): Int {
        return typeFactory.type(this)
    }

}

data class AddToCartDoneRecommendationWidgetDataModel(
        val title: String = "",
        val foreignTitle: String = "",
        val source: String = "",
        val tid: String = "",
        val widgetUrl: String = "",
        val layoutType: String = "",
        val seeMoreAppLink: String = "",
        val currentPage: Int = 0,
        val nextPage: Int = 0,
        val prevPage: Int = 0,
        val hasNext: Boolean = false,
        val pageName: String = "",
        val recommendationItemList: List<AddToCartDoneRecommendationItemDataModel> = listOf()
)

data class AddToCartDoneRecommendationItemDataModel(
        val recommendationItem: RecommendationItem,
        var isAddedToCart: Boolean = false
)