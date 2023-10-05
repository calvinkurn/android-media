package com.tokopedia.recommendation_widget_common.widget.carousel.global

import android.os.Bundle
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Action.PRODUCT_VIEW
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Action.SELECT_CONTENT
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.BUSINESS_UNIT_HOME
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.CURRENCY_CODE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.CURRENT_SITE_MP
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DEFAULT_VALUE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DIMENSION_40
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DIMENSION_45
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DIMENSION_56
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DIMENSION_90
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ECOMMERCE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.IDR
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.IMPRESSIONS
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEMS
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_BRAND
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_CATEGORY
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_NAME
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_VARIANT
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.KEY_INDEX
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.LIST
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.PRICE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.QUANTITY
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.SHOP_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.SHOP_NAME
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.SHOP_TYPE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.TRACKER_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.VALUE_IS_TOPADS
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.VALUE_NONE_OTHER
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.convertToWidgetType
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetSource
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.track.constant.TrackerConstant
import com.tokopedia.track.constant.TrackerConstant.BUSINESS_UNIT
import com.tokopedia.track.constant.TrackerConstant.CURRENT_SITE
import com.tokopedia.trackingoptimizer.TrackingQueue

object RecommendationCarouselTracking {
    private const val EVENT_ACTION_ATC = "atc pdp recom with atc"
    private const val TRACKER_ID_IMPRESSION = "44135"
    private const val TRACKER_ID_CLICK = "44140"
    private const val TRACKER_ID_ATC = "43020"
    private const val ATC_DIMENSION_40_FORMAT = "/product - %s - rekomendasi untuk anda - %s%s - %s - %s"
    private const val IMPRESSION_CLICK_DIMENSION_40_FORMAT =
        "/%s - %s - rekomendasi untuk anda - %s%s"

    private fun bundle(bundleApply: Bundle.() -> Unit): Bundle {
        return Bundle().apply(bundleApply)
    }

    fun sendEventAtcClick(
        recomItem: RecommendationItem,
        userId: String,
        quantity: Int,
        androidPageName: String = RecommendationWidgetSource.PDP.trackingValue // remove default value after recommendation carousel widget migration
    ) {
        val bundle = Bundle().apply {
            putString(EVENT, RecommendationTrackingConstants.Action.ADD_TO_CART)
            putString(EVENT_ACTION, EVENT_ACTION_ATC)

            putString(EVENT_CATEGORY, androidPageName)

            putString(EVENT_LABEL, recomItem.productId.toString())

            putString(BUSINESS_UNIT, BUSINESS_UNIT_HOME)
            putString(CURRENT_SITE, CURRENT_SITE_MP)
            putString(TRACKER_ID, TRACKER_ID_ATC)

            val bundleProduct = Bundle().apply {
                putString(RecommendationTrackingConstants.Tracking.CATEGORY_ID, recomItem.departmentId.toString())
                putString(
                    DIMENSION_40,
                    ATC_DIMENSION_40_FORMAT.format(
                        recomItem.pageName,
                        recomItem.recommendationType,
                        if (recomItem.isTopAds) VALUE_IS_TOPADS else DEFAULT_VALUE,
                        recomItem.type.convertToWidgetType(),
                        recomItem.anchorProductId
                    )
                )
                putString(DIMENSION_45, recomItem.cartId)
                putString(DIMENSION_56, recomItem.warehouseId.toString())
                putString(DIMENSION_90, "%s.%s".format(androidPageName, recomItem.recommendationType))
                putString(ITEM_BRAND, VALUE_NONE_OTHER)
                putString(ITEM_CATEGORY, recomItem.categoryBreadcrumbs)
                putString(ITEM_ID, recomItem.productId.toString())
                putString(ITEM_NAME, recomItem.name)
                putString(ITEM_VARIANT, VALUE_NONE_OTHER)
                putDouble(PRICE, recomItem.priceInt.toDouble())
                putLong(QUANTITY, quantity.toLong())
                putString(SHOP_ID, recomItem.shopId.toString())
                putString(SHOP_NAME, recomItem.shopName)
                putString(SHOP_TYPE, recomItem.shopType)
            }

            val list = arrayListOf(bundleProduct)
            putParcelableArrayList(ITEMS, list)

            putString(RecommendationTrackingConstants.Tracking.PRODUCT_ID, recomItem.productId.toString())
            putString(TrackerConstant.USERID, userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(RecommendationTrackingConstants.Action.ADD_TO_CART, bundle)
    }

    fun sendEventItemImpression(
        trackingQueue: TrackingQueue,
        widget: RecommendationWidget,
        item: RecommendationItem,
        trackingModel: RecommendationWidgetTrackingModel,
    ) {
        trackingQueue.putEETracking(hashMapOf(
            EVENT to PRODUCT_VIEW,
            EVENT_CATEGORY to trackingModel.androidPageName,
            EVENT_ACTION to trackingModel.eventActionImpression,
            EVENT_LABEL to widget.title,
            TRACKER_ID to TRACKER_ID_IMPRESSION,
            BUSINESS_UNIT to BUSINESS_UNIT_HOME,
            CURRENT_SITE to CURRENT_SITE_MP,
            ECOMMERCE to mapOf(
                CURRENCY_CODE to IDR,
                IMPRESSIONS to arrayListOf(mapOf(
                    LIST to IMPRESSION_CLICK_DIMENSION_40_FORMAT.format(
                        trackingModel.listPageName,
                        item.pageName,
                        item.recommendationType,
                        if (item.isTopAds) VALUE_IS_TOPADS else DEFAULT_VALUE,
                    ),
                    DIMENSION_56 to item.warehouseId.toString(),
                    KEY_INDEX to item.position + 1,
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

    fun sendEventItemClick(
        widget: RecommendationWidget,
        item: RecommendationItem,
        trackingModel: RecommendationWidgetTrackingModel,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            SELECT_CONTENT,
            bundle {
                putString(EVENT_CATEGORY, trackingModel.androidPageName)
                putString(EVENT_ACTION, trackingModel.eventActionClick)
                putString(EVENT_LABEL, widget.title)
                putString(TRACKER_ID, TRACKER_ID_CLICK)
                putString(BUSINESS_UNIT, BUSINESS_UNIT_HOME)
                putString(CURRENT_SITE, CURRENT_SITE_MP)
                putParcelableArrayList(ITEMS, arrayListOf(bundle {
                    putString(DIMENSION_40, IMPRESSION_CLICK_DIMENSION_40_FORMAT.format(
                        trackingModel.listPageName,
                        item.pageName,
                        item.recommendationType,
                        if (item.isTopAds) VALUE_IS_TOPADS else DEFAULT_VALUE,
                    ))
                    putString(DIMENSION_56, item.warehouseId.toString())
                    putInt(KEY_INDEX, item.position + 1)
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

    fun sendEventSeeMoreClick() {

    }
}
