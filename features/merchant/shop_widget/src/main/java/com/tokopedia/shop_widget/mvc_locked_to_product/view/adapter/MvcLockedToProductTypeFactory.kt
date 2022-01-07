package com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder.MvcLockedToProductProductGridViewHolder
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder.MvcLockedToProductTotalProductAndSortViewHolder
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder.MvcLockedToProductVoucherViewHolder
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductVoucherUiModel
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductProductGridUiModel
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductTotalProductAndSortUiModel

class MvcLockedToProductTypeFactory : BaseAdapterTypeFactory() {
    fun type(mvcLockedToProductProductGridUiModel: MvcLockedToProductProductGridUiModel): Int {
        return MvcLockedToProductProductGridViewHolder.LAYOUT
    }

    fun type(mvcLockedToProductVoucherUiModel: MvcLockedToProductVoucherUiModel): Int {
        return MvcLockedToProductVoucherViewHolder.LAYOUT
    }

    fun type(mvcLockedToProductTotalProductAndSortUiModel: MvcLockedToProductTotalProductAndSortUiModel): Int {
        return MvcLockedToProductTotalProductAndSortViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            MvcLockedToProductVoucherViewHolder.LAYOUT -> {
                MvcLockedToProductVoucherViewHolder(view)
            }
            MvcLockedToProductTotalProductAndSortViewHolder.LAYOUT -> {
                MvcLockedToProductTotalProductAndSortViewHolder(view)
            }
            MvcLockedToProductProductGridViewHolder.LAYOUT -> {
                MvcLockedToProductProductGridViewHolder(view)
            }
            else -> {
                super.createViewHolder(view, type)
            }
        }
    }
}