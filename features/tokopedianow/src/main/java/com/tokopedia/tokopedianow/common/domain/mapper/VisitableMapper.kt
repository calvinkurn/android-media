package com.tokopedia.tokopedianow.common.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowProductCardCarouselItemUiModel

object VisitableMapper {
    const val NO_ORDER_QUANTITY = 0

    fun MutableList<Visitable<*>>.updateProductCardCarouselItemQuantity(productId: String, quantity: Int) {
        filterIsInstance<TokoNowProductCardCarouselItemUiModel>().firstOrNull { it.productCardModel.productId == productId }?.let { model ->
            val index = indexOf(model)
            val newModel = model.copy(
                productCardModel = model.productCardModel.copy(
                    orderQuantity = quantity
                )
            )
            this[index] = newModel
        }
    }

    fun MutableList<Visitable<*>>.resetAllProductCardCarouselItemQuantities() {
        filterIsInstance<TokoNowProductCardCarouselItemUiModel>().forEachIndexed { index, model ->
            val newModel = model.copy(
                productCardModel = model.productCardModel.copy(
                    orderQuantity = NO_ORDER_QUANTITY
                )
            )
            this[index] = newModel
        }
    }
}
