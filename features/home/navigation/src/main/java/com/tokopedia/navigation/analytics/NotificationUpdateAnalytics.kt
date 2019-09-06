package com.tokopedia.navigation.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.navigation.domain.pojo.ProductData
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

/**
 * @author : Steven 03/05/19
 */
class NotificationUpdateAnalytics @Inject constructor() {

    companion object {
        val SCREEN_NAME: String = "notif center"
        val EVENT_CATEGORY_NOTIF_CENTER: String = "notif center"
        val EVENT_NAME_CLICK_NOTIF_CENTER: String = "clickNotifCenter"

        val EVENT_ACTION_CLICK_NEWEST_INFO: String = "click on info terbaru"
        val EVENT_ACTION_CLICK_NOTIF_LIST: String = "click on notif list"
        val EVENT_ACTION_CLICK_FILTER_REQ: String = "click on filter request"
        val EVENT_ACTION_SCROLL_TO_BOTTOM: String = "scroll to bottom"
        val EVENT_ACTION_MARK_ALL_AS_READ: String = "mark all as read"

        val EVENT_NAME = "event"
        val EVENT_CATEGORY = "eventCategory"
        val EVENT_ACTION = "eventAction"
        val EVENT_LABEL = "eventLabel"
        val ECOMMERCE = "ecommerce"

        // Name
        val NAME_EVENT_PRODUCT_VIEW = "productView"
        val NAME_EVENT_PRODUCT_CLICK = "productClick"

        // Category
        val CATEGORY_NOTIF_CENTER = "notif center"

        // Action
        val ACTION_VIEW_PRODUCT_THUMBNAIL = "view on product thumbnail"
        val ACTION_CLICK_PRODUCT_THUMBNAIL = "click on product thumbnail"
    }

    // #NC1
    fun trackClickNewestInfo() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_NAME_CLICK_NOTIF_CENTER,
                EVENT_CATEGORY_NOTIF_CENTER,
                EVENT_ACTION_CLICK_NEWEST_INFO,
                ""
        ))
    }

    // #NC2
    fun trackClickNotifList(templateKey: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_NAME_CLICK_NOTIF_CENTER,
                EVENT_CATEGORY_NOTIF_CENTER,
                EVENT_ACTION_CLICK_NOTIF_LIST,
                templateKey
        ))
    }
    // #NC3
    fun trackClickFilterRequest(filter: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_NAME_CLICK_NOTIF_CENTER,
                EVENT_CATEGORY_NOTIF_CENTER,
                EVENT_ACTION_CLICK_FILTER_REQ,
                filter
        ))
    }
    // #NC4
    fun trackScrollBottom(notifSize: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_NAME_CLICK_NOTIF_CENTER,
                EVENT_CATEGORY_NOTIF_CENTER,
                EVENT_ACTION_SCROLL_TO_BOTTOM,
                notifSize
        ))
    }
    // #NC5
    fun trackMarkAllAsRead(markAllReadCounter: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_NAME_CLICK_NOTIF_CENTER,
                EVENT_CATEGORY_NOTIF_CENTER,
                EVENT_ACTION_MARK_ALL_AS_READ,
                markAllReadCounter
        ))
    }

    // #NC8
    fun trackImpressionProductRecommendation(productData: List<ProductData>) {
        val impressions = arrayListOf<Map<String, Any>>()
        for ((index, product) in productData.withIndex()) {
            impressions.add(
                DataLayer.mapOf(
                    "name", product.name,
                    "id", product.productId,
                    "price", product.price,
                    "brand", "",
                    "category", "",
                    "variant", "",
                    "list", "",
                    "position", index
                )
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT_NAME, NAME_EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, CATEGORY_NOTIF_CENTER,
                EVENT_ACTION, ACTION_VIEW_PRODUCT_THUMBNAIL,
                EVENT_LABEL, "",
                ECOMMERCE, DataLayer.mapOf(
                    "currencyCode", "IDR",
                    "impressions", impressions
                )
            )
        )
    }

    // #NC9
    fun trackOnProductThumbnailToPdp(productData: ProductData, position: Int) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT_NAME, NAME_EVENT_PRODUCT_CLICK,
                EVENT_CATEGORY, CATEGORY_NOTIF_CENTER,
                EVENT_ACTION, ACTION_CLICK_PRODUCT_THUMBNAIL,
                EVENT_LABEL, "",
                ECOMMERCE, DataLayer.mapOf(
                    "click", DataLayer.mapOf(
                        "actionField", DataLayer.mapOf("list", "/notifcenter"),
                        "products", DataLayer.listOf(
                            DataLayer.mapOf(
                                "name", productData.name,
                                "id", productData.productId,
                                "price", productData.price,
                                "brand", "",
                                "category", "",
                                "variant", "",
                                "list", "",
                                "position", position,
                                "attribution", ""
                            )
                        )
                    )
                )
            )
        )
    }
}