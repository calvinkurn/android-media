package com.tokopedia.recommendation_widget_common.widget.comparison.compareditem

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.recommendation_widget_common.widget.ProductRecommendationTracking
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonListModel
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonModel
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonWidgetInterface
import com.tokopedia.recommendation_widget_common.widget.comparison.RecommendationTrackingModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.item_comparison_compared_widget.view.*

class ComparisonWidgetComparedItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    companion object {
        private const val CLASS_NAME = "com.tokopedia.recommendation_widget_common.widget.comparison.caompareditem.ComparisonWidgetComparedItemViewHolder.kt"
    }
    val context: Context = view.context

    fun bind(
        comparisonModel: ComparisonModel,
        comparisonListModel: ComparisonListModel,
        comparisonWidgetInterface: ComparisonWidgetInterface,
        recommendationTrackingModel: RecommendationTrackingModel,
        trackingQueue: TrackingQueue?,
        userSession: UserSessionInterface,
        comparedProductPosition: Int = -1
    ) {
        if (comparedProductPosition == 0)
            view.visibility = View.INVISIBLE

        view.specsView.setSpecsInfo(comparisonModel.specsModel)
        view.productCardView.setProductModel(comparisonModel.productCardModel)
        if (!comparisonModel.isCurrentItem) {
            view.productCardView.setOnClickListener {
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
                TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(ProductRecommendationTracking.PRODUCT_CLICK,
                    ProductRecommendationTracking.getClickProductTracking(
                        recommendationItem = comparisonModel.recommendationItem,
                        androidPageName = recommendationTrackingModel.androidPageName,
                        headerTitle = recommendationTrackingModel.headerTitle,
                        chipsTitle = comparisonModel.productCardModel.productName,
                        position = (adapterPosition+1),
                        isLoggedIn = userSession.isLoggedIn,
                        anchorProductId = comparisonListModel.getAnchorProduct()?.recommendationItem?.productId.toString()
                    )
                )
                comparisonWidgetInterface.onProductCardClicked(comparisonModel.recommendationItem, comparisonListModel, adapterPosition)
            }
        }
        view.productCardView.addOnImpressionListener(comparisonModel) {
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
                    position = (adapterPosition+1),
                    isLoggedIn = userSession.isLoggedIn,
                    anchorProductId = comparisonListModel.getAnchorProduct()?.recommendationItem?.productId.toString()
                )
            )
            comparisonWidgetInterface.onProductCardImpressed(comparisonModel.recommendationItem, comparisonListModel, adapterPosition)
        }
    }
}