package com.tokopedia.tokopedianow.shoppinglist.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListAnotherOptionBottomSheetEmptyStateUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListAnotherOptionBottomSheetErrorStateUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel.LayoutState.NORMAL
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel.LayoutState.LOADING
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel.LayoutType.PRODUCT_RECOMMENDATION

object ShoppingListAnotherOptionBottomSheetVisitableMapper {
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
                state = NORMAL
            )
        }
        addAll(newList)
        return this
    }

    fun MutableList<Visitable<*>>.addShimmeringRecommendedProducts(): MutableList<Visitable<*>> {
        val newList = arrayListOf(
            ShoppingListHorizontalProductCardItemUiModel(
                type = PRODUCT_RECOMMENDATION,
                state = LOADING
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                type = PRODUCT_RECOMMENDATION,
                state = LOADING
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                type = PRODUCT_RECOMMENDATION,
                state = LOADING
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                type = PRODUCT_RECOMMENDATION,
                state = LOADING
            )
        )
        addAll(newList)
        return this
    }

    fun MutableList<Visitable<*>>.addEmptyState(): MutableList<Visitable<*>> {
        add(ShoppingListAnotherOptionBottomSheetEmptyStateUiModel())
        return this
    }

    fun MutableList<Visitable<*>>.addErrorState(
        throwable: Throwable
    ): MutableList<Visitable<*>> {
        add(ShoppingListAnotherOptionBottomSheetErrorStateUiModel(throwable = throwable))
        return this
    }
}
