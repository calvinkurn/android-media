package com.tokopedia.notifcenter.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.notifcenter.domain.pojo.ProductData
import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationItemViewBean
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

/**
 * @author : Steven 03/05/19
 */
class NotificationUpdateAnalytics @Inject constructor() {

    private val seenNotifications = HashSet<String>()
    private val seenProductCards = HashSet<String>()

    companion object {
        val SCREEN_NAME: String = "notif center"
        val EVENT_CATEGORY_NOTIF_CENTER: String = "notif center"
        val EVENT_NAME_CLICK_NOTIF_CENTER: String = "clickNotifCenter"

        val EVENT_ACTION_CLICK_NEWEST_INFO: String = "click on info terbaru"
        val EVENT_ACTION_CLICK_NOTIF_LIST: String = "click on notif list"
        val EVENT_ACTION_CLICK_NOTIF_TAB: String = "click on transaction or update"
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
        val NAME_EVENT_ATC = "addToCart"
        val NAME_EVENT_VIEW_NOTIF = "viewNotifCenterIris"

        // Category
        val CATEGORY_NOTIF_CENTER = "notif center"

        // Action
        val ACTION_VIEW_PRODUCT_THUMBNAIL = "view on product thumbnail"
        val ACTION_CLICK_PRODUCT_THUMBNAIL = "click on product thumbnail"
        val ACTION_CLICK_ATC_BUTTON = "click on atc button"
        val ACTION_VIEW_NOTIF_LIST = "view on notif list"
        val ACTION_CLICK_LONGER_CONTENT_BUTTON = "click on text (longer content)"
        val ACTION_VIEW_PRODUCT_CARD = "view on product card impression"

        // Label
        val LABEL_UPDATE_NOTIF_CENTER = "tab update / recomm page from notif center"
        val LABEL_LOCATION_UPDATE = "tab notif center page"
        val LABEL_LOCATION = "lonceng"
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
    fun trackClickNotifList(viewModel: NotificationItemViewBean) {
        val label = viewModel.getImpressionTrackLabel(LABEL_LOCATION)
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_NAME_CLICK_NOTIF_CENTER,
                EVENT_CATEGORY_NOTIF_CENTER,
                EVENT_ACTION_CLICK_NOTIF_LIST,
                label
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
                EVENT_LABEL, LABEL_UPDATE_NOTIF_CENTER,
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
                EVENT_LABEL, LABEL_UPDATE_NOTIF_CENTER,
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

    // #NC6
    fun trackAtcOnClick(product: ProductData) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT_NAME, NAME_EVENT_ATC,
                EVENT_CATEGORY, CATEGORY_NOTIF_CENTER,
                EVENT_ACTION, ACTION_CLICK_ATC_BUTTON,
                EVENT_LABEL, LABEL_UPDATE_NOTIF_CENTER,
                ECOMMERCE, DataLayer.mapOf(
                    "currencyCode", "IDR",
                    "add", DataLayer.mapOf(
                        "actionField", DataLayer.mapOf("list", "/notifcenter"),
                            "products", DataLayer.listOf(
                                DataLayer.mapOf(
                                    "name", product.name,
                                    "id", product.productId,
                                    "price", product.price,
                                    "brand", "",
                                    "category", "",
                                    "variant", "",
                                    "quantity", "1",
                                    "shop_id", product.shop?.id,
                                    "shop_type", "",
                                    "shop_name", product.shop?.name,
                                    "category_id", "",
                                    "dimension45", ""
                                )
                            )
                    )
                )
            )
        )
    }

    // #NC7
    fun trackAtcToPdpClick(product: ProductData) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT_NAME, NAME_EVENT_PRODUCT_CLICK,
                EVENT_CATEGORY, CATEGORY_NOTIF_CENTER,
                EVENT_ACTION, ACTION_CLICK_PRODUCT_THUMBNAIL,
                EVENT_LABEL, LABEL_UPDATE_NOTIF_CENTER,
                ECOMMERCE, DataLayer.mapOf(
                    "click", DataLayer.mapOf(
                        "actionField", DataLayer.mapOf("list", "/notifcenter"),
                        "products", DataLayer.listOf(
                            DataLayer.mapOf(
                                    "name", product.name,
                                    "id", product.productId,
                                    "price", product.price,
                                    "brand", "",
                                    "category", "",
                                    "variant", "",
                                    "list", "",
                                    "position", "",
                                    "attribution", ""
                            )
                        )
                    )
                )
            )
        )
    }

    fun saveNotificationImpression(notification: NotificationItemViewBean) {
        val notificationId = notification.notificationId
        val isNotAlreadyTracked = seenNotifications.add(notificationId)
        if (isNotAlreadyTracked) {
            trackNotificationImpression(notification)
        }
    }

    private fun trackNotificationImpression(notification: NotificationItemViewBean) {
        val label = notification.getImpressionTrackLabel(LABEL_LOCATION)
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        NAME_EVENT_VIEW_NOTIF,
                        CATEGORY_NOTIF_CENTER,
                        ACTION_VIEW_NOTIF_LIST,
                        label
                )
        )
    }

    fun trackOnClickLongerContentBtn(templateKey: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        EVENT_NAME_CLICK_NOTIF_CENTER,
                        EVENT_CATEGORY_NOTIF_CENTER,
                        ACTION_CLICK_LONGER_CONTENT_BUTTON,
                        templateKey
                )
        )
    }

    fun saveProductCardImpression(notification: NotificationItemViewBean, position: Int) {
        val notificationId = notification.notificationId
        val isNotAlreadyTracked = seenProductCards.add(notificationId)
        if (isNotAlreadyTracked) {
            trackProductCardImpression(notification, position)
        }
    }

    fun trackNotificationCenterTab(tabPosition: Int) {
        val tab = mapOf(
                0 to "transaction",
                1 to "update"
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        EVENT_NAME_CLICK_NOTIF_CENTER,
                        EVENT_CATEGORY_NOTIF_CENTER,
                        EVENT_ACTION_CLICK_NOTIF_TAB,
                        "tab ${tab[tabPosition]}"
                )
        )
    }

    private fun trackProductCardImpression(notification: NotificationItemViewBean, position: Int) {
        val product = notification.getAtcProduct()
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT_NAME, NAME_EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, CATEGORY_NOTIF_CENTER,
                EVENT_ACTION, ACTION_VIEW_PRODUCT_CARD,
                EVENT_LABEL, LABEL_LOCATION_UPDATE,
                ECOMMERCE, DataLayer.mapOf(
                    "currencyCode", "IDR",
                    "impressions", arrayListOf(
                        DataLayer.mapOf(
                            "name", product?.name,
                            "id", product?.productId,
                            "price", product?.price,
                            "brand", "",
                            "category", "",
                            "variant", "",
                            "list", "",
                            "position", position
                        )
                    )
                )
            )
        )
    }
}