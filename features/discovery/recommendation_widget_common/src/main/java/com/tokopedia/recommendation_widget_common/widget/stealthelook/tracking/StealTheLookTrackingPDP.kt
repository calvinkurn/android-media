package com.tokopedia.recommendation_widget_common.widget.stealthelook.tracking

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.orZero
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
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.PRODUCT_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.TRACKER_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.VALUE_IS_TOPADS
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.VALUE_NONE_OTHER
import com.tokopedia.recommendation_widget_common.extension.hasLabelGroupFulfillment
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetSource
import com.tokopedia.recommendation_widget_common.widget.stealthelook.StealTheLookGridModel
import com.tokopedia.recommendation_widget_common.widget.stealthelook.StealTheLookStyleModel
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
        model: StealTheLookStyleModel
    ) {
        val anchorItem = model.grids.firstOrNull()?.recommendationItem
        val stylePosition = model.stylePosition + 1
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(hashMapOf(
            TrackAppUtils.EVENT to RecommendationTrackingConstants.Action.PRODUCT_VIEW,
            TrackAppUtils.EVENT_CATEGORY to source.eventCategory,
            TrackAppUtils.EVENT_ACTION to EVENT_ACTION_IMPRESSION,
            TrackAppUtils.EVENT_LABEL to "${widget.title} - ${source.anchorProductId}",
            TRACKER_ID to TRACKER_ID_IMPRESSION,
            PRODUCT_ID to source.anchorProductId,
            TrackerConstant.BUSINESS_UNIT to BUSINESS_UNIT_HOME,
            TrackerConstant.CURRENT_SITE to CURRENT_SITE_MP,
            ITEM_LIST to LIST_FORMAT.format(
                widget.pageName,
                anchorItem?.recommendationType.orEmpty(),
                if(anchorItem?.isTopAds == true) VALUE_IS_TOPADS else DEFAULT_VALUE,
                widget.layoutType,
                stylePosition,
                anchorItem?.gridPosition?.value.orEmpty(),
                anchorItem?.departmentId.orZero(),
                anchorItem?.productId
            ),
            TrackerConstant.USERID to userId,
            RecommendationTrackingConstants.Tracking.ECOMMERCE to mapOf(
                CURRENCY_CODE to RecommendationTrackingConstants.Tracking.IDR,
                IMPRESSIONS to model.grids.map {
                    val item = it.recommendationItem
                    mapOf(
                        DIMENSION_40 to LIST_FORMAT.format(
                            widget.pageName,
                            item.recommendationType,
                            if(item.isTopAds) VALUE_IS_TOPADS else DEFAULT_VALUE,
                            widget.layoutType,
                            stylePosition,
                            item.gridPosition.value,
                            item.departmentId,
                            item.productId
                        ),
                        DIMENSION_56 to item.warehouseId.toString(),
                        DIMENSION_58 to item.labelGroupList.hasLabelGroupFulfillment().toString(),
                        KEY_INDEX to (item.position + 1).toString(),
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
        val stylePosition = model.stylePosition + 1
        val list = LIST_FORMAT.format(
            widget.pageName,
            item.recommendationType,
            if(item.isTopAds) VALUE_IS_TOPADS else DEFAULT_VALUE,
            widget.layoutType,
            stylePosition,
            item.gridPosition.value,
            item.departmentId,
            item.productId
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
                putString(PRODUCT_ID, source.anchorProductId)
                putString(TrackerConstant.BUSINESS_UNIT, BUSINESS_UNIT_HOME)
                putString(TrackerConstant.CURRENT_SITE, CURRENT_SITE_MP)
                putParcelableArrayList(ITEMS, arrayListOf(
                    Bundle().apply {
                        putString(DIMENSION_40, list)
                        putString(DIMENSION_56, item.warehouseId.toString())
                        putString(DIMENSION_58, item.labelGroupList.hasLabelGroupFulfillment().toString())
                        putString(KEY_INDEX, (item.position + 1).toString())
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
