package com.tokopedia.recommendation_widget_common.widget.vertical

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.trackingoptimizer.TrackingQueue

class RecommendationVerticalTypeFactoryImpl(
    private val trackingQueue: TrackingQueue
) : BaseAdapterTypeFactory(), RecommendationVerticalTypeFactory {
    override fun type(recommendationVerticalProductCardModel: RecommendationVerticalProductCardModel): Int {
        return RecommendationVerticalProductCardViewHolder.LAYOUT
    }

    override fun type(recommendationVerticalProductCardModel: RecommendationVerticalSeeMoreModel): Int {
        return RecommendationVerticalSeeMoreViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            RecommendationVerticalProductCardViewHolder.LAYOUT -> {
                RecommendationVerticalProductCardViewHolder(itemView = parent, trackingQueue)
            }
            RecommendationVerticalSeeMoreViewHolder.LAYOUT -> {
                RecommendationVerticalSeeMoreViewHolder(itemView = parent)
            }
            else -> super.createViewHolder(parent, type)
        }
    }
}
