package com.tokopedia.recommendation_widget_common.widget.carousel

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.CommonRecomCarouselCardTypeFactory

/**
 * Created by yfsx on 5/3/21.
 */
class RecommendationCarouselAdapter (
        typeFactory: CommonRecomCarouselCardTypeFactory,
) : BaseAdapter<CommonRecomCarouselCardTypeFactory>(typeFactory){

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