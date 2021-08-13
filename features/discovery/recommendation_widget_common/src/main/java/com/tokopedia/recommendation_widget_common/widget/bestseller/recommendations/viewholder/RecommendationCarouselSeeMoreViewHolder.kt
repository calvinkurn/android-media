package com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.adapter.RecommendationCarouselListener
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.model.RecommendationSeeMoreDataModel

/**
 * Created by Lukas on 05/11/20.
 */
class RecommendationCarouselSeeMoreViewHolder(view: View, private val listener: RecommendationCarouselListener) : AbstractViewHolder<RecommendationSeeMoreDataModel>(view){
    override fun bind(element: RecommendationSeeMoreDataModel) {
        itemView.setOnClickListener {
            listener.onSeeMoreCardClick(element.applink)
        }
    }
    companion object{
        val LAYOUT = R.layout.recommendation_carousel_see_more_view_holder
    }
}