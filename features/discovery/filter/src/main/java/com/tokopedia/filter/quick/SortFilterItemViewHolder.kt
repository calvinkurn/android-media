package com.tokopedia.filter.quick

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.filter.databinding.SortFilterQuickItemBinding
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding

internal class SortFilterItemViewHolder(
    itemView: View,
    private val listener: Listener,
): ViewHolder(itemView) {

    private var binding: SortFilterQuickItemBinding? by viewBinding()

    fun bind(sortFilterItem: SortFilterItem) {
        binding?.sortFilterItemChips?.run {
            chipText = sortFilterItem.title
            chipType = getChipType(sortFilterItem)
            chip_image_icon.shouldShowWithAction(sortFilterItem.iconUrl.isNotEmpty()) {
                chip_image_icon.setImageUrl(sortFilterItem.iconUrl)
            }

            configureChevron(sortFilterItem)

            setOnClickListener {
                if (sortFilterItem.hasChevron)
                    listener.onItemChevronClicked(sortFilterItem, bindingAdapterPosition)
                else
                    listener.onItemClicked(sortFilterItem, bindingAdapterPosition)
            }
        }
    }

    private fun ChipsUnify.configureChevron(sortFilterItem: SortFilterItem) {
        if (sortFilterItem.hasChevron)
            setChevronClickListener {
                listener.onItemChevronClicked(sortFilterItem, bindingAdapterPosition)
            }
        else
            clearRightIcon()
    }

    private fun getChipType(sortFilterItem: SortFilterItem) =
        if (sortFilterItem.isSelected) ChipsUnify.TYPE_SELECTED
        else ChipsUnify.TYPE_NORMAL

    companion object {
        @LayoutRes
        val LAYOUT = com.tokopedia.filter.R.layout.sort_filter_quick_item
    }

    interface Listener {
        fun onItemClicked(sortFilterItem: SortFilterItem, position: Int)
        fun onItemChevronClicked(sortFilterItem: SortFilterItem, position: Int)
    }
}
