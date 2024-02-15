package com.tokopedia.tokopedianow.shoppinglist.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel

object CommonVisitableMapper {
    fun MutableList<Visitable<*>>.addRecommendedProducts(
        recommendationWidget: RecommendationWidget
    ): MutableList<Visitable<*>> {
        val newList = recommendationWidget.recommendationItemList.map {
            ShoppingListHorizontalProductCardItemUiModel(
                id = it.productId.toString(),
                image = it.imageUrl,
                price = it.price,
                name = it.name,
                weight = it.labelGroupList.firstOrNull { label -> label.isWeightPosition() }?.title.orEmpty(),
                percentage = it.discountPercentage,
                slashPrice = it.slashedPrice,
                type = ShoppingListHorizontalProductCardItemUiModel.LayoutType.PRODUCT_RECOMMENDATION,
                state = ShoppingListHorizontalProductCardItemUiModel.LayoutState.NORMAL
            )
        }
        addAll(newList)
        return this
    }
}
