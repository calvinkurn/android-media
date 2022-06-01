package com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder

import android.view.View

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductVoucherSortPlaceholderUiModel

open class MvcLockedToProductVoucherSortPlaceholderViewHolder(
        itemView: View
) : AbstractViewHolder<MvcLockedToProductVoucherSortPlaceholderUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_mvc_locked_to_product_voucher_sort_placeholder_layout
    }

    override fun bind(uiModel: MvcLockedToProductVoucherSortPlaceholderUiModel) {
    }

}