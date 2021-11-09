package com.tokopedia.officialstore.official.presentation.listener

import android.content.Context
import com.tokopedia.officialstore.analytics.RecommendationWidgetTracker
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelEventHandler
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.bestseller.factory.RecommendationWidgetListener
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.HashMap

class RecommendationWidgetCallback(
    private val dcEventHandler: DynamicChannelEventHandler,
    context:Context?,
    private val userId: String

): RecommendationWidgetListener {

    private val trackingQueue = context?.let { TrackingQueue(it) }

    override fun onBestSellerClick(bestSellerDataModel: BestSellerDataModel, recommendationItem: RecommendationItem, widgetPosition: Int) {
        RecommendationWidgetTracker.sendClickTracker(recommendationItem, userId)
        dcEventHandler.onBestSellerClick(recommendationItem.appUrl)
    }

    override fun onBestSellerImpress(bestSellerDataModel: BestSellerDataModel, recommendationItem: RecommendationItem, widgetPosition: Int) {
        trackingQueue?.putEETracking(RecommendationWidgetTracker.getImpressionTracker(recommendationItem, userId) as HashMap<String, Any>)
    }

    override fun onBestSellerThreeDotsClick(bestSellerDataModel: BestSellerDataModel, recommendationItem: RecommendationItem, widgetPosition: Int) {
        dcEventHandler.onBestSellerThreeDotsClick(recommendationItem, widgetPosition)
    }

    override fun onBestSellerFilterClick(filter: RecommendationFilterChipsEntity.RecommendationFilterChip, bestSellerDataModel: BestSellerDataModel, widgetPosition: Int, selectedChipsPosition: Int) {
    }

    override fun onBestSellerSeeMoreTextClick(bestSellerDataModel: BestSellerDataModel, appLink: String, widgetPosition: Int) {
        dcEventHandler.onBestSellerSeeMoreTextClick(appLink)
    }

    override fun onBestSellerSeeAllCardClick(bestSellerDataModel: BestSellerDataModel, appLink: String, widgetPosition: Int) {
        dcEventHandler.onBestSellerSeeAllCardClick(appLink)
    }
}
