package com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.adapter.RecommendationCarouselListener
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.model.RecommendationCarouselItemDataModel
import kotlinx.android.synthetic.main.recommendation_carousel_item_view_holder.view.*

/**
 * Created by Lukas on 05/11/20.
 */
class RecommendationCarouselItemViewHolder(view: View, private val listener: RecommendationCarouselListener) : AbstractViewHolder<RecommendationCarouselItemDataModel>(view){
    override fun bind(element: RecommendationCarouselItemDataModel) {
        itemView.recommendation_item_card.applyCarousel()
        itemView.recommendation_item_card.setProductModel(element.productCardModel)
        itemView.recommendation_item_card.setThreeDotsOnClickListener {
            listener.onThreeDotsClick(element.recommendationItem, adapterPosition)
        }
        itemView.setOnClickListener {
            listener.onProductClick(element.recommendationItem)
        }
        itemView.addOnImpressionListener(element) {
            listener.onProductImpression(element.recommendationItem)
        }
    }

    companion object{
        val LAYOUT = R.layout.recommendation_carousel_item_view_holder
    }
}