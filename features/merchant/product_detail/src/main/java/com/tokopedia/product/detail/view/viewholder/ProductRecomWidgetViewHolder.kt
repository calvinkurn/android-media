package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductRecomWidgetDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselWidgetListener
import kotlinx.android.synthetic.main.item_dynamic_widget_recom.view.*

/**
 * Created by yfsx on 5/6/21.
 */
class ProductRecomWidgetViewHolder (
        private val view: View,
        private val listener: DynamicProductDetailListener)
    : AbstractViewHolder<ProductRecomWidgetDataModel>(view),
        RecommendationCarouselWidgetListener {

    companion object {
        val LAYOUT = R.layout.item_dynamic_widget_recom
    }

    override fun bind(element: ProductRecomWidgetDataModel) {
        element.recomWidgetData?.let {
            itemView.widget_recom.bind(
                    carouselData = RecommendationCarouselData(it),
                    adapterPosition = adapterPosition,
                    widgetListener = this)
        }
    }

    override fun onChannelExpired(data: RecommendationCarouselData, channelPosition: Int) {
        TODO("Not yet implemented")
    }

    override fun onSeeAllBannerClicked(data: RecommendationCarouselData, applink: String) {
        TODO("Not yet implemented")
    }

    override fun onRecomChannelImpressed(data: RecommendationCarouselData) {
        TODO("Not yet implemented")
    }

    override fun onRecomProductCardImpressed(data: RecommendationCarouselData, recomItem: RecommendationItem, itemPosition: Int, adapterPosition: Int) {
        TODO("Not yet implemented")
    }

    override fun onRecomProductCardClicked(data: RecommendationCarouselData, recomItem: RecommendationItem, applink: String, itemPosition: Int, adapterPosition: Int) {
        TODO("Not yet implemented")
    }

    override fun onRecomBannerImpressed(data: RecommendationCarouselData, adapterPosition: Int) {
        TODO("Not yet implemented")
    }

    override fun onRecomBannerClicked(data: RecommendationCarouselData, applink: String, adapterPosition: Int) {
        TODO("Not yet implemented")
    }
}