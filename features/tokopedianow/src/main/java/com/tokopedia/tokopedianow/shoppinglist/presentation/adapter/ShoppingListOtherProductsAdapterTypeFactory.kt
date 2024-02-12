package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.ShoppingListHorizontalProductCardItemViewHolder

class ShoppingListOtherProductsAdapterTypeFactory :
    BaseAdapterTypeFactory(),
    ShoppingListHorizontalProductCardItemTypeFactory {

    override fun type(uiModel: ShoppingListHorizontalProductCardItemUiModel): Int = ShoppingListHorizontalProductCardItemViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
       return when (type) {
           ShoppingListHorizontalProductCardItemViewHolder.LAYOUT -> ShoppingListHorizontalProductCardItemViewHolder(parent)
           else -> super.createViewHolder(parent, type)
       }
    }
}
