package com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListStaticLayoutId
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main.ShoppingListTypeFactory

data class ShoppingListTopCheckAllUiModel(
    val id: String = ShoppingListStaticLayoutId.TOP_CHECK_ALL.name,
    val productState: ShoppingListProductState,
    val isSelected: Boolean
): Visitable<ShoppingListTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: ShoppingListTypeFactory): Int = typeFactory.type(this)

    fun getChangePayload(item: ShoppingListTopCheckAllUiModel): Any? {
        return when {
            isSelected != item.isSelected -> true
            else -> null
        }
    }
}
