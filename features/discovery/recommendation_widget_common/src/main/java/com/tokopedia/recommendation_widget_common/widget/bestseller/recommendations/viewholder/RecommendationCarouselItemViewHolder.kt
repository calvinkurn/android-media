package com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.layout.ProductConstraintLayout
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.recommendation_widget_common.databinding.RecommendationCarouselItemViewHolderBinding
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.adapter.RecommendationCarouselListener
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.model.RecommendationCarouselItemDataModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by Lukas on 05/11/20.
 */
class RecommendationCarouselItemViewHolder(view: View, private val listener: RecommendationCarouselListener) : AbstractViewHolder<RecommendationCarouselItemDataModel>(view) {
    private var binding: RecommendationCarouselItemViewHolderBinding? by viewBinding()

    override fun bind(element: RecommendationCarouselItemDataModel) {
        binding?.recommendationItemCard?.applyCarousel()
        binding?.recommendationItemCard?.setProductModel(element.productCardModel)
        binding?.recommendationItemCard?.setThreeDotsOnClickListener {
            listener.onThreeDotsClick(element.recommendationItem, adapterPosition)
        }
        binding?.recommendationItemCard?.setOnClickListener(object : ProductCardClickListener {
            override fun onClick(v: View) {
                listener.onProductClick(element.recommendationItem)
            }

            override fun onAreaClicked(v: View) {
                element.recommendationItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.AREA)
            }

            override fun onProductImageClicked(v: View) {
                element.recommendationItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.COVER)
            }

            override fun onSellerInfoClicked(v: View) {
                element.recommendationItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.SELLER_NAME)
            }
        })
        binding?.recommendationItemCard?.setVisibilityPercentListener(element.recommendationItem.isTopAds, object : ProductConstraintLayout.OnVisibilityPercentChanged {
            override fun onShow() {
                element.recommendationItem.sendShowAdsByteIo(itemView.context)
            }

            override fun onShowOver(maxPercentage: Int) {
                element.recommendationItem.sendShowOverAdsByteIo(itemView.context, maxPercentage)
            }
        })
        itemView.addOnImpressionListener(element) {
            listener.onProductImpression(element.recommendationItem)
        }
    }

    companion object {
        val LAYOUT = R.layout.recommendation_carousel_item_view_holder
    }
}
