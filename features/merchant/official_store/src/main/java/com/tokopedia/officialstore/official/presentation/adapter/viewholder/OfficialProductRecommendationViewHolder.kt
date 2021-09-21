package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.common.OfficialStoreConstant
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.ProductRecommendationDataModel
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

class OfficialProductRecommendationViewHolder(
        view: View
): AbstractViewHolder<ProductRecommendationDataModel>(view) {

    private val productCardView: ProductCardGridView? by lazy { view.findViewById<ProductCardGridView>(R.id.product_item) }

    override fun bind(element: ProductRecommendationDataModel) {
        productCardView?.run {
            setProductModel(element.productItem.toProductCardModel(hasThreeDots = true))

            setImageProductViewHintListener(element.productItem, object : ViewHintListener {
                override fun onViewHint() {
                    if (element.productItem.isTopAds) {
                        context?.run {
                            TopAdsUrlHitter(context).hitImpressionUrl(
                                    className,
                                    element.productItem.trackerImageUrl,
                                    element.productItem.productId.toString(),
                                    element.productItem.name,
                                    element.productItem.imageUrl,
                                    OfficialStoreConstant.TopAdsComponent.OS_RECOM_TOP_ADS
                            )
                        }
                    }
                    element.listener.onProductImpression(element.productItem)
                }
            })

            setOnClickListener {
                if (element.productItem.isTopAds) {
                    context?.run {
                        TopAdsUrlHitter(context).hitClickUrl(
                                className,
                                element.productItem.clickUrl,
                                element.productItem.productId.toString(),
                                element.productItem.name,
                                element.productItem.imageUrl,
                                OfficialStoreConstant.TopAdsComponent.OS_RECOM_TOP_ADS
                        )
                    }
                }
                element.listener.onProductClick(element.productItem, element.productItem.type, adapterPosition)
            }

            setThreeDotsOnClickListener {
                element.listener.onThreeDotsClick(element.productItem, adapterPosition)
            }
        }
    }

    override fun bind(element: ProductRecommendationDataModel, payloads: List<Any>) {
        if (payloads.getOrNull(0) !is Boolean) return

        productCardView?.setThreeDotsOnClickListener {
            element.listener.onThreeDotsClick(element.productItem, adapterPosition)
        }
    }

    companion object {
        val LAYOUT = R.layout.viewmodel_product_recommendation_item
        private const val className: String = "com.tokopedia.officialstore.official.presentation.adapter.viewholder.OfficialProductRecommendationViewHolder"
    }

}