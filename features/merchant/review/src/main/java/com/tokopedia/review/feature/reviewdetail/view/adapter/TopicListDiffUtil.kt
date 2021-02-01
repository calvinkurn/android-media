package com.tokopedia.review.feature.reviewdetail.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.review.feature.reviewdetail.view.model.SortFilterItemWrapper
import com.tokopedia.review.feature.reviewdetail.view.model.SortItemUiModel

class TopicListDiffUtil(private val oldList: List<SortFilterItemWrapper>, private val newList: List<SortFilterItemWrapper>): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].titleUnformated == newList[oldItemPosition].titleUnformated
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].isSelected == newList[newItemPosition].isSelected
    }
}