package com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.databinding.ItemMvcLockedToProductSortBottomsheetLayoutBinding
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductSortUiModel
import com.tokopedia.utils.view.binding.viewBinding

open class MvcLockedToProductSortViewHolder(
        itemView: View,
        private val listener: MvcLockedToProductSortViewHolderListener
) : AbstractViewHolder<MvcLockedToProductSortUiModel>(itemView) {
    private val viewBinding: ItemMvcLockedToProductSortBottomsheetLayoutBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_mvc_locked_to_product_sort_bottomsheet_layout
    }

    interface MvcLockedToProductSortViewHolderListener{
        fun onSortItemClicked(uiModel: MvcLockedToProductSortUiModel)
    }

    override fun bind(uiModel: MvcLockedToProductSortUiModel) {
        viewBinding?.radioButtonSort?.apply {
            isChecked = false
        }
        setSortItem(uiModel)
    }

    private fun setSortItem(uiModel: MvcLockedToProductSortUiModel) {
        viewBinding?.textSortName?.text = uiModel.name
        viewBinding?.radioButtonSort?.apply {
            isChecked = uiModel.isSelected
        }
        itemView.setOnClickListener {
            listener.onSortItemClicked(uiModel)
        }
    }

}