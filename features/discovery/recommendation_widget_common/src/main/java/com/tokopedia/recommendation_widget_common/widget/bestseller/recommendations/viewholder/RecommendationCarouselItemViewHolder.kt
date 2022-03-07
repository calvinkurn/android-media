package com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.RecommendationCarouselItemViewHolderBinding
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.adapter.RecommendationCarouselListener
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.model.RecommendationCarouselItemDataModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by Lukas on 05/11/20.
 */
class RecommendationCarouselItemViewHolder(view: View, private val listener: RecommendationCarouselListener) : AbstractViewHolder<RecommendationCarouselItemDataModel>(view){
    private var binding: RecommendationCarouselItemViewHolderBinding? by viewBinding()
    override fun bind(element: RecommendationCarouselItemDataModel) {
        binding?.recommendationItemCard?.applyCarousel()
        binding?.recommendationItemCard?.setProductModel(element.productCardModel)
        binding?.recommendationItemCard?.setThreeDotsOnClickListener {
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