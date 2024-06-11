package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecomWidgetDataModel
import com.tokopedia.product.detail.view.listener.ProductDetailListener
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecomCarouselWidgetBasicListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselTokonowListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselWidgetView

/**
 * Created by yfsx on 5/6/21.
 */
class ProductRecomWidgetViewHolder(
    private val view: View,
    private val listener: ProductDetailListener
) : AbstractViewHolder<ProductRecomWidgetDataModel>(view),
    RecomCarouselWidgetBasicListener, RecommendationCarouselTokonowListener, RecomCarouselWidgetBasicListener.OnAdsItemClickListener,
    RecomCarouselWidgetBasicListener.OnAdsViewListener {

    companion object {
        val LAYOUT = R.layout.item_dynamic_widget_recom
    }

    private var productRecom: ProductRecomWidgetDataModel? = null

    private val recomWidget: RecommendationCarouselWidgetView =
        itemView.findViewById(R.id.widget_recom)

    override fun bind(element: ProductRecomWidgetDataModel) {
        productRecom = element
        itemView.visible()
        if (element.recomWidgetData == null || element.recomWidgetData?.recommendationItemList?.isEmpty() == true) {
            recomWidget.bindTemporaryHeader(itemView.context.getString(R.string.title_other_product))
        } else {
            element.recomWidgetData?.let {
                recomWidget.bind(
                    carouselData = RecommendationCarouselData(
                        recommendationData = it,
                        state = RecommendationCarouselData.STATE_READY,
                        appLogAdditionalParam = listener.getAppLogAdditionalParam()
                    ),
                    adapterPosition = adapterPosition,
                    basicListener = this,
                    tokonowListener = this,
                    adsViewListener = this,
                    adsItemClickListener = this
                )
            }
        }
    }

    override fun onChannelExpired(data: RecommendationCarouselData, channelPosition: Int) {
    }

    override fun onSeeAllBannerClicked(data: RecommendationCarouselData, applink: String) {
        listener.onSeeAllRecomClicked(data.recommendationData, data.recommendationData.pageName, applink, getComponentTrackData(productRecom))
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
                getComponentTrackData(productRecom))
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
                getComponentTrackData(productRecom))

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
        listener.onRecommendationBannerImpressed(
                data = data.recommendationData,
                templateNameType = productRecom?.name ?: "",
        )
    }

    override fun onRecomBannerClicked(data: RecommendationCarouselData, applink: String, adapterPosition: Int) {
        listener.onRecommendationBannerClicked(
                appLink = applink,
                data = data.recommendationData,
                templateNameType = productRecom?.name ?: "",
        )
    }

    override fun onChannelWidgetEmpty() {
        listener.onChannelRecommendationEmpty(adapterPosition, productRecom?.recomWidgetData)
    }

    override fun onAreaClicked(recomItem: RecommendationItem, bindingAdapterPosition: Int) {
        recomItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.AREA)
    }

    override fun onSellerInfoClicked(recomItem: RecommendationItem, bindingAdapterPosition: Int) {
        recomItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.SELLER_NAME)
    }

    override fun onProductImageClicked(recomItem: RecommendationItem, bindingAdapterPosition: Int) {
        recomItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.COVER)
    }

    override fun onViewAttachedToWindow(recomItem: RecommendationItem, bindingAdapterPosition: Int) {
        recomItem.sendShowAdsByteIo(itemView.context)
    }

    override fun onViewDetachedFromWindow(recomItem: RecommendationItem, bindingAdapterPosition: Int, visiblePercentage: Int) {
        recomItem.sendShowOverAdsByteIo(itemView.context, visiblePercentage)
    }

    override fun onWidgetFail(pageName: String, e: Throwable) {
    }

    override fun onShowError(pageName: String, e: Throwable) {
    }

    private fun getComponentTrackData(element: ProductRecomWidgetDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)
}
