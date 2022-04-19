package com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder.*
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.*

class MvcLockedToProductTypeFactory(
    private val globalErrorViewListener: MvcLockedToProductGlobalErrorViewHolder.Listener,
    private val sortSectionListener: MvcLockedToProductSortSectionViewHolder.Listener,
    private val productCardListener: MvcLockedToProductGridViewHolder.Listener
) : BaseAdapterTypeFactory() {
    fun type(uiModel: MvcLockedToProductGridProductUiModel): Int {
        return MvcLockedToProductGridViewHolder.LAYOUT
    }

    fun type(uiModel: MvcLockedToProductVoucherUiModel): Int {
        return MvcLockedToProductVoucherViewHolder.LAYOUT
    }

    fun type(uiModel: MvcLockedToProductSortSectionUiModel): Int {
        return MvcLockedToProductSortSectionViewHolder.LAYOUT
    }

    fun type(uiModel: MvcLockedToProductVoucherSortPlaceholderUiModel): Int {
        return MvcLockedToProductVoucherSortPlaceholderViewHolder.LAYOUT
    }

    fun type(uiModel: MvcLockedToProductGridListPlaceholderUiModel): Int {
        return MvcLockedToProductGridListPlaceholderViewHolder.LAYOUT
    }

    fun type(uiModel: MvcLockedToProductGlobalErrorUiModel): Int {
        return MvcLockedToProductGlobalErrorViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            MvcLockedToProductVoucherViewHolder.LAYOUT -> {
                MvcLockedToProductVoucherViewHolder(view)
            }
            MvcLockedToProductSortSectionViewHolder.LAYOUT -> {
                MvcLockedToProductSortSectionViewHolder(view, sortSectionListener)
            }
            MvcLockedToProductGridViewHolder.LAYOUT -> {
                MvcLockedToProductGridViewHolder(view, productCardListener)
            }
            MvcLockedToProductVoucherSortPlaceholderViewHolder.LAYOUT -> {
                MvcLockedToProductVoucherSortPlaceholderViewHolder(view)
            }
            MvcLockedToProductGridListPlaceholderViewHolder.LAYOUT -> {
                MvcLockedToProductGridListPlaceholderViewHolder(view)
            }
            MvcLockedToProductGlobalErrorViewHolder.LAYOUT -> {
                MvcLockedToProductGlobalErrorViewHolder(view, globalErrorViewListener)
            }
            else -> {
                super.createViewHolder(view, type)
            }
        }
    }
}