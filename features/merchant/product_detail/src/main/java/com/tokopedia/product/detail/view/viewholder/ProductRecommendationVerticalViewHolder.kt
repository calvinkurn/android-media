package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationVerticalDataModel
import com.tokopedia.product.detail.databinding.ViewProductRecommendationVerticalBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class ProductRecommendationVerticalViewHolder(
    view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<ProductRecommendationVerticalDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.view_product_recommendation_vertical
    }

    private val binding = ViewProductRecommendationVerticalBinding.bind(view)

    override fun bind(element: ProductRecommendationVerticalDataModel) {
        val item = element.recommendationItem ?: return

        binding.productDetailCard.apply {
            setProductModel(
                item.toProductCardModel(hasThreeDots = true)
            )

            setOnClickListener { onClickRecommendation(item, element.position) }

            setThreeDotsOnClickListener { onClickThreeDots(item, element.position) }
        }

        itemView.addOnImpressionListener(element.impressHolder) {
            onImpressRecommendation(item, element.position)
        }
    }

    private fun onClickRecommendation(
        item: RecommendationItem,
        position: Int
    ) {
        val trackData = listener.getRecommendationVerticalTrackData() ?: return
        listener.eventRecommendationClick(
            item,
            String.EMPTY,
            position,
            item.pageName,
            item.header,
            trackData
        )

        goToProduct(item.productId.toString())
    }

    private fun onClickThreeDots(
        item: RecommendationItem,
        position: Int
    ) {
        val recommVerticalData = listener.getRecommendationVerticalTrackData() ?: return

        listener.onThreeDotsClick(
            item,
            recommVerticalData.adapterPosition,
            position
        )
    }

    private fun onImpressRecommendation(
        item: RecommendationItem,
        position: Int
    ) {
        val trackData =
            listener.getRecommendationVerticalTrackData() ?: return
        listener.eventRecommendationImpression(
            item,
            String.EMPTY,
            position,
            item.pageName,
            item.header,
            trackData
        )
    }

    private fun goToProduct(productId: String) {
        RouteManager.route(
            itemView.context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            productId
        )
    }
}
