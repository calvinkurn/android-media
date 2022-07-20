package com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder.MvcLockedToProductSortViewHolder
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductSortUiModel

class MvcLockedToProductSortListBottomSheetTypeFactory(
    private val sortItemListener: MvcLockedToProductSortViewHolder.MvcLockedToProductSortViewHolderListener
) : BaseAdapterTypeFactory() {
    fun type(uiModel: MvcLockedToProductSortUiModel): Int {
        return MvcLockedToProductSortViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            MvcLockedToProductSortViewHolder.LAYOUT -> {
                MvcLockedToProductSortViewHolder(parent, sortItemListener)
            }
            else -> {
                super.createViewHolder(parent, type)
            }
        }
    }

}