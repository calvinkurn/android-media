package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.anotheroptionbottomsheet

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.common.ShoppingListHorizontalProductCardItemTypeFactory
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListAnotherOptionBottomSheetEmptyStateUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListAnotherOptionBottomSheetErrorStateUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.ShoppingListAnotherOptionBottomSheetEmptyStateViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.ShoppingListAnotherOptionBottomSheetErrorStateViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.ShoppingListHorizontalProductCardItemViewHolder

class ShoppingListAnotherOptionBottomSheetAdapterTypeFactory(
    private val bottomSheetErrorStateListener: ShoppingListAnotherOptionBottomSheetErrorStateViewHolder.ShoppingListAnotherOptionBottomSheetErrorStateListener? = null
):
    BaseAdapterTypeFactory(),
    ShoppingListHorizontalProductCardItemTypeFactory,
    ShoppingListAnotherOptionBottomSheetEmptyStateTypeFactory,
    ShoppingListAnotherOptionBottomSheetErrorStateTypeFactory {
    override fun type(uiModel: ShoppingListHorizontalProductCardItemUiModel): Int = ShoppingListHorizontalProductCardItemViewHolder.LAYOUT
    override fun type(uiModel: ShoppingListAnotherOptionBottomSheetEmptyStateUiModel): Int = ShoppingListAnotherOptionBottomSheetEmptyStateViewHolder.LAYOUT
    override fun type(uiModel: ShoppingListAnotherOptionBottomSheetErrorStateUiModel): Int = ShoppingListAnotherOptionBottomSheetErrorStateViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
       return when (type) {
           ShoppingListHorizontalProductCardItemViewHolder.LAYOUT -> ShoppingListHorizontalProductCardItemViewHolder(parent)
           ShoppingListAnotherOptionBottomSheetEmptyStateViewHolder.LAYOUT -> ShoppingListAnotherOptionBottomSheetEmptyStateViewHolder(parent)
           ShoppingListAnotherOptionBottomSheetErrorStateViewHolder.LAYOUT -> ShoppingListAnotherOptionBottomSheetErrorStateViewHolder(parent, bottomSheetErrorStateListener)
           else -> super.createViewHolder(parent, type)
       }
    }
}
