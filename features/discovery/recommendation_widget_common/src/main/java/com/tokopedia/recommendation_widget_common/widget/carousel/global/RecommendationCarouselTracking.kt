package com.tokopedia.recommendation_widget_common.widget.carousel.global

import android.os.Bundle
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.convertToWidgetType
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetSource
import com.tokopedia.track.TrackApp
import com.tokopedia.track.constant.TrackerConstant

object RecommendationCarouselTracking {
    private const val EVENT_ACTION_ATC = "atc pdp recom with atc"
    private const val TRACKER_ID_ATC = "43020"
    private const val ATC_DIMENSION_40_FORMAT = "/product - %s - rekomendasi untuk anda - %s%s - %s - %s"

    fun sendEventAtcClick(
        recomItem: RecommendationItem,
        userId: String,
        quantity: Int,
        androidPageName: String = RecommendationWidgetSource.PDP.trackingValue // remove default value after recommendation carousel widget migration
    ) {
        val bundle = Bundle().apply {
            putString(TrackerConstant.EVENT, RecommendationTrackingConstants.Action.ADD_TO_CART)
            putString(TrackerConstant.EVENT_ACTION, EVENT_ACTION_ATC)

            putString(TrackerConstant.EVENT_CATEGORY, androidPageName)

            putString(TrackerConstant.EVENT_LABEL, recomItem.productId.toString())

            putString(TrackerConstant.BUSINESS_UNIT, RecommendationTrackingConstants.Tracking.BUSINESS_UNIT_HOME)
            putString(TrackerConstant.CURRENT_SITE, RecommendationTrackingConstants.Tracking.CURRENT_SITE_MP)
            putString(TrackerConstant.TRACKER_ID, TRACKER_ID_ATC)

            val bundleProduct = Bundle().apply {
                putString(RecommendationTrackingConstants.Tracking.CATEGORY_ID, recomItem.departmentId.toString())
                putString(
                    RecommendationTrackingConstants.Tracking.DIMENSION_40,
                    ATC_DIMENSION_40_FORMAT.format(
                        recomItem.pageName,
                        recomItem.recommendationType,
                        if (recomItem.isTopAds) RecommendationTrackingConstants.Tracking.VALUE_IS_TOPADS else RecommendationTrackingConstants.Tracking.DEFAULT_VALUE,
                        recomItem.type.convertToWidgetType(),
                        recomItem.anchorProductId
                    )
                )
                putString(RecommendationTrackingConstants.Tracking.DIMENSION_45, recomItem.cartId)
                putString(RecommendationTrackingConstants.Tracking.DIMENSION_90, "%s.%s".format(androidPageName, recomItem.recommendationType))
                putString(RecommendationTrackingConstants.Tracking.ITEM_BRAND, RecommendationTrackingConstants.Tracking.VALUE_NONE_OTHER)
                putString(RecommendationTrackingConstants.Tracking.ITEM_CATEGORY, recomItem.categoryBreadcrumbs)
                putLong(RecommendationTrackingConstants.Tracking.ITEM_ID, recomItem.productId)
                putString(RecommendationTrackingConstants.Tracking.ITEM_NAME, recomItem.name)
                putString(RecommendationTrackingConstants.Tracking.ITEM_VARIANT, RecommendationTrackingConstants.Tracking.VALUE_NONE_OTHER)
                putString(RecommendationTrackingConstants.Tracking.PRICE, "%.1f".format(recomItem.priceInt.toDouble()))
                putInt(RecommendationTrackingConstants.Tracking.QUANTITY, quantity)
                putInt(RecommendationTrackingConstants.Tracking.SHOP_ID, recomItem.shopId)
                putString(RecommendationTrackingConstants.Tracking.SHOP_NAME, recomItem.shopName)
                putString(RecommendationTrackingConstants.Tracking.SHOP_TYPE, recomItem.shopType)
            }

            val list = arrayListOf(bundleProduct)
            putParcelableArrayList(RecommendationTrackingConstants.Tracking.ITEMS, list)

            putString(RecommendationTrackingConstants.Tracking.PRODUCT_ID, recomItem.productId.toString())
            putString(TrackerConstant.USERID, userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(RecommendationTrackingConstants.Action.ADD_TO_CART, bundle)
    }

    fun sendEventItemImpression() {

    }

    fun sendEventItemClick() {

    }

    fun sendEventSeeMoreClick() {

    }
}
