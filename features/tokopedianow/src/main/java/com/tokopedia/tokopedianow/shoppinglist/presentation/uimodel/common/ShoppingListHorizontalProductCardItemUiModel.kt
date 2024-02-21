package com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.common.ShoppingListHorizontalProductCardItemTypeFactory

data class ShoppingListHorizontalProductCardItemUiModel(
    val id: String = String.EMPTY,
    val image: String = String.EMPTY,
    val price: String = String.EMPTY,
    val name: String = String.EMPTY,
    val weight: String = String.EMPTY,
    val percentage: String = String.EMPTY,
    val slashPrice: String = String.EMPTY,
    val type: LayoutType,
    @TokoNowLayoutState val state: Int = TokoNowLayoutState.SHOW
): Visitable<ShoppingListHorizontalProductCardItemTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: ShoppingListHorizontalProductCardItemTypeFactory): Int = typeFactory.type(this)

    enum class LayoutType {
        AVAILABLE_SHOPPING_LIST,
        UNAVAILABLE_SHOPPING_LIST,
        PRODUCT_RECOMMENDATION
    }
}
