package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.ItemComparisonBpcWidgetBinding
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.model.ComparisonBpcItemModel
import com.tokopedia.recommendation_widget_common.widget.global.RecomWidgetAnalyticListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by Frenzel
 */
class ComparisonBpcWidgetItemViewHolder(
    val view: View,
    private val listener: RecomWidgetAnalyticListener
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
            val productCardLayoutParams = productCardView.layoutParams
            productCardLayoutParams.width = element.productCardWidth
            productCardLayoutParams.height = element.productCardHeight
            productCardView.layoutParams = productCardLayoutParams

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
//                TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
//                    ProductRecommendationTracking.getClickProductTracking(
//                        recommendationItem = comparisonModel.recommendationItem,
//                        androidPageName = recommendationTrackingModel.androidPageName,
//                        headerTitle = recommendationTrackingModel.headerTitle,
//                        position = adapterPosition,
//                        isLoggedIn = userSession.isLoggedIn,
//                        anchorProductId = comparisonListModel.getAnchorProduct()?.recommendationItem?.productId.toString(),
//                        userId = userSession.userId,
//                        widgetType = ProductRecommendationTracking.COMPARISON_WIDGET
//                    )
//                )
                listener.onProductCardClicked(element.recommendationItem)
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
//                element.trackingQueue?.putEETracking(
//                    ComparisonWidgetTracking.getImpressionProductTrackingComparisonWidget(
//                        recommendationItem = comparisonModel.recommendationItem,
//                        androidPageName = recommendationTrackingModel.androidPageName,
//                        headerTitle = recommendationTrackingModel.headerTitle,
//                        position = adapterPosition,
//                        userId = userSession.userId
//                    )
//                )
                listener.onProductCardImpressed(element.recommendationItem)
            }
        }
    }
}
