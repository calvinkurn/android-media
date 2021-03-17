package com.tokopedia.seller.search.feature.suggestion.view.viewholder.hightlights

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights.ItemHighlightSuggestionSearchUiModel

class HighlightSuggestionSearchDiffUtil(private val oldList: List<ItemHighlightSuggestionSearchUiModel>,
                                        private val newList: List<ItemHighlightSuggestionSearchUiModel>) : DiffUtil.Callback() {

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