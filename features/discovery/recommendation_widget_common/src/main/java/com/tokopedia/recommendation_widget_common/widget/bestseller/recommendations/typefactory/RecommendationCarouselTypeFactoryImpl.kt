package com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.adapter.RecommendationCarouselListener
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.model.RecommendationCarouselItemDataModel
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.model.RecommendationSeeMoreDataModel
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.viewholder.RecommendationCarouselItemViewHolder
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.viewholder.RecommendationCarouselSeeMoreViewHolder

/**
 * Created by Lukas on 05/11/20.
 */
class RecommendationCarouselTypeFactoryImpl(
        private val listener: RecommendationCarouselListener
) : RecommendationCarouselTypeFactory{
    override fun type(dataModel: RecommendationSeeMoreDataModel): Int {
        return RecommendationCarouselSeeMoreViewHolder.LAYOUT
    }

    override fun type(dataModel: RecommendationCarouselItemDataModel): Int {
        return RecommendationCarouselItemViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<Visitable<RecommendationCarouselTypeFactory>> {
        return when(viewType){
            RecommendationCarouselSeeMoreViewHolder.LAYOUT -> RecommendationCarouselSeeMoreViewHolder(view, listener)
            RecommendationCarouselItemViewHolder.LAYOUT -> RecommendationCarouselItemViewHolder(view, listener)
            else -> throw Exception("The type layout not supported")
        } as AbstractViewHolder<Visitable<RecommendationCarouselTypeFactory>>
    }

}