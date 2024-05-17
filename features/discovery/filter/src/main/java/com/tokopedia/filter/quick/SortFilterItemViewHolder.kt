package com.tokopedia.filter.quick

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.filter.R
import com.tokopedia.filter.databinding.SortFilterQuickImageItemBinding
import com.tokopedia.filter.databinding.SortFilterQuickItemBinding
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

internal abstract class SortFilterItemViewHolder(
    itemView: View,
    private val listener: Listener
) : ViewHolder(itemView) {
    abstract fun bind(sortFilterItem: SortFilterItem)
    interface Listener {
        fun onItemClicked(sortFilterItem: SortFilterItem, position: Int)
        fun onItemChevronClicked(sortFilterItem: SortFilterItem, position: Int)
    }
}

internal class SortFilterItemImageViewHolder(
    itemView: View,
    private val listener: Listener
) : SortFilterItemViewHolder(itemView, listener) {
    private var binding: SortFilterQuickImageItemBinding? by viewBinding()

    override fun bind(sortFilterItem: SortFilterItem) {
        binding?.sortFilterQuickImage?.run {
            if (sortFilterItem.imageUrlActive.isBlank() &&
                sortFilterItem.imageUrlInactive.isBlank() ||
                !sortFilterItem.shouldShowImage) {
                return
            }
            this.loadImageRounded(
                getImageUrl(sortFilterItem),
                IMAGE_RADIUS.toPx().toFloat()
            )
            setOnClickListener {
                listener.onItemClicked(sortFilterItem, bindingAdapterPosition)
            }
        }
    }

    private fun getImageUrl(sortFilterItem: SortFilterItem) =
        if (sortFilterItem.isSelected) {
            sortFilterItem.imageUrlActive
        } else {
            sortFilterItem.imageUrlInactive
        }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.sort_filter_quick_image_item
        private const val IMAGE_RADIUS = 16
    }
}

internal class SortFilterItemCommonViewHolder(
    itemView: View,
    private val listener: Listener
) : SortFilterItemViewHolder(itemView, listener) {
    private var binding: SortFilterQuickItemBinding? by viewBinding()
    override fun bind(sortFilterItem: SortFilterItem) {
        binding?.sortFilterItemChips?.run {
            chipText = sortFilterItem.title
            chipType = getChipType(sortFilterItem)
            chip_image_icon.shouldShowWithAction(sortFilterItem.iconUrl.isNotEmpty()) {
                chip_image_icon.setImageUrl(sortFilterItem.iconUrl)
            }

            chipSize = ChipsUnify.SIZE_SMALL

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
        imageView.loadImage(imageUrl)
        addCustomView(imageView)
        chip_sub_container.setPadding(0)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.sort_filter_quick_item
    }
}
