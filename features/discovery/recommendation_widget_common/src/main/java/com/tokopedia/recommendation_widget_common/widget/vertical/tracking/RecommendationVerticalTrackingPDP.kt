package com.tokopedia.recommendation_widget_common.widget.vertical.tracking

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Action.PRODUCT_VIEW
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Action.SELECT_CONTENT
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.BUSINESS_UNIT_HOME
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.CLICK_SEE_MORE_WIDGET
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.CURRENCY_CODE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.CURRENT_SITE_MP
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DEFAULT_VALUE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DIMENSION_40
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DIMENSION_56
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ECOMMERCE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.IDR
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.IMPRESSIONS
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEMS
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_BRAND
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_CATEGORY
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_LIST
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_NAME
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_VARIANT
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.KEY_INDEX
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.LIST
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.PRICE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.TRACKER_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.VALUE_IS_TOPADS
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.VALUE_NONE_OTHER
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselTrackingConst
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetSource
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.track.constant.TrackerConstant.BUSINESS_UNIT
import com.tokopedia.track.constant.TrackerConstant.CURRENT_SITE
import com.tokopedia.track.constant.TrackerConstant.USERID
import com.tokopedia.trackingoptimizer.TrackingQueue

class RecommendationVerticalTrackingPDP(
    private val widget: RecommendationWidget,
    private val source: RecommendationWidgetSource.PDP,
    private val userId: String
): RecommendationVerticalTracking {
    companion object {
        private const val EVENT_ACTION_IMPRESSION = "impression - product recommendation pdp recom"
        private const val EVENT_ACTION_CLICK = "click - product recommendation pdp recom"
        private const val TRACKER_ID_IMPRESSION = "7889"
        private const val TRACKER_ID_CLICK = "7890"
        private const val IMPRESSION_CLICK_DIMENSION_40_FORMAT =
            "/${RecommendationCarouselTrackingConst.List.PRODUCT} - %s - rekomendasi untuk anda - %s%s - %s - %s"
    }

    private fun bundle(bundleApply: Bundle.() -> Unit): Bundle {
        return Bundle().apply(bundleApply)
    }

    override fun sendEventItemImpression(trackingQueue: TrackingQueue, item: RecommendationItem) {
        val list = IMPRESSION_CLICK_DIMENSION_40_FORMAT.format(
            item.pageName,
            item.recommendationType,
            if (item.isTopAds) VALUE_IS_TOPADS else DEFAULT_VALUE,
            widget.layoutType,
            source.anchorProductId
        )
        trackingQueue.putEETracking(hashMapOf(
            EVENT to PRODUCT_VIEW,
            EVENT_CATEGORY to source.eventCategory,
            EVENT_ACTION to EVENT_ACTION_IMPRESSION,
            EVENT_LABEL to "${widget.title} - $DEFAULT_VALUE",
            TRACKER_ID to TRACKER_ID_IMPRESSION,
            BUSINESS_UNIT to BUSINESS_UNIT_HOME,
            CURRENT_SITE to CURRENT_SITE_MP,
            ITEM_LIST to list,
            USERID to userId,
            ECOMMERCE to mapOf(
                CURRENCY_CODE to IDR,
                IMPRESSIONS to arrayListOf(mapOf(
                    LIST to list,
                    DIMENSION_56 to item.warehouseId.toString(),
                    KEY_INDEX to (item.position + 1).toString(),
                    ITEM_BRAND to VALUE_NONE_OTHER,
                    ITEM_CATEGORY to item.categoryBreadcrumbs,
                    ITEM_ID to item.productId,
                    ITEM_NAME to item.name,
                    ITEM_VARIANT to VALUE_NONE_OTHER,
                    PRICE to item.priceInt.toDouble()
                )),
            )
        ))
    }

    override fun sendEventItemClick(item: RecommendationItem) {
        val list = IMPRESSION_CLICK_DIMENSION_40_FORMAT.format(
            item.pageName,
            item.recommendationType,
            if (item.isTopAds) VALUE_IS_TOPADS else DEFAULT_VALUE,
            widget.layoutType,
            source.anchorProductId
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            SELECT_CONTENT,
            bundle {
                putString(EVENT, SELECT_CONTENT)
                putString(EVENT_CATEGORY, source.eventCategory)
                putString(EVENT_ACTION, EVENT_ACTION_CLICK)
                putString(EVENT_LABEL, "${widget.title} - $DEFAULT_VALUE")
                putString(ITEM_LIST, list)
                putString(TRACKER_ID, TRACKER_ID_CLICK)
                putString(BUSINESS_UNIT, BUSINESS_UNIT_HOME)
                putString(CURRENT_SITE, CURRENT_SITE_MP)
                putParcelableArrayList(ITEMS, arrayListOf(bundle {
                    putString(DIMENSION_40, list)
                    putString(DIMENSION_56, item.warehouseId.toString())
                    putString(KEY_INDEX, (item.position + 1).toString())
                    putString(ITEM_BRAND, VALUE_NONE_OTHER)
                    putString(ITEM_CATEGORY, item.categoryBreadcrumbs)
                    putString(ITEM_ID, item.productId.toString())
                    putString(ITEM_NAME, item.name)
                    putString(ITEM_VARIANT, VALUE_NONE_OTHER)
                    putDouble(PRICE, item.priceInt.toDouble())
                }))
            }
        )
    }

    override fun sendEventSeeMoreClick() {
        val map = DataLayer.mapOf(
            EVENT, RecommendationTrackingConstants.Tracking.CLICK_PDP,
            EVENT_ACTION, CLICK_SEE_MORE_WIDGET.format(widget.pageName),
            EVENT_CATEGORY, source.eventCategory,
            EVENT_LABEL, "${widget.title} - ${widget.pageName} - ${widget.layoutType}",
            BUSINESS_UNIT, BUSINESS_UNIT_HOME,
            CURRENT_SITE, CURRENT_SITE_MP
        ) + source.trackingMap
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    object Factory {
        fun create(
            widget: RecommendationWidget,
            source: RecommendationWidgetSource.PDP,
            userId: String
        ): RecommendationVerticalTracking = RecommendationVerticalTrackingPDP(widget, source, userId)
    }
}
