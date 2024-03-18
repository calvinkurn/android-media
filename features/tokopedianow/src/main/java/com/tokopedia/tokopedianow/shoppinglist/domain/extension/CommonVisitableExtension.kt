package com.tokopedia.tokopedianow.shoppinglist.domain.extension

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowErrorUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.Constant.INVALID_INDEX
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType

internal object CommonVisitableExtension {
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
}
