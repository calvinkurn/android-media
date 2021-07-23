package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecomWidgetDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselWidgetListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselWidgetView

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
    private var productRecom: ProductRecomWidgetDataModel? = null

    private var componentTrackDataModel: ComponentTrackDataModel? = null
    private val recomWidget : RecommendationCarouselWidgetView = itemView.findViewById(R.id.widget_recom)

    override fun bind(element: ProductRecomWidgetDataModel) {
        productRecom = element
        itemView.visible()
        if (element.recomWidgetData == null || element.recomWidgetData?.recommendationItemList?.isEmpty() == true) {
            recomWidget.bindTemporaryHeader(itemView.context.getString(R.string.title_other_product))
        } else {
            element.recomWidgetData?.let {
                componentTrackDataModel = getComponentTrackData(element = element)
                recomWidget.bind(
                        carouselData = RecommendationCarouselData(it, RecommendationCarouselData.STATE_READY),
                        adapterPosition = adapterPosition,
                        widgetListener = this)
            }
        }
    }

    override fun onChannelExpired(data: RecommendationCarouselData, channelPosition: Int) {
    }

    override fun onSeeAllBannerClicked(data: RecommendationCarouselData, applink: String) {
        listener.goToApplink(applink)
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
                componentTrackDataModel?: ComponentTrackDataModel())
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
                componentTrackDataModel ?: ComponentTrackDataModel())

        view.context?.run {
            RouteManager.route(this,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    recomItem.productId.toString())
        }
    }

    override fun onRecomProductCardAddToCartNonVariant(data: RecommendationCarouselData, recomItem: RecommendationItem, adapterPosition: Int, quantity: Int) {

    }

    override fun onRecomProductCardAddVariantClick(data: RecommendationCarouselData, recomItem: RecommendationItem, adapterPosition: Int) {

    }

    override fun onRecomBannerImpressed(data: RecommendationCarouselData, adapterPosition: Int) {
    }

    override fun onRecomBannerClicked(data: RecommendationCarouselData, applink: String, adapterPosition: Int) {
        listener.goToApplink(applink)
    }

    override fun onChannelWidgetEmpty() {
        listener.onChannelRecommendationEmpty(adapterPosition, productRecom?.recomWidgetData)
    }

    private fun getComponentTrackData(element: ProductRecomWidgetDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)
}