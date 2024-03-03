package com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.common.ShoppingListHorizontalProductCardItemTypeFactory

data class ShoppingListHorizontalProductCardItemUiModel(
    val id: String = String.EMPTY,
    val image: String = String.EMPTY,
    val price: String = String.EMPTY,
    val priceInt: Double = Int.ZERO.toDouble(),
    val name: String = String.EMPTY,
    val weight: String = String.EMPTY,
    val percentage: String = String.EMPTY,
    val slashPrice: String = String.EMPTY,
    val isSelected: Boolean = false,
    val appLink: String = String.EMPTY,
    @TokoNowLayoutState val state: Int = TokoNowLayoutState.SHOW,
    val productLayoutType: ShoppingListProductLayoutType
): Visitable<ShoppingListHorizontalProductCardItemTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: ShoppingListHorizontalProductCardItemTypeFactory): Int = typeFactory.type(this)

    fun getChangePayload(item: ShoppingListHorizontalProductCardItemUiModel): Any? {
        return when {
            isSelected != item.isSelected && productLayoutType == item.productLayoutType -> true
            else -> null
        }
    }
}
