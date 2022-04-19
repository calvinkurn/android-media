package com.tokopedia.recommendation_widget_common.widget.carousel

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.recommendation_widget_common.widget.productcard.common.RecomCarouselDiffUtilComparable

class RecommendationCarouselDiffUtilCallback(
        private val oldList: List<Any>,
        private val newList: List<Any>,
): DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition !in oldList.indices) return false
        if (newItemPosition !in newList.indices) return false

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        if (oldItem !is RecomCarouselDiffUtilComparable
                || newItem !is RecomCarouselDiffUtilComparable) return false

        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition !in oldList.indices) return false
        if (newItemPosition !in newList.indices) return false

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        if (oldItem !is RecomCarouselDiffUtilComparable
                || newItem !is RecomCarouselDiffUtilComparable) return false

        return oldItem.areContentsTheSame(newItem)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        if (oldItem !is RecomCarouselDiffUtilComparable
            || newItem !is RecomCarouselDiffUtilComparable) return null

        return oldItem.getChangePayload(newItem)
    }
}