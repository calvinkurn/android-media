package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecomWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
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

    private lateinit var componentTrackDataModel: ComponentTrackDataModel

    override fun bind(element: ProductRecomWidgetDataModel) {
        itemView.visible()
        if (element.recomWidgetData == null || element.recomWidgetData?.recommendationItemList?.isEmpty() == true) {
            itemView.widget_recom.bindTemporaryHeader(itemView.context.getString(R.string.title_other_product))
        } else {
            element.recomWidgetData?.let {
                componentTrackDataModel = getComponentTrackData(element = element)
                itemView.widget_recom.bind(
                        carouselData = RecommendationCarouselData(it, RecommendationCarouselData.STATE_READY),
                        adapterPosition = adapterPosition,
                        widgetListener = this)
            }
        }
    }

    override fun onChannelExpired(data: RecommendationCarouselData, channelPosition: Int) {
    }

    override fun onSeeAllBannerClicked(data: RecommendationCarouselData, applink: String) {
        view.context?.run {
            RouteManager.route(this, applink)
        }
    }

    override fun onRecomChannelImpressed(data: RecommendationCarouselData) {
    }

    override fun onRecomProductCardImpressed(data: RecommendationCarouselData, recomItem: RecommendationItem, itemPosition: Int, adapterPosition: Int) {
        val topAdsImageUrl = recomItem.trackerImageUrl
        if (recomItem.isTopAds) {
            listener.sendTopAdsImpression(topAdsImageUrl, recomItem.productId.toString(), recomItem.name, recomItem.imageUrl)
        }

        listener.eventRecommendationImpression(
                recomItem,
                 "",
                itemPosition,
                data.recommendationData.pageName,
                data.recommendationData.title,
                componentTrackDataModel)
    }

    override fun onRecomProductCardClicked(data: RecommendationCarouselData, recomItem: RecommendationItem, applink: String, itemPosition: Int, adapterPosition: Int) {
        val topAdsClickUrl = recomItem.clickUrl
        if (recomItem.isTopAds) {
            listener.sendTopAdsClick(topAdsClickUrl, recomItem.productId.toString(), recomItem.name, recomItem.imageUrl)
        }

        listener.eventRecommendationClick(
                recomItem,
                "",
                itemPosition,
                data.recommendationData.pageName,
                data.recommendationData.title,
                componentTrackDataModel)

        view.context?.run {
            RouteManager.route(this,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    recomItem.productId.toString())
        }
    }

    override fun onRecomBannerImpressed(data: RecommendationCarouselData, adapterPosition: Int) {
    }

    override fun onRecomBannerClicked(data: RecommendationCarouselData, applink: String, adapterPosition: Int) {
        view.context?.run {
            RouteManager.route(this, applink)
        }
    }

    private fun getComponentTrackData(element: ProductRecomWidgetDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)
}