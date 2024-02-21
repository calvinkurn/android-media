package com.tokopedia.tokopedianow.shoppinglist.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel.LayoutType.PRODUCT_RECOMMENDATION
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.SHOW
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel

internal object CommonVisitableMapper {
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
                type = PRODUCT_RECOMMENDATION,
                state = SHOW
            )
        }
        addAll(newList)
        return this
    }
}
