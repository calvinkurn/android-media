package com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder

import android.view.View

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.databinding.ItemMvcLockedToProductTotalProductAndSortLayoutBinding
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductSortListFactory
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductTotalProductAndSortUiModel
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding

open class MvcLockedToProductTotalProductAndSortViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<MvcLockedToProductTotalProductAndSortUiModel>(itemView) {
    private val viewBinding: ItemMvcLockedToProductTotalProductAndSortLayoutBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_mvc_locked_to_product_total_product_and_sort_layout
    }

    interface Listener {
        fun onSortChipClicked()
    }

    override fun bind(uiModel: MvcLockedToProductTotalProductAndSortUiModel) {
        setTotalProduct(uiModel)
        setSortChipData(uiModel)
    }

    private fun setSortChipData(uiModel: MvcLockedToProductTotalProductAndSortUiModel) {
        viewBinding?.chipSort?.apply {
            chipText = uiModel.selectedSortData.name
            chipType = getChipType(uiModel.selectedSortData.name)
            setOnClickListener {
                listener.onSortChipClicked()
            }
            setChevronClickListener {
                listener.onSortChipClicked()
            }
        }
    }

    private fun getChipType(selectedChipName: String): String {
        return if (MvcLockedToProductSortListFactory.isDefaultSortName(selectedChipName)) {
            ChipsUnify.TYPE_NORMAL
        } else {
            ChipsUnify.TYPE_SELECTED
        }
    }

    private fun setTotalProduct(uiModel: MvcLockedToProductTotalProductAndSortUiModel) {
        viewBinding?.textTotalProduct?.text = uiModel.totalProductWording
    }

}