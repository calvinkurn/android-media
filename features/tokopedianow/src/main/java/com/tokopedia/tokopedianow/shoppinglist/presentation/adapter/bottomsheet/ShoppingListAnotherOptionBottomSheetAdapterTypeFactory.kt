package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.bottomsheet

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.common.ShoppingListHorizontalProductCardItemTypeFactory
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.bottomsheet.ShoppingListAnotherOptionBottomSheetEmptyStateUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.bottomsheet.ShoppingListAnotherOptionBottomSheetErrorStateUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.bottomsheet.ShoppingListAnotherOptionBottomSheetEmptyStateViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.bottomsheet.ShoppingListAnotherOptionBottomSheetErrorStateViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.common.ShoppingListHorizontalProductCardItemViewHolder

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
