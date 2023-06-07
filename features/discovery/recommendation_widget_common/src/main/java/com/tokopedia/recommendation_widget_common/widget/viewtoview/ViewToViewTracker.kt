package com.tokopedia.recommendation_widget_common.widget.viewtoview

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.constant.TrackerConstant
import com.tokopedia.trackingoptimizer.TrackingQueue

object ViewToViewTracker {
    const val TRACKER_ID_IMPRESS = "40439"
    const val TRACKER_ID_CLICK = "40441"

    const val ACTION_IMPRESSION = "impression on banner v2v widget"
    const val ACTION_CLICK = "click on banner v2v widget"

    const val BUSINESS_UNIT_HOME = "home & browse"
    const val PAGE_PDP = "product detail page"

    const val KEY_CREATIVE_NAME = "creative_name"
    const val KEY_CREATIVE_SLOT = "creative_slot"
    const val KEY_ITEM_ID = "item_id"
    const val KEY_ITEM_NAME = "item_name"

    const val ITEM_NAME_FORMAT = "/product - p%s - v2v widget - rekomendasi untuk anda - banner - %s"

    const val VALUE_NULL = "null"

    fun eventImpressViewToView(
        position: Int,
        product: RecommendationItem,
        androidPageName: String = PAGE_PDP, // remove default value once migrated to global, use RecommendationWidgetTrackingModel.androidPageName instead
        pageTitle: String,
        productId: String,
        userId: String,
        trackingQueue: TrackingQueue?
    ) {
        val itemList = arrayListOf(product.asDataLayer(position, pageTitle))
        val dataLayer = DataLayer.mapOf(
            TrackerConstant.EVENT, RecommendationTrackingConstants.Action.PROMO_VIEW,
            TrackerConstant.EVENT_ACTION, ACTION_IMPRESSION,
            TrackerConstant.EVENT_CATEGORY, androidPageName,
            TrackerConstant.EVENT_LABEL, pageTitle,
            TrackerConstant.BUSINESS_UNIT, BUSINESS_UNIT_HOME,
            TrackerConstant.CURRENT_SITE, RecommendationTrackingConstants.Tracking.CURRENT_SITE_MP,
            TrackerConstant.TRACKER_ID, TRACKER_ID_IMPRESS,
            RecommendationTrackingConstants.Tracking.ECOMMERCE,
            DataLayer.mapOf(
                RecommendationTrackingConstants.Tracking.CURRENCY_CODE,
                RecommendationTrackingConstants.Tracking.IDR,
                RecommendationTrackingConstants.Action.PROMO_VIEW,
                DataLayer.mapOf(
                    RecommendationTrackingConstants.Tracking.PROMOTIONS,
                    itemList
                )
            ),
            RecommendationTrackingConstants.Tracking.PRODUCT_ID, productId,
            TrackerConstant.USERID, userId
        ) as HashMap<String, Any>

        trackingQueue?.putEETracking(dataLayer)
    }

    private fun RecommendationItem.asPromotionBundle(
        position: Int,
        headerName: String
    ) = Bundle().apply {
        val currentPosition = position + 1
        putString(KEY_CREATIVE_NAME, VALUE_NULL)
        putString(KEY_CREATIVE_SLOT, currentPosition.toString())
        putString(KEY_ITEM_ID, asItemId())
        putString(KEY_ITEM_NAME, asItemName(currentPosition, headerName))
    }

    private fun RecommendationItem.asDataLayer(
        position: Int,
        headerName: String
    ): Map<String, Any> {
        val currentPosition = position + 1
        return hashMapOf(
            KEY_CREATIVE_NAME to VALUE_NULL,
            KEY_CREATIVE_SLOT to currentPosition.toString(),
            KEY_ITEM_ID to asItemId(),
            KEY_ITEM_NAME to asItemName(currentPosition, headerName)
        )
    }

    private fun RecommendationItem.asItemId(): String {
        val channelId = VALUE_NULL
        val bannerId = VALUE_NULL
        val targetingType = VALUE_NULL
        val targetingValue = VALUE_NULL
        return "${channelId}_${bannerId}_${targetingType}_${targetingValue}_${departmentId}_$recommendationType"
    }

    private fun RecommendationItem.asItemName(
        position: Int,
        headerName: String
    ): String {
        return ITEM_NAME_FORMAT.format(position, headerName)
    }

    fun eventClickViewToView(
        position: Int,
        product: RecommendationItem,
        androidPageName: String = PAGE_PDP, // remove default value once migrated to global, use RecommendationWidgetTrackingModel.androidPageName instead
        pageTitle: String,
        productId: String,
        userId: String
    ) {
        val itemBundle = Bundle().apply {
            putString(TrackerConstant.EVENT, RecommendationTrackingConstants.Action.SELECT_CONTENT)
            putString(TrackerConstant.EVENT_ACTION, ACTION_CLICK)
            putString(TrackerConstant.EVENT_CATEGORY, androidPageName)
            putString(TrackerConstant.EVENT_LABEL, pageTitle)
            putString(TrackerConstant.BUSINESS_UNIT, BUSINESS_UNIT_HOME)
            putString(TrackerConstant.CURRENT_SITE, RecommendationTrackingConstants.Tracking.CURRENT_SITE_MP)
            putString(TrackerConstant.TRACKER_ID, TRACKER_ID_CLICK)

            val bundlePromotion = product.asPromotionBundle(position, pageTitle)
            val list = arrayListOf(bundlePromotion)
            putParcelableArrayList(RecommendationTrackingConstants.Tracking.PROMOTIONS, list)

            putString(RecommendationTrackingConstants.Tracking.PRODUCT_ID, productId)
            putString(TrackerConstant.USERID, userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(RecommendationTrackingConstants.Action.SELECT_CONTENT, itemBundle)
    }
}
