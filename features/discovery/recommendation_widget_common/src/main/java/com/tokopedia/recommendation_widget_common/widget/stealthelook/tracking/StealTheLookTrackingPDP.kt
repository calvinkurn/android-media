package com.tokopedia.recommendation_widget_common.widget.stealthelook.tracking

import android.os.Bundle
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Action.SELECT_CONTENT
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.BUSINESS_UNIT_HOME
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.CURRENCY_CODE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.CURRENT_SITE_MP
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DEFAULT_VALUE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DIMENSION_40
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DIMENSION_56
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DIMENSION_58
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.IMPRESSIONS
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEMS
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_BRAND
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_CATEGORY
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_LIST
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_NAME
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_VARIANT
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.KEY_INDEX
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.PRICE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.TRACKER_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.VALUE_IS_TOPADS
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.VALUE_NONE_OTHER
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetSource
import com.tokopedia.recommendation_widget_common.widget.stealthelook.StealTheLookGridModel
import com.tokopedia.recommendation_widget_common.widget.stealthelook.StealTheLookStyleModel
import com.tokopedia.recommendation_widget_common.widget.vertical.tracking.RecommendationVerticalTrackingPDP
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.constant.TrackerConstant
import com.tokopedia.trackingoptimizer.TrackingQueue

class StealTheLookTrackingPDP(
    private val widget: RecommendationWidget,
    private val source: RecommendationWidgetSource.PDP,
    private val userId: String
): StealTheLookTracking {
    companion object {
        private const val LIST_FORMAT = "/product - %s - rekomendasi untuk anda - %s%s - %s - %s - %s - %s - %s"

        private const val EVENT_ACTION_IMPRESSION = "impression on product steal the look"
        private const val TRACKER_ID_IMPRESSION = "48727"

        private const val EVENT_ACTION_CLICK = "click on product steal the look"
        private const val TRACKER_ID_CLICK = "48728"
    }

    override fun sendEventViewportImpression(
        trackingQueue: TrackingQueue,
        model: StealTheLookStyleModel
    ) {
        trackingQueue.putEETracking(hashMapOf(
            TrackAppUtils.EVENT to RecommendationTrackingConstants.Action.PRODUCT_VIEW,
            TrackAppUtils.EVENT_CATEGORY to source.eventCategory,
            TrackAppUtils.EVENT_ACTION to EVENT_ACTION_IMPRESSION,
            TrackAppUtils.EVENT_LABEL to "${widget.title} - ${source.anchorProductId}",
            TRACKER_ID to TRACKER_ID_IMPRESSION,
            TrackerConstant.BUSINESS_UNIT to RecommendationTrackingConstants.Tracking.BUSINESS_UNIT_HOME,
            TrackerConstant.CURRENT_SITE to RecommendationTrackingConstants.Tracking.CURRENT_SITE_MP,
            ITEM_LIST to LIST_FORMAT.format(
                model.recommendationWidget.pageName,
                DEFAULT_VALUE,
                DEFAULT_VALUE,
                widget.layoutType,
                model.stylePosition,
                DEFAULT_VALUE,
                DEFAULT_VALUE,
                source.anchorProductId
            ),
            TrackerConstant.USERID to userId,
            RecommendationTrackingConstants.Tracking.ECOMMERCE to mapOf(
                CURRENCY_CODE to RecommendationTrackingConstants.Tracking.IDR,
                IMPRESSIONS to model.gridPositionMap.toList().mapIndexed { index, pair ->
                    val item = pair.second.recommendationItem
                    mapOf(
                        DIMENSION_40 to LIST_FORMAT.format(
                            model.recommendationWidget.pageName,
                            item.recommendationType,
                            if(item.isTopAds) VALUE_IS_TOPADS else DEFAULT_VALUE,
                            widget.layoutType,
                            model.stylePosition,
                            index + 1,
                            item.departmentId,
                            source.anchorProductId
                        ),
                        DIMENSION_56 to item.warehouseId.toString(),
                        KEY_INDEX to (index + 1).toString(),
                        ITEM_BRAND to VALUE_NONE_OTHER,
                        ITEM_CATEGORY to item.categoryBreadcrumbs,
                        ITEM_ID to item.productId,
                        ITEM_NAME to item.name,
                        ITEM_VARIANT to VALUE_NONE_OTHER,
                        PRICE to item.priceInt.toDouble()
                    )
                },
            )
        ))
    }

    override fun sendEventItemClick(model: StealTheLookGridModel) {
        val item = model.recommendationItem
        val list = LIST_FORMAT.format(
            model.recommendationWidget.pageName,
            item.recommendationType,
            if(item.isTopAds) VALUE_IS_TOPADS else DEFAULT_VALUE,
            widget.layoutType,
            model.stylePosition,
            model.position,
            item.departmentId,
            source.anchorProductId
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            SELECT_CONTENT,
            Bundle().apply {
                putString(TrackAppUtils.EVENT, SELECT_CONTENT)
                putString(TrackAppUtils.EVENT_CATEGORY, source.eventCategory)
                putString(TrackAppUtils.EVENT_ACTION, EVENT_ACTION_CLICK)
                putString(TrackAppUtils.EVENT_LABEL, "${widget.title} - ${source.anchorProductId}")
                putString(ITEM_LIST, list)
                putString(TRACKER_ID, TRACKER_ID_CLICK)
                putString(TrackerConstant.BUSINESS_UNIT, BUSINESS_UNIT_HOME)
                putString(TrackerConstant.CURRENT_SITE, CURRENT_SITE_MP)
                putParcelableArrayList(ITEMS, arrayListOf(
                    Bundle().apply {
                        putString(DIMENSION_40, list)
                        putString(DIMENSION_56, item.warehouseId.toString())
                        putString(KEY_INDEX, (model.position + 1).toString())
                        putString(ITEM_BRAND, VALUE_NONE_OTHER)
                        putString(ITEM_CATEGORY, item.categoryBreadcrumbs)
                        putString(ITEM_ID, item.productId.toString())
                        putString(ITEM_NAME, item.name)
                        putString(ITEM_VARIANT, VALUE_NONE_OTHER)
                        putDouble(PRICE, item.priceInt.toDouble())
                    }
                ))
            }
        )
    }

    object Factory {
        fun create(
            widget: RecommendationWidget,
            source: RecommendationWidgetSource.PDP,
            userId: String
        ): StealTheLookTracking = StealTheLookTrackingPDP(widget, source, userId)
    }
}
