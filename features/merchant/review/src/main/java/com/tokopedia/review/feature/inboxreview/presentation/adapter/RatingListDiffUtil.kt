package com.tokopedia.review.feature.inboxreview.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.review.feature.inboxreview.presentation.model.ListItemRatingWrapper

class RatingListDiffUtil(private val oldList: List<ListItemRatingWrapper>, private val newList: List<ListItemRatingWrapper>): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].sortValue == newList[oldItemPosition].sortValue
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].isSelected == newList[newItemPosition].isSelected &&
                oldList[oldItemPosition].sortValue == newList[newItemPosition].sortValue
    }
}