package com.tokopedia.recommendation_widget_common.widget.comparison

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.recommendation_widget_common.databinding.ItemComparisonWidgetBinding
import com.tokopedia.recommendation_widget_common.widget.ProductRecommendationTracking
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding

class ComparisonWidgetItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    private var binding: ItemComparisonWidgetBinding? by viewBinding()

    companion object {
        private const val CLASS_NAME = "com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonWidgetItemViewHolder.kt"
    }
    val context: Context = view.context

    fun bind(
            comparisonModel: ComparisonModel,
            comparisonListModel: ComparisonListModel,
            comparisonWidgetInterface: ComparisonWidgetInterface,
            recommendationTrackingModel: RecommendationTrackingModel,
            trackingQueue: TrackingQueue?,
            userSession: UserSessionInterface
    ) {
        binding?.specsView?.setSpecsInfo(comparisonModel.specsModel)
        binding?.productCardView?.setProductModel(comparisonModel.productCardModel)
        if (!comparisonModel.isCurrentItem) {
            binding?.productCardView?.setOnClickListener {
                if (comparisonModel.recommendationItem.isTopAds) {
                    val product = comparisonModel.recommendationItem
                    TopAdsUrlHitter(context).hitClickUrl(
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
                                chipsTitle = comparisonModel.productCardModel.productName,
                                position = adapterPosition,
                                isLoggedIn = userSession.isLoggedIn,
                                anchorProductId = comparisonListModel.getAnchorProduct()?.recommendationItem?.productId.toString()
                        )
                )
                comparisonWidgetInterface.onProductCardClicked(comparisonModel.recommendationItem, comparisonListModel, adapterPosition)
            }
        }
        binding?.productCardView?.addOnImpressionListener(comparisonModel) {
            if (comparisonModel.recommendationItem.isTopAds) {
                val product = comparisonModel.recommendationItem
                TopAdsUrlHitter(context).hitImpressionUrl(
                        CLASS_NAME,
                        product.trackerImageUrl,
                        product.productId.toString(),
                        product.name,
                        product.imageUrl
                )
            }
            trackingQueue?.putEETracking(
                    ProductRecommendationTracking.getImpressionProductTracking(
                            recommendationItem = comparisonModel.recommendationItem,
                            androidPageName = recommendationTrackingModel.androidPageName,
                            headerTitle = recommendationTrackingModel.headerTitle,
                            position = adapterPosition,
                            isLoggedIn = userSession.isLoggedIn,
                            anchorProductId = comparisonListModel.getAnchorProduct()?.recommendationItem?.productId.toString()
                    )
            )
            comparisonWidgetInterface.onProductCardImpressed(comparisonModel.recommendationItem, comparisonListModel, adapterPosition)
        }
    }
}