package com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.adapter.RecommendationCarouselListener
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.model.RecommendationSeeMoreDataModel
import com.tokopedia.unifycomponents.CardUnify2

/**
 * Created by Lukas on 05/11/20.
 */
class RecommendationCarouselSeeMoreViewHolder(
    view: View,
    private val listener: RecommendationCarouselListener,
    private val cardInteraction: Boolean = false
) : AbstractViewHolder<RecommendationSeeMoreDataModel>(view){
    override fun bind(element: RecommendationSeeMoreDataModel) {
        itemView.findViewById<CardUnify2>(R.id.recommendation_carousel_see_more_card).apply {
            animateOnPress = if(cardInteraction) CardUnify2.ANIMATE_OVERLAY_BOUNCE else CardUnify2.ANIMATE_OVERLAY
        }
        itemView.setOnClickListener {
            listener.onSeeMoreCardClick(element.applink)
        }
    }
    companion object{
        val LAYOUT = R.layout.recommendation_carousel_see_more_view_holder
    }
}