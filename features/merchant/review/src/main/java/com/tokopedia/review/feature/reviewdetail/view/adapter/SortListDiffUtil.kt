package com.tokopedia.review.feature.reviewdetail.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.review.feature.reviewdetail.view.model.SortItemUiModel

class SortListDiffUtil(private val oldList: List<SortItemUiModel>, private val newList: List<SortItemUiModel>): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[oldItemPosition].title
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].isSelected == newList[newItemPosition].isSelected
    }
}