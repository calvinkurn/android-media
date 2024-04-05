package com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.viewholder

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asAdsLogRealtimeClickModel
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asAdsLogShowModel
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asAdsLogShowOverModel
import com.tokopedia.recommendation_widget_common.databinding.RecommendationCarouselItemViewHolderBinding
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
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
                sendEventRealtimeClickAdsByteIo(itemView.context, element.recommendationItem, AdsLogConst.Refer.AREA)
            }

            override fun onProductImageClicked(v: View) {
                sendEventRealtimeClickAdsByteIo(itemView.context, element.recommendationItem, AdsLogConst.Refer.COVER)
            }

            override fun onSellerInfoClicked(v: View) {
                sendEventRealtimeClickAdsByteIo(itemView.context, element.recommendationItem, AdsLogConst.Refer.SELLER_NAME)
            }
        })

        itemView.addOnImpressionListener(element) {
            listener.onProductImpression(element.recommendationItem)
        }
    }

    override fun onViewAttachedToWindow(element: RecommendationCarouselItemDataModel?) {
        element?.recommendationItem?.let {
            if (it.isTopAds) {
                AppLogTopAds.sendEventShow(
                    itemView.context,
                    PageName.HOME,
                    element.recommendationItem.asAdsLogShowModel()
                )
            }
        }
    }

    override fun onViewDetachedFromWindow(element: RecommendationCarouselItemDataModel?, visiblePercentage: Int) {
        element?.recommendationItem?.let {
            if (it.isTopAds) {
                AppLogTopAds.sendEventShowOver(
                    itemView.context,
                    PageName.HOME,
                    element.recommendationItem.asAdsLogShowOverModel(visiblePercentage)
                )
            }
        }
    }

    private fun sendEventRealtimeClickAdsByteIo(context: Context, element: RecommendationItem, refer: String) {
        if (element.isTopAds) {
            AppLogTopAds.sendEventRealtimeClick(
                context,
                PageName.HOME,
                element.asAdsLogRealtimeClickModel(refer)
            )
        }
    }

    companion object {
        val LAYOUT = R.layout.recommendation_carousel_item_view_holder
    }
}
