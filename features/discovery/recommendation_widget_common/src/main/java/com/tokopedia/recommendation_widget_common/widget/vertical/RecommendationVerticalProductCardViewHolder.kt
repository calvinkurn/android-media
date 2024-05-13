package com.tokopedia.recommendation_widget_common.widget.vertical

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.layout.ProductConstraintLayout
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.ItemRecomVerticalProductcardBinding
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asProductTrackModel
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue

class RecommendationVerticalProductCardViewHolder(
    itemView: View,
    private val trackingQueue: TrackingQueue
) : AbstractViewHolder<RecommendationVerticalProductCardModel>(itemView) {
    companion object {
        val LAYOUT = R.layout.item_recom_vertical_productcard
        private const val CLASS_NAME = "com.tokopedia.recommendation_widget_common.widget.vertical.RecommendationVerticalProductCardViewHolder"
    }

    private val binding = ItemRecomVerticalProductcardBinding.bind(itemView)

    override fun bind(element: RecommendationVerticalProductCardModel) {
        setupProductCardLayoutData(element.productModel)
        setupProductCardListener(element)
    }

    override fun bind(element: RecommendationVerticalProductCardModel, payloads: MutableList<Any>) {
        val payload = payloads.firstOrNull().takeIf { it is Map<*, *> } as? Map<*, *>
        if (payload.isNullOrEmpty()) {
            bind(element)
        } else {
            if (payload.shouldUpdateProductCard()) {
                setupProductCardLayoutData(element.productModel)
            }
            if (payload.shouldUpdateListeners()) {
                setupProductCardListener(element)
            }
        }
    }

    override fun onViewRecycled() {
        binding.productCardView.recycle()
    }

    private fun setupProductCardLayoutData(productModel: ProductCardModel) {
        binding.productCardView.setProductModel(productModel)
    }

    private fun setupProductCardListener(element: RecommendationVerticalProductCardModel) {
        with(binding.productCardView) {
            addOnImpressionListener(element.recomItem) { onProductCardImpressed(element) }
            addOnImpression1pxListener(element.recomItem.appLogImpressHolder) { onProductCardImpressed1px(element) }
            setVisibilityPercentListener(element.recomItem.isTopAds, object : ProductConstraintLayout.OnVisibilityPercentChanged {
                override fun onShow() {
                    element.recomItem.sendShowAdsByteIo(itemView.context)
                }

                override fun onShowOver(maxPercentage: Int) {
                    element.recomItem.sendShowOverAdsByteIo(itemView.context, maxPercentage)
                }
            })
            setOnClickListener(object: ProductCardClickListener {
                override fun onClick(v: View) {
                    onProductClicked(element)
                }

                override fun onAreaClicked(v: View) {
                    element.recomItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.AREA)
                }

                override fun onProductImageClicked(v: View) {
                    element.recomItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.COVER)
                }

                override fun onSellerInfoClicked(v: View) {
                    element.recomItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.SELLER_NAME)
                }
            })
        }
    }

    private fun onProductCardImpressed(element: RecommendationVerticalProductCardModel) {
        if (element.recomItem.isTopAds) {
            TopAdsUrlHitter(binding.productCardView.context).hitImpressionUrl(
                CLASS_NAME,
                element.recomItem.trackerImageUrl,
                element.recomItem.productId.toString(),
                element.recomItem.name,
                element.recomItem.imageUrl,
                element.componentName
            )
        }

        element.widgetTracking?.sendEventItemImpression(
            trackingQueue = trackingQueue,
            item = element.recomItem
        )
    }

    private fun onProductCardImpressed1px(element: RecommendationVerticalProductCardModel) {
        AppLogRecommendation.sendProductShowAppLog(
            element.recomItem.asProductTrackModel(entranceForm = EntranceForm.PURE_GOODS_CARD)
        )

    }

    private fun onProductClicked(element: RecommendationVerticalProductCardModel) {
        val productRecommendation = element
            .recomWidget
            .recommendationItemList
            .getOrNull(bindingAdapterPosition) ?: return

        if (element.recomItem.isTopAds) {
            TopAdsUrlHitter(binding.productCardView.context).hitClickUrl(
                CLASS_NAME,
                productRecommendation.clickUrl,
                productRecommendation.productId.toString(),
                productRecommendation.name,
                productRecommendation.imageUrl
            )
        }

        element.widgetTracking?.sendEventItemClick(
            item = element.recomItem
        )

        AppLogRecommendation.sendProductClickAppLog(
            element.recomItem.asProductTrackModel(entranceForm = EntranceForm.PURE_GOODS_CARD)
        )

        RouteManager.route(binding.productCardView.context, productRecommendation.appUrl)
    }

    private fun Map<*, *>.shouldUpdateProductCard(): Boolean {
        return containsKey(RecommendationVerticalProductCardModel.PAYLOAD_FLAG_SHOULD_UPDATE_PRODUCT_CARD)
    }

    private fun Map<*, *>.shouldUpdateListeners(): Boolean {
        return containsKey(RecommendationVerticalProductCardModel.PAYLOAD_FLAG_SHOULD_UPDATE_LISTENERS)
    }
}
