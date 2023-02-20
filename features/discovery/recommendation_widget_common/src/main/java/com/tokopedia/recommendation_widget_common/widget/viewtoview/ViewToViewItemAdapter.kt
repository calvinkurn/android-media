package com.tokopedia.recommendation_widget_common.widget.viewtoview

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselDiffUtilCallback

class ViewToViewItemAdapter(
    typeFactory: ViewToViewItemTypeFactory,
) : BaseAdapter<ViewToViewItemTypeFactory>(typeFactory) {

    val data: List<Visitable<*>>
        get() = visitables

    fun submitList(data: List<Visitable<*>>) {
        val diffUtilCallback = RecommendationCarouselDiffUtilCallback(visitables, data)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        result.dispatchUpdatesTo(this)
        visitables.clear()
        visitables.addAll(data)
    }

    override fun onViewRecycled(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }
}
