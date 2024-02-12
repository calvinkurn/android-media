package com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.ShoppingListHorizontalProductCardItemTypeFactory

data class ShoppingListHorizontalProductCardItemUiModel(
    val id: String,
    val image: String,
    val eta: String,
    val price: String,
    val name: String,
    val weight: String,
    val percentage: String,
    val slashPrice: String,
    val type: LayoutType
): Visitable<ShoppingListHorizontalProductCardItemTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: ShoppingListHorizontalProductCardItemTypeFactory): Int = typeFactory.type(this)

    enum class LayoutType {
        ATC_WISHLIST,
        EMPTY_STOCK,
        PRODUCT_RECOMMENDATION
    }
}
