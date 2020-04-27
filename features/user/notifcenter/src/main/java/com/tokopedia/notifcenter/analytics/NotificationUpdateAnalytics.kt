package com.tokopedia.notifcenter.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
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
        // Name
        const val EVENT_NAME = "event"
        const val EVENT_CHECKOUT = "checkout"
        const val EVENT_NAME_CLICK_NOTIF_CENTER = "clickNotifCenter"
        const val NAME_EVENT_PRODUCT_VIEW = "productView"
        const val NAME_EVENT_PRODUCT_CLICK = "productClick"
        const val NAME_EVENT_ATC = "addToCart"
        const val NAME_EVENT_VIEW_NOTIF = "viewNotifCenterIris"

        // Category
        const val EVENT_CATEGORY = "eventCategory"
        const val CATEGORY_NOTIF_CENTER = "notif center"

        // Action
        const val EVENT_ACTION = "eventAction"
        const val ACTION_VIEW_PRODUCT_THUMBNAIL = "view on product thumbnail"
        const val ACTION_CLICK_PRODUCT_THUMBNAIL = "click on product thumbnail"
        const val ACTION_CLICK_ATC_BUTTON = "click on atc button"
        const val ACTION_CLICK_BUY_BUTTON = "click on buy button"
        const val ACTION_VIEW_NOTIF_LIST = "view on notif list"
        const val ACTION_CLICK_LONGER_CONTENT_BUTTON = "click on text (longer content)"
        const val ACTION_VIEW_PRODUCT_CARD = "view on product card impression"

        const val EVENT_ACTION_CLICK_NEWEST_INFO = "click on info terbaru"
        const val EVENT_ACTION_CLICK_NOTIF_LIST = "click on notif list"
        const val EVENT_ACTION_CLICK_NOTIF_TAB = "click on transaction or update"
        const val EVENT_ACTION_CLICK_FILTER_REQ = "click on filter request"
        const val EVENT_ACTION_SCROLL_TO_BOTTOM = "scroll to bottom"
        const val EVENT_ACTION_MARK_ALL_AS_READ = "mark all as read"

        // Label
        const val EVENT_LABEL = "eventLabel"
        const val LABEL_UPDATE_NOTIF_CENTER = "tab update / recomm page from notif center"
        const val LABEL_LOCATION_UPDATE = "tab notif center page"
        const val LABEL_LOCATION = "lonceng"

        // Other
        const val ECOMMERCE = "ecommerce"
    }

    // #11A
    fun trackProductListImpression(notification: NotificationItemViewBean) {
        val eventLabel = notification.getImpressionTrackLabel(LABEL_LOCATION)
        val impressions = arrayListOf<Map<String, Any>>()
        for ((index, product) in notification.products.withIndex()) {
            impressions.add(
                    DataLayer.mapOf(
                            "name", product.name,
                            "id", product.productId,
                            "price", product.price,
                            "brand", "",
                            "category", "",
                            "variant", "",
                            "list", notification.products.map { it.name },
                            "position", index
                    )
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT_NAME, NAME_EVENT_PRODUCT_VIEW,
                        EVENT_CATEGORY, CATEGORY_NOTIF_CENTER,
                        EVENT_ACTION, ACTION_VIEW_PRODUCT_THUMBNAIL,
                        EVENT_LABEL, eventLabel,
                        ECOMMERCE, DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", impressions
                )
                )
        )
    }

    // #11C
    fun trackProductCheckoutBuyClick(notification: NotificationItemViewBean) {
        val eventLabel = notification.getImpressionTrackLabel(LABEL_LOCATION)
        val productTrack = arrayListOf<Map<String, Any>>()
        for ((_, product) in notification.products.withIndex()) {
            productTrack.add(
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
                            "shop_name", product.shop?.name
                    )
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT_NAME, EVENT_CHECKOUT,
                        EVENT_CATEGORY, CATEGORY_NOTIF_CENTER,
                        EVENT_ACTION, ACTION_CLICK_BUY_BUTTON,
                        EVENT_LABEL, eventLabel,
                        ECOMMERCE, DataLayer.mapOf(
                            "checkout", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf(
                                    "step", "1",
                                    "option", "click checkout"
                                ),
                                "products", productTrack
                            )
                        )
                )
        )
    }

    // #11B
    fun trackProductCheckoutCardClick(notification: NotificationItemViewBean) {
        val eventLabel = notification.getImpressionTrackLabel(LABEL_LOCATION)
        val productListName = "/notifcenter"
        val productTrack = arrayListOf<Map<String, Any>>()
        for ((index, product) in notification.products.withIndex()) {
            productTrack.add(
                    DataLayer.mapOf(
                            "name", product.name,
                            "id", product.productId,
                            "price", product.price,
                            "brand", "",
                            "category", "",
                            "variant", "",
                            "list", productListName,
                            "position", index,
                            "attribution", ""
                    )
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT_NAME, NAME_EVENT_PRODUCT_CLICK,
                        EVENT_CATEGORY, CATEGORY_NOTIF_CENTER,
                        EVENT_ACTION, ACTION_CLICK_PRODUCT_THUMBNAIL,
                        EVENT_LABEL, eventLabel,
                        ECOMMERCE, DataLayer.mapOf(
                            "click", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", productListName),
                                "products", productTrack
                            )
                        )
                )
        )
    }

    // #NC1
    fun trackClickNewestInfo() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_NAME_CLICK_NOTIF_CENTER,
                CATEGORY_NOTIF_CENTER,
                EVENT_ACTION_CLICK_NEWEST_INFO,
                ""
        ))
    }

    // #NC2
    fun trackClickNotifList(viewModel: NotificationItemViewBean) {
        val label = viewModel.getImpressionTrackLabel(LABEL_LOCATION)
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_NAME_CLICK_NOTIF_CENTER,
                CATEGORY_NOTIF_CENTER,
                EVENT_ACTION_CLICK_NOTIF_LIST,
                label
        ))
    }
    // #NC3
    fun trackClickFilterRequest(filter: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_NAME_CLICK_NOTIF_CENTER,
                CATEGORY_NOTIF_CENTER,
                EVENT_ACTION_CLICK_FILTER_REQ,
                filter
        ))
    }
    // #NC4
    fun trackScrollBottom(notifSize: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_NAME_CLICK_NOTIF_CENTER,
                CATEGORY_NOTIF_CENTER,
                EVENT_ACTION_SCROLL_TO_BOTTOM,
                notifSize
        ))
    }
    // #NC5
    fun trackMarkAllAsRead(markAllReadCounter: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_NAME_CLICK_NOTIF_CENTER,
                CATEGORY_NOTIF_CENTER,
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
    fun trackAtcOnClick(product: ProductData, atc: DataModel) {
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
                                    "dimension82", atc.cartId.toString(),
                                    "dimension45", ""
                                )
                            )
                    )
                )
            )
        )
    }

    // #NC7
    fun trackAtcToPdpClick(notification: NotificationItemViewBean) {
        val productTrack = arrayListOf<Map<String, Any>>()
        for ((index, product) in notification.products.withIndex()) {
            productTrack.add(DataLayer.mapOf(
                    "name", product.name,
                    "id", product.productId,
                    "price", product.price,
                    "brand", "",
                    "category", "",
                    "variant", "",
                    "list", "/notifcenter",
                    "position", index,
                    "attribution", ""
            ))
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT_NAME, NAME_EVENT_PRODUCT_CLICK,
                EVENT_CATEGORY, CATEGORY_NOTIF_CENTER,
                EVENT_ACTION, ACTION_CLICK_PRODUCT_THUMBNAIL,
                EVENT_LABEL, LABEL_UPDATE_NOTIF_CENTER,
                ECOMMERCE, DataLayer.mapOf(
                    "click", DataLayer.mapOf(
                        "actionField", DataLayer.mapOf("list", "/notifcenter"),
                        "products", productTrack
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
                        CATEGORY_NOTIF_CENTER,
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
                        CATEGORY_NOTIF_CENTER,
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