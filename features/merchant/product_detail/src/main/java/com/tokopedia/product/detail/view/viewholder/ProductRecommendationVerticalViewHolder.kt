package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationVerticalDataModel
import com.tokopedia.product.detail.databinding.ViewProductRecommendationVerticalBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel

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

            setOnClickListener {
                val trackData = listener.getRecommendationVerticalTrackData() ?: return@setOnClickListener
                listener.eventRecommendationClick(
                    item,
                    "",
                    element.position,
                    item.pageName,
                    item.header,
                    trackData
                )

                goToProduct(item.productId.toString())
//                listener.onClickRecommendationVerticalItem(
//                    item,
//                    element.position
//                )
            }

            setThreeDotsOnClickListener {
                val recommVerticalData = listener.getRecommendationVerticalTrackData() ?: return@setThreeDotsOnClickListener

                listener.onThreeDotsClick(
                    item,
                    recommVerticalData.adapterPosition,
                    element.position
                )
            }
        }

        itemView.addOnImpressionListener(element.impressHolder) {
            val trackData = listener.getRecommendationVerticalTrackData() ?: return@addOnImpressionListener
            listener.eventRecommendationImpression(
                item,
                "",
                element.position,
                item.pageName,
                item.header,
                trackData
            )
//            listener.onImpressRecommendationVerticalItem(item, element.position)
        }
    }

    private fun goToProduct(productId: String){
        RouteManager.route(itemView.context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            productId
        )
    }
}