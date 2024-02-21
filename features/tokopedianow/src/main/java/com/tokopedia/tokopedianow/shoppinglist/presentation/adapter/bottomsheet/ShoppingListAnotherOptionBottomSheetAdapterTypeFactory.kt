package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.bottomsheet

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowErrorTypeFactory
import com.tokopedia.tokopedianow.common.model.TokoNowErrorUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowErrorViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.common.ShoppingListHorizontalProductCardItemTypeFactory
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.bottomsheet.ShoppingListAnotherOptionBottomSheetEmptyStateUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.bottomsheet.ShoppingListAnotherOptionBottomSheetEmptyStateViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.common.ShoppingListHorizontalProductCardItemViewHolder

class ShoppingListAnotherOptionBottomSheetAdapterTypeFactory(
    private val errorListener: TokoNowErrorViewHolder.TokoNowErrorListener
):
    BaseAdapterTypeFactory(),
    ShoppingListHorizontalProductCardItemTypeFactory,
    ShoppingListAnotherOptionBottomSheetEmptyStateTypeFactory,
    TokoNowErrorTypeFactory{
    override fun type(uiModel: ShoppingListHorizontalProductCardItemUiModel): Int = ShoppingListHorizontalProductCardItemViewHolder.LAYOUT
    override fun type(uiModel: ShoppingListAnotherOptionBottomSheetEmptyStateUiModel): Int = ShoppingListAnotherOptionBottomSheetEmptyStateViewHolder.LAYOUT
    override fun type(uiModel: TokoNowErrorUiModel): Int = TokoNowErrorViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
       return when (type) {
           ShoppingListHorizontalProductCardItemViewHolder.LAYOUT -> ShoppingListHorizontalProductCardItemViewHolder(parent)
           ShoppingListAnotherOptionBottomSheetEmptyStateViewHolder.LAYOUT -> ShoppingListAnotherOptionBottomSheetEmptyStateViewHolder(parent)
           TokoNowErrorViewHolder.LAYOUT -> TokoNowErrorViewHolder(parent, errorListener)
           else -> super.createViewHolder(parent, type)
       }
    }
}
