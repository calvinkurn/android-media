package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.layout.ProductConstraintLayout
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
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
            productCardView.setOnClickListener(object: ProductCardClickListener {
                override fun onClick(v: View) {
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

                override fun onAreaClicked(v: View) {
                    element.recommendationItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.AREA)
                }

                override fun onProductImageClicked(v: View) {
                    element.recommendationItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.COVER)
                }

                override fun onSellerInfoClicked(v: View) {
                    element.recommendationItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.SELLER_NAME)
                }
            })
            productCardView.setVisibilityPercentListener(element.recommendationItem.isTopAds, object : ProductConstraintLayout.OnVisibilityPercentChanged {
                override fun onShow() {
                    element.recommendationItem.sendShowAdsByteIo(itemView.context)
                }

                override fun onShowOver(maxPercentage: Int) {
                    element.recommendationItem.sendShowOverAdsByteIo(itemView.context, maxPercentage)
                }
            })
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
