package com.tokopedia.tokopedianow.shoppinglist.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.bottomsheet.ShoppingListAnotherOptionBottomSheetEmptyStateUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.LOADING
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.PRODUCT_RECOMMENDATION

internal object AnotherOptionBottomSheetVisitableMapper {
    fun MutableList<Visitable<*>>.addLoadingState(): MutableList<Visitable<*>> {
        val newList = arrayListOf(
            ShoppingListHorizontalProductCardItemUiModel(
                productLayoutType = PRODUCT_RECOMMENDATION,
                state = LOADING
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                productLayoutType = PRODUCT_RECOMMENDATION,
                state = LOADING
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                productLayoutType = PRODUCT_RECOMMENDATION,
                state = LOADING
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                productLayoutType = PRODUCT_RECOMMENDATION,
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
}
