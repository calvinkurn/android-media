package com.tokopedia.recommendation_widget_common.widget.vertical

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

class RecommendationVerticalAdapter(
    typeFactory: RecommendationVerticalTypeFactory
) : BaseAdapter<RecommendationVerticalTypeFactory>(typeFactory) {

    @Suppress("UNCHECKED_CAST")
    fun submitList(newList: List<RecommendationVerticalVisitable>) {
        val diffCallback = RecommendationVerticalDiffUtilCallback(
            oldList = visitables.toMutableList() as List<RecommendationVerticalVisitable>,
            newList = newList
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onViewRecycled(holder: AbstractViewHolder<out Visitable<*>>) {
        holder.onViewRecycled()
    }
}
