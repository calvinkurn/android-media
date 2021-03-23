package com.tokopedia.recommendation_widget_common.widget.comparison

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.recommendation_widget_common.widget.ProductRecommendationTracking
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.item_comparison_widget.view.*

class ComparisonWidgetItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val context: Context = view.context

    fun bind(
            comparisonModel: ComparisonModel,
            comparisonListModel: ComparisonListModel,
            comparisonWidgetInterface: ComparisonWidgetInterface,
            recommendationTrackingModel: RecommendationTrackingModel,
            trackingQueue: TrackingQueue,
            userSession: UserSessionInterface
    ) {
        view.specsView.setSpecsInfo(comparisonModel.specsModel)
        view.productCardView.setProductModel(comparisonModel.productCardModel)
        view.productCardView.setOnClickListener {
            trackingQueue.putEETracking(
                    ProductRecommendationTracking.getClickProductTracking(
                            recommendationItems = listOf(
                                    comparisonModel.recommendationItem
                            ),
                            androidPageName = recommendationTrackingModel.androidPageName,
                            headerTitle = recommendationTrackingModel.headerTitle,
                            chipsTitle = comparisonModel.productCardModel.productName,
                            recomPageName = comparisonModel.recommendationItem.pageName,
                            isTopads = comparisonModel.recommendationItem.isTopAds,
                            widgetType = comparisonModel.recommendationItem.type,
                            productId = comparisonModel.recommendationItem.productId.toString(),
                            position = (adapterPosition+1),
                            isLoggedIn = userSession.isLoggedIn,
                            recommendationType = comparisonModel.recommendationItem.recommendationType
                    )
            )
            comparisonWidgetInterface.onProductCardClicked(comparisonModel.recommendationItem, comparisonListModel, adapterPosition)
        }
        view.productCardView.addOnImpressionListener(comparisonModel) {
            trackingQueue.putEETracking(
                    ProductRecommendationTracking.getImpressionProductTracking(
                            recommendationItems = listOf(
                                    comparisonModel.recommendationItem
                            ),
                            androidPageName = recommendationTrackingModel.androidPageName,
                            headerTitle = recommendationTrackingModel.headerTitle,
                            chipsTitle = comparisonModel.productCardModel.productName,
                            recomPageName = comparisonModel.recommendationItem.pageName,
                            isTopads = comparisonModel.recommendationItem.isTopAds,
                            widgetType = comparisonModel.recommendationItem.type,
                            productId = comparisonModel.recommendationItem.productId.toString(),
                            position = (adapterPosition+1),
                            isLoggedIn = userSession.isLoggedIn,
                            recommendationType = comparisonModel.recommendationItem.recommendationType
                    )
            )
            comparisonWidgetInterface.onProductCardImpressed(comparisonModel.recommendationItem, comparisonListModel, adapterPosition)
        }
    }
}