package com.tokopedia.filter.quick

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.tokopedia.filter.R
import com.tokopedia.filter.databinding.SortFilterQuickItemBinding
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding

internal class SortFilterItemViewHolder(
    itemView: View,
    private val listener: Listener
) : ViewHolder(itemView) {

    private var binding: SortFilterQuickItemBinding? by viewBinding()

    fun bind(sortFilterItem: SortFilterItem) {
        binding?.sortFilterItemChips?.run {
            chipText = sortFilterItem.title
            chipType = getChipType(sortFilterItem)
            chip_image_icon.shouldShowWithAction(sortFilterItem.iconUrl.isNotEmpty()) {
                chip_image_icon.setImageUrl(sortFilterItem.iconUrl)
            }

            chipSize = ChipsUnify.SIZE_MEDIUM

            val imageUrl: String = if (sortFilterItem.isSelected) {
                sortFilterItem.imageUrlActive
            } else {
                sortFilterItem.imageUrlInactive
            }

            if (sortFilterItem.shouldShowImage && imageUrl.isNotBlank()) {
                setupShowImageFilter(imageUrl)
            }

            configureChevron(sortFilterItem)

            setOnClickListener {
                if (sortFilterItem.hasChevron) {
                    listener.onItemChevronClicked(sortFilterItem, bindingAdapterPosition)
                } else {
                    listener.onItemClicked(sortFilterItem, bindingAdapterPosition)
                }
            }
        }
    }

    private fun ChipsUnify.configureChevron(sortFilterItem: SortFilterItem) {
        if (sortFilterItem.hasChevron) {
            setChevronClickListener {
                listener.onItemChevronClicked(sortFilterItem, bindingAdapterPosition)
            }
        } else {
            clearRightIcon()
        }
    }

    private fun getChipType(sortFilterItem: SortFilterItem) =
        if (sortFilterItem.isSelected) {
            ChipsUnify.TYPE_SELECTED
        } else {
            ChipsUnify.TYPE_NORMAL
        }

    private fun ChipsUnify.setupShowImageFilter(imageUrl: String) {
        val imageView = ImageView(context)
        imageView.adjustViewBounds = true
        Glide.with(context)
            .load(imageUrl)
            .into(imageView)
        addCustomView(imageView)
        chip_sub_container.setPadding(0)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.sort_filter_quick_item
    }

    interface Listener {
        fun onItemClicked(sortFilterItem: SortFilterItem, position: Int)
        fun onItemChevronClicked(sortFilterItem: SortFilterItem, position: Int)
    }
}
