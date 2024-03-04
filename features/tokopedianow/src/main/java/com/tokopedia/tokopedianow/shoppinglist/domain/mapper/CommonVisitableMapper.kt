package com.tokopedia.tokopedianow.shoppinglist.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.PRODUCT_RECOMMENDATION
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.SHOW
import com.tokopedia.tokopedianow.common.model.TokoNowErrorUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.Constant.INVALID_INDEX
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType

internal object CommonVisitableMapper {
    fun mapRecommendedProducts(
        recommendationWidget: RecommendationWidget
    ): List<ShoppingListHorizontalProductCardItemUiModel> = recommendationWidget.recommendationItemList.map {
        ShoppingListHorizontalProductCardItemUiModel(
            id = it.productId.toString(),
            image = it.imageUrl,
            price = it.price,
            priceInt = it.priceInt.toDouble(),
            name = it.name,
            weight = it.labelGroupList.firstOrNull { label -> label.isWeightPosition() }?.title.orEmpty(),
            percentage = it.discountPercentage,
            slashPrice = it.slashedPrice,
            appLink = it.appUrl,
            productLayoutType = PRODUCT_RECOMMENDATION,
            state = SHOW
        )
    }

    fun MutableList<Visitable<*>>.modifyProduct(
        productId: String,
        @TokoNowLayoutState state: Int
    ): MutableList<Visitable<*>> {
        val index = indexOfFirst { it is ShoppingListHorizontalProductCardItemUiModel && it.id == productId }

        if (index != INVALID_INDEX) {
            val item = this[index] as ShoppingListHorizontalProductCardItemUiModel
            removeAt(index)
            add(index, item.copy(state = state))
        }

        return this
    }

    fun MutableList<Visitable<*>>.modifyProduct(
        productId: String,
        isSelected: Boolean
    ): MutableList<Visitable<*>> {
        val index = indexOfFirst { it is ShoppingListHorizontalProductCardItemUiModel && it.id == productId }

        if (index != INVALID_INDEX) {
            val item = this[index] as ShoppingListHorizontalProductCardItemUiModel
            removeAt(index)
            add(index, item.copy(isSelected = isSelected))
        }

        return this
    }

    fun MutableList<Visitable<*>>.addErrorState(
        isFullPage: Boolean,
        throwable: Throwable
    ): MutableList<Visitable<*>> {
        add(
            TokoNowErrorUiModel(
                isFullPage = isFullPage,
                throwable = throwable
            )
        )
        return this
    }

    fun MutableList<ShoppingListHorizontalProductCardItemUiModel>.modifyProduct(
        productId: String,
        productLayoutType: ShoppingListProductLayoutType
    ) {
        val index = indexOfFirst { it.id == productId }

        if (index != INVALID_INDEX) {
            val item = this[index]
            removeAt(index)
            add(index, item.copy(productLayoutType = productLayoutType))
        }
    }
}
