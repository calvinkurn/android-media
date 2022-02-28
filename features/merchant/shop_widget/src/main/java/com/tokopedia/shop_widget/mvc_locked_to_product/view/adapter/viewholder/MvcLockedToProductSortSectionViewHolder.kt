package com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder

import android.view.View

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.databinding.ItemMvcLockedToProductSortSectionLayoutBinding
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductSortListFactory
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductSortSectionUiModel
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding

open class MvcLockedToProductSortSectionViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<MvcLockedToProductSortSectionUiModel>(itemView) {
    private val viewBinding: ItemMvcLockedToProductSortSectionLayoutBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_mvc_locked_to_product_sort_section_layout
    }

    interface Listener {
        fun onSortChipClicked()
    }

    override fun bind(uiModel: MvcLockedToProductSortSectionUiModel) {
        setSortChipData(uiModel)
    }

    private fun setSortChipData(uiModel: MvcLockedToProductSortSectionUiModel) {
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

}