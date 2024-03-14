package com.tokopedia.recommendation_widget_common.widget.comparison.compareditem

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.recommendation_widget_common.databinding.ItemComparisonComparedWidgetBinding
import com.tokopedia.recommendation_widget_common.widget.comparison.tracking.ComparisonWidgetTracking
import com.tokopedia.recommendation_widget_common.widget.ProductRecommendationTracking
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonListModel
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonModel
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonViewHolder
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonWidgetInterface
import com.tokopedia.recommendation_widget_common.widget.comparison.RecommendationTrackingModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding

class ComparisonWidgetComparedItemViewHolder(
    val view: View
): RecyclerView.ViewHolder(view), ComparisonViewHolder {

    private var binding: ItemComparisonComparedWidgetBinding? by viewBinding()

    companion object {
        private const val CLASS_NAME = "com.tokopedia.recommendation_widget_common.widget.comparison.caompareditem.ComparisonWidgetComparedItemViewHolder.kt"
    }
    val context: Context = view.context

    override fun bind(
        comparisonModel: ComparisonModel,
        comparisonListModel: ComparisonListModel,
        comparisonWidgetInterface: ComparisonWidgetInterface,
        recommendationTrackingModel: RecommendationTrackingModel,
        trackingQueue: TrackingQueue?,
        userSession: UserSessionInterface,
    ) {

        binding?.specsView?.setSpecsInfo(comparisonModel.specsModel)
        binding?.productCardView?.setProductModel(comparisonModel.productCardModel)
        if (comparisonModel.isClickable) {
            binding?.productCardView?.setOnClickListener {
                if (comparisonModel.recommendationItem.isTopAds) {
                    val product = comparisonModel.recommendationItem
                    com.tokopedia.topads.sdk.utils.TopAdsUrlHitter(context).hitClickUrl(
                        CLASS_NAME,
                        product.clickUrl,
                        product.productId.toString(),
                        product.name,
                        product.imageUrl
                    )
                }
                TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                    ProductRecommendationTracking.getClickProductTracking(
                        recommendationItem = comparisonModel.recommendationItem,
                        androidPageName = recommendationTrackingModel.androidPageName,
                        headerTitle = recommendationTrackingModel.headerTitle,
                        position = adapterPosition,
                        isLoggedIn = userSession.isLoggedIn,
                        anchorProductId = comparisonListModel.getAnchorProduct()?.recommendationItem?.productId.toString(),
                        userId = userSession.userId,
                        widgetType = ProductRecommendationTracking.COMPARISON_WIDGET
                    )
                )
                comparisonWidgetInterface.onProductCardClicked(comparisonModel.recommendationItem, comparisonListModel, adapterPosition)
            }
        }
        binding?.productCardView?.addOnImpressionListener(comparisonModel) {
            if (comparisonModel.recommendationItem.isTopAds) {
                val product = comparisonModel.recommendationItem
                com.tokopedia.topads.sdk.utils.TopAdsUrlHitter(context).hitImpressionUrl(
                    CLASS_NAME,
                    product.trackerImageUrl,
                    product.productId.toString(),
                    product.name,
                    product.imageUrl
                )
            }
            trackingQueue?.putEETracking(
                ComparisonWidgetTracking.getImpressionProductTrackingComparisonWidget(
                    recommendationItem = comparisonModel.recommendationItem,
                    androidPageName = recommendationTrackingModel.androidPageName,
                    headerTitle = recommendationTrackingModel.headerTitle,
                    position = adapterPosition,
                    userId = userSession.userId
                )
            )
            comparisonWidgetInterface.onProductCardImpressed(comparisonModel.recommendationItem, comparisonListModel, adapterPosition)
        }
    }
}
