package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.highlight

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemHighlightSearchUiModel

class HighlightSearchDiffUtil(private val oldList: List<ItemHighlightSearchUiModel>,
                              private val newList: List<ItemHighlightSearchUiModel>): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title &&
                oldList[oldItemPosition].appUrl == newList[newItemPosition].appUrl
    }
}