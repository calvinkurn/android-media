package com.tokopedia.recommendation_widget_common.widget.carousel.global.tracking

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Action.ADD_TO_CART
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Action.PRODUCT_VIEW
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Action.SELECT_CONTENT
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.BUSINESS_UNIT_HOME
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.CATEGORY_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.CLICK_HOMEPAGE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.CURRENCY_CODE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.CURRENT_SITE_MP
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DEFAULT_VALUE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DIMENSION_40
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DIMENSION_45
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DIMENSION_56
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DIMENSION_84
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
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.PRODUCT_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.QUANTITY
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.SHOP_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.SHOP_NAME
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.SHOP_TYPE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.TRACKER_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.VALUE_IS_TOPADS
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.VALUE_NONE_OTHER
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselTrackingConst
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselTrackingConst.Action.ADJUST_QTY_PDP_RECOM_WITH_ATC
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselTrackingConst.Action.ATC_PDP_RECOM_WITH_ATC
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselTrackingConst.Action.CLICK_PDP_RECOM_SEE_ALL
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselTrackingConst.Action.CLICK_PDP_RECOM_WITH_ATC
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselTrackingConst.Action.DELETE_PDP_RECOM_WITH_ATC
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselTrackingConst.Action.IMPRESSION_PDP_RECOM_WITH_ATC
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselTrackingConst.Category.PDP
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselTrackingConst.List.REKOMENDASI_UNTUK_ANDA
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselTrackingConst.TrackerId
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselTrackingConst.TrackerId.ADJUST_QTY_ITEM_PDP_ATC
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselTrackingConst.TrackerId.ATC_RECOMMENDATION_ITEM_PDP_ATC
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselTrackingConst.TrackerId.CLICK_RECOMMENDATION_ITEM_PDP_ATC
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselTrackingConst.TrackerId.DELETE_RECOMMENDATION_ITEM_PDP_ATC
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselTrackingConst.TrackerId.SEE_ALL_RECOMMENDATION_PDP_ATC
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetSource
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.track.constant.TrackerConstant.BUSINESS_UNIT
import com.tokopedia.track.constant.TrackerConstant.CURRENT_SITE
import com.tokopedia.track.constant.TrackerConstant.USERID
import com.tokopedia.trackingoptimizer.TrackingQueue

class RecommendationCarouselWidgetTrackingPDPATC(
    private val widget: RecommendationWidget,
    private val source: RecommendationWidgetSource.PDPAfterATC
) : RecommendationCarouselWidgetTracking {

    private val anchorProductId = source.anchorProductId
    private val userId = source.userId

    private fun bundle(bundleApply: Bundle.() -> Unit): Bundle {
        return Bundle().apply(bundleApply)
    }

    private fun nonLogin() = if (!source.isUserLoggedIn) " - non login" else ""

    override fun sendEventItemImpression(
        trackingQueue: TrackingQueue,
        item: RecommendationItem
    ) {
        trackingQueue.putEETracking(impressionMap(item))
    }

    private fun impressionMap(item: RecommendationItem): HashMap<String, Any> {
        return hashMapOf(
            EVENT to PRODUCT_VIEW,
            EVENT_CATEGORY to PDP,
            EVENT_ACTION to IMPRESSION_PDP_RECOM_WITH_ATC + nonLogin(),
            EVENT_LABEL to anchorProductId,
            TRACKER_ID to TrackerId.IMPRESSION_RECOMMENDATION_ITEM_PDP_ATC,
            BUSINESS_UNIT to BUSINESS_UNIT_HOME,
            CURRENT_SITE to CURRENT_SITE_MP,
            PRODUCT_ID to anchorProductId,
            USERID to userId,
            ECOMMERCE to mapOf(
                CURRENCY_CODE to IDR,
                IMPRESSIONS to arrayListOf(
                    mapOf(
                        LIST to listName(item),
                        KEY_INDEX to item.position + 1,
                        ITEM_BRAND to VALUE_NONE_OTHER,
                        ITEM_CATEGORY to item.categoryBreadcrumbs,
                        ITEM_ID to item.productId,
                        ITEM_NAME to item.name,
                        ITEM_VARIANT to VALUE_NONE_OTHER,
                        PRICE to item.priceInt.toDouble(),
                        DIMENSION_84 to VALUE_NONE_OTHER,
                        DIMENSION_90 to "$PDP.${item.recommendationType}",
                        DIMENSION_56 to source.warehouseId
                    )
                )
            )
        )
    }

    private fun listName(item: RecommendationItem) =
        listOf(
            "/${RecommendationCarouselTrackingConst.List.PRODUCT}",
            widget.pageName,
            REKOMENDASI_UNTUK_ANDA,
            item.recommendationType + topAdsListName(item),
            widget.layoutType,
            anchorProductId
        ).joinToString(separator = " - ")

    private fun topAdsListName(item: RecommendationItem) =
        if (item.isTopAds) VALUE_IS_TOPADS else DEFAULT_VALUE

    override fun sendEventItemClick(item: RecommendationItem) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, clickBundle(item))
    }

    private fun clickBundle(item: RecommendationItem): Bundle =
        bundle {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_CATEGORY, PDP)
            putString(EVENT_ACTION, CLICK_PDP_RECOM_WITH_ATC + nonLogin())
            putString(EVENT_LABEL, anchorProductId)
            putString(TRACKER_ID, CLICK_RECOMMENDATION_ITEM_PDP_ATC)
            putString(BUSINESS_UNIT, BUSINESS_UNIT_HOME)
            putString(CURRENT_SITE, CURRENT_SITE_MP)
            putString(PRODUCT_ID, anchorProductId)
            putString(USERID, userId)
            putParcelableArrayList(
                ITEMS,
                arrayListOf(
                    bundle {
                        putString(DIMENSION_40, listName(item))
                        putInt(KEY_INDEX, item.position + 1)
                        putString(ITEM_BRAND, VALUE_NONE_OTHER)
                        putString(ITEM_CATEGORY, item.categoryBreadcrumbs)
                        putString(ITEM_ID, item.productId.toString())
                        putString(ITEM_NAME, item.name)
                        putString(ITEM_VARIANT, VALUE_NONE_OTHER)
                        putDouble(PRICE, item.priceInt.toDouble())
                        putString(DIMENSION_84, VALUE_NONE_OTHER)
                        putString(DIMENSION_90, "$PDP.${item.recommendationType}")
                        putString(DIMENSION_56, source.warehouseId)
                    }
                )
            )
        }

    override fun sendEventAddToCart(atcTrackingData: RecommendationCarouselWidgetTrackingATC) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(ADD_TO_CART, atcBundle(atcTrackingData))
    }

    private fun atcBundle(atcTrackingData: RecommendationCarouselWidgetTrackingATC): Bundle {
        val item = atcTrackingData.item

        return bundle {
            putString(EVENT, ADD_TO_CART)
            putString(EVENT_CATEGORY, PDP)
            putString(EVENT_ACTION, ATC_PDP_RECOM_WITH_ATC)
            putString(EVENT_LABEL, anchorProductId)
            putString(TRACKER_ID, ATC_RECOMMENDATION_ITEM_PDP_ATC)
            putString(BUSINESS_UNIT, BUSINESS_UNIT_HOME)
            putString(CURRENT_SITE, CURRENT_SITE_MP)
            putString(PRODUCT_ID, anchorProductId)
            putString(USERID, userId)
            putParcelableArrayList(
                ITEMS,
                arrayListOf(
                    bundle {
                        putString(CATEGORY_ID, VALUE_NONE_OTHER)
                        putString(DIMENSION_40, listName(item))
                        putString(DIMENSION_45, atcTrackingData.cartId)
                        putInt(KEY_INDEX, item.position + 1)
                        putString(ITEM_BRAND, VALUE_NONE_OTHER)
                        putString(ITEM_CATEGORY, item.categoryBreadcrumbs)
                        putString(ITEM_ID, item.productId.toString())
                        putString(ITEM_NAME, item.name)
                        putString(ITEM_VARIANT, VALUE_NONE_OTHER)
                        putDouble(PRICE, item.priceInt.toDouble())
                        putInt(QUANTITY, atcTrackingData.quantity)
                        putString(SHOP_ID, item.shopId.toString())
                        putString(SHOP_NAME, item.shopName)
                        putString(SHOP_TYPE, item.shopType)
                        putString(DIMENSION_90, "$PDP.${item.recommendationType}")
                    }
                )
            )
        }
    }

    override fun sendEventUpdateCart() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, CLICK_HOMEPAGE,
                EVENT_ACTION, ADJUST_QTY_PDP_RECOM_WITH_ATC,
                EVENT_CATEGORY, PDP,
                EVENT_LABEL, anchorProductId,
                TRACKER_ID, ADJUST_QTY_ITEM_PDP_ATC,
                BUSINESS_UNIT, BUSINESS_UNIT_HOME,
                CURRENT_SITE, CURRENT_SITE_MP
            )
        )
    }

    override fun sendEventDeleteCart() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, CLICK_HOMEPAGE,
                EVENT_ACTION, DELETE_PDP_RECOM_WITH_ATC,
                EVENT_CATEGORY, PDP,
                EVENT_LABEL, anchorProductId,
                TRACKER_ID, DELETE_RECOMMENDATION_ITEM_PDP_ATC,
                BUSINESS_UNIT, BUSINESS_UNIT_HOME,
                CURRENT_SITE, CURRENT_SITE_MP
            )
        )
    }

    override fun sendEventSeeAll() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, CLICK_HOMEPAGE,
                EVENT_ACTION, CLICK_PDP_RECOM_SEE_ALL + nonLogin(),
                EVENT_CATEGORY, PDP,
                EVENT_LABEL, anchorProductId,
                TRACKER_ID, SEE_ALL_RECOMMENDATION_PDP_ATC,
                BUSINESS_UNIT, BUSINESS_UNIT_HOME,
                CURRENT_SITE, CURRENT_SITE_MP
            )
        )
    }

    object Factory {
        fun create(
            widget: RecommendationWidget,
            source: RecommendationWidgetSource.PDPAfterATC
        ): RecommendationCarouselWidgetTracking =
            RecommendationCarouselWidgetTrackingPDPATC(widget, source)
    }
}
