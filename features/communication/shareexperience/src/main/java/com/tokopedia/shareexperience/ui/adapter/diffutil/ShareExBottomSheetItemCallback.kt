package com.tokopedia.shareexperience.ui.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory
import com.tokopedia.shareexperience.ui.model.image.ShareExImageCarouselUiModel

class ShareExBottomSheetItemCallback(
    private val oldList: List<Visitable<in ShareExTypeFactory>>,
    private val newList: List<Visitable<in ShareExTypeFactory>>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return when {
            (oldItem is ShareExImageCarouselUiModel && newItem is ShareExImageCarouselUiModel) -> {
                compareImageCarousel(oldItem, newItem)
            }
            else -> oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return when {
            (oldItem is ShareExImageCarouselUiModel && newItem is ShareExImageCarouselUiModel) -> {
                compareImageCarousel(oldItem, newItem)
            }
            else -> oldItem == newItem
        }
    }

    private fun compareImageCarousel(
        oldItem: ShareExImageCarouselUiModel,
        newItem: ShareExImageCarouselUiModel
    ): Boolean {
        return oldItem.imageUrlList.map { it.imageUrl } == newItem.imageUrlList.map { it.imageUrl }
    }
}
