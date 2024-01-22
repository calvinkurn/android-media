package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.experiments.ProductCardExperiment
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.ItemComparisonBpcWidgetBinding
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.model.ComparisonBpcItemModel
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.tracking.ComparisonBpcAnalyticListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by Frenzel
 */
class ComparisonBpcWidgetItemViewHolder(
    val view: View,
    private val listener: ComparisonBpcAnalyticListener
) : AbstractViewHolder<ComparisonBpcItemModel>(view) {

    private var binding: ItemComparisonBpcWidgetBinding? by viewBinding()

    companion object {
        private const val CLASS_NAME = "com.tokopedia.recommendation_widget_common.widget.comparison_bpc.ComparisonBeautyWidgetItemViewHolder.kt"
        val LAYOUT = R.layout.item_comparison_bpc_widget
    }
    val context: Context = view.context

    override fun bind(element: ComparisonBpcItemModel) {
        binding?.run {
            productCardView.applyCarousel()
            setLayoutParams(element)

            bpcSpecsView.setSpecsInfo(element.specsModel)
            productCardView.setProductModel(element.productCardModel)
            productCardView.setOnClickListener {
                if (element.recommendationItem.isTopAds) {
                    val product = element.recommendationItem
                    TopAdsUrlHitter(context).hitClickUrl(
                        CLASS_NAME,
                        product.clickUrl,
                        product.productId.toString(),
                        product.name,
                        product.imageUrl
                    )
                }
                listener.onProductCardClicked(element.recommendationItem, element.trackingModel, element.anchorProductId)
            }
            productCardView.addOnImpressionListener(element) {
                if (element.recommendationItem.isTopAds) {
                    val product = element.recommendationItem
                    TopAdsUrlHitter(context).hitImpressionUrl(
                        CLASS_NAME,
                        product.trackerImageUrl,
                        product.productId.toString(),
                        product.name,
                        product.imageUrl
                    )
                }
                listener.onProductCardImpressed(element.recommendationItem, element.trackingModel, element.anchorProductId, element.widgetTitle)
            }
        }
    }

    private fun setLayoutParams(element: ComparisonBpcItemModel) {
        val productCardLayoutParams = binding?.productCardView?.layoutParams
        productCardLayoutParams?.height = element.productCardHeight
        binding?.productCardView?.layoutParams = productCardLayoutParams
    }
}
