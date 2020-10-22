package com.tokopedia.notifcenter.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.entity.UserInfo
import com.tokopedia.notifcenter.data.viewbean.BaseNotificationItemViewBean
import com.tokopedia.notifcenter.data.viewbean.MultipleProductCardViewBean
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

/**
 * @author : Steven 03/05/19
 */
class NotificationUpdateAnalytics @Inject constructor(): NotificationAnalytics(), NotificationTracker {

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
        const val ACTION_VIEW_MULTI_PRODUCT_THUMBNAIL = "view on multi product thumbnail"
        const val ACTION_VIEW_PRODUCT_THUMBNAIL = "view on product thumbnail"
        const val ACTION_CLICK_PRODUCT_THUMBNAIL = "click on product thumbnail"
        const val ACTION_CLICK_ATC_BUTTON = "click on atc button"
        const val ACTION_CLICK_BUY_BUTTON = "click on buy button"
        const val ACTION_CLICK_MP_BUY_BUTTON = "click on multiple product buy button"
        const val ACTION_VIEW_NOTIF_LIST = "view on notif list"
        const val ACTION_CLICK_LONGER_CONTENT_BUTTON = "click on text (longer content)"
        const val ACTION_VIEW_PRODUCT_CARD = "view on product card impression"

        const val EVENT_ACTION_CLICK_NEWEST_INFO = "click on info terbaru"
        const val EVENT_ACTION_CLICK_NOTIF_LIST = "click on notif list"
        const val EVENT_ACTION_CLICK_NOTIF_TAB = "click on transaction or update"
        const val EVENT_ACTION_CLICK_FILTER_REQ = "click on filter request"
        const val EVENT_ACTION_SCROLL_TO_BOTTOM = "scroll to bottom"
        const val EVENT_ACTION_MARK_ALL_AS_READ = "mark all as read"
        const val EVENT_ACTION_TS_NC = "click on notif setting from notif center"

        // Label
        const val EVENT_LABEL = "eventLabel"
        const val EVENT_USER_ID = "userId"
        const val LABEL_UPDATE_NOTIF_CENTER = "tab update / recomm page from notif center"
        const val LABEL_LOCATION_UPDATE = "tab notif center page"
        const val LABEL_LOCATION = "lonceng"
        const val LABEL_BOTTOM_SHEET_LOCATION = "bottom_sheet"
        const val LABEL_NOTIF_LIST_LOCATION = "notif_list"

        // Other
        const val ECOMMERCE = "ecommerce"
    }

    fun trackTroubleshooterGearClicked(userId: String, shopId: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(mapOf(
                "event" to EVENT_NAME_CLICK_NOTIF_CENTER,
                "eventCategory" to CATEGORY_NOTIF_CENTER,
                "eventAction" to EVENT_ACTION_TS_NC,
                "eventLabel" to "",
                "userId" to userId,
                "shopId" to shopId
        ))
    }

    // #11A
    // multi product
    fun trackMultiProductListImpression(
            userId: String,
            location: String = "notif_list",
            productNumber: Int = 0,
            notification: BaseNotificationItemViewBean
    ) {
        val eventLabel = getImpressionTrackLabel(
                notificationId = notification.notificationId,
                productNumber = productNumber,
                location = location
        )
        trackProductListImpression(
                userId = userId,
                eventAction = ACTION_VIEW_MULTI_PRODUCT_THUMBNAIL,
                eventLabel = eventLabel,
                notification = notification
        )
    }

    fun trackMultiProductListBottomSheetImpression(
            userId: String,
            shopId: String,
            productNumber: Int = 0,
            notificationId: String,
            product: ProductData
    ) {
        val eventLabel = getImpressionTrackLabel(
                notificationId = notificationId,
                productNumber = productNumber,
                location = LABEL_BOTTOM_SHEET_LOCATION
        )
        trackProductListBottomSheetImpression(
                userId = userId,
                eventLabel = eventLabel,
                shopId = shopId,
                index = productNumber.toString(),
                product = product
        )
    }

    //single product
    fun trackProductListImpression(
            userId: String,
            location: String = "notif_list",
            notification: BaseNotificationItemViewBean
    ) {
        val eventLabel = getImpressionTrackLabel(
                location = location,
                templateKey = notification.templateKey,
                notificationId = notification.notificationId,
                productId = notification.products.first().productId
        )
        trackProductListImpression(
                userId = userId,
                eventLabel = eventLabel,
                notification = notification
        )
    }

    private fun trackProductListImpression(
            userId: String,
            eventAction: String = ACTION_VIEW_PRODUCT_THUMBNAIL,
            eventLabel: String,
            notification: BaseNotificationItemViewBean
    ) {
        val impressions = arrayListOf<Map<String, Any>>()
        for ((index, product) in notification.products.withIndex()) {
            impressions.add(
                    mutableMapOf(
                            "name" to product.name,
                            "id" to product.productId,
                            "price" to product.price,
                            "brand" to "",
                            "category" to "",
                            "variant" to "",
                            "list" to "/notifcenter",
                            "position" to index,
                            "dimension79" to notification.userInfo.shopId
                    )
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT_NAME, NAME_EVENT_PRODUCT_VIEW,
                        EVENT_CATEGORY, CATEGORY_NOTIF_CENTER,
                        EVENT_ACTION, eventAction,
                        EVENT_LABEL, eventLabel,
                        EVENT_USER_ID, userId,
                        ECOMMERCE, DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "impressions", impressions
                        )
                )
        )
    }

    private fun trackProductListBottomSheetImpression(
            userId: String,
            eventLabel: String,
            index: String,
            shopId: String,
            product: ProductData
    ) {
        val impressions = arrayListOf<Map<String, Any>>()
        impressions.add(
                mutableMapOf(
                        "name" to product.name,
                        "id" to product.productId,
                        "price" to product.price,
                        "brand" to "",
                        "category" to "",
                        "variant" to "",
                        "list" to "/notifcenter",
                        "position" to index,
                        "dimension79" to shopId
                )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT_NAME, NAME_EVENT_PRODUCT_VIEW,
                        EVENT_CATEGORY, CATEGORY_NOTIF_CENTER,
                        EVENT_ACTION, ACTION_VIEW_PRODUCT_THUMBNAIL,
                        EVENT_LABEL, eventLabel,
                        EVENT_USER_ID, userId,
                        ECOMMERCE, DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", impressions
                )
                )
        )
    }

    // #11C
    fun trackProductCheckoutBuyClick(
            location: String = LABEL_LOCATION,
            notification: NotificationItemViewBean
    ) {
        trackProductCheckoutBuyClick(
                location = location,
                templateKey = notification.templateKey,
                notificationId = notification.notificationId,
                product = notification.getAtcProduct()
        )
    }

    fun trackProductCheckoutBuyClick(
            location: String = LABEL_LOCATION,
            notification: MultipleProductCardViewBean
    ) {
        trackProductCheckoutBuyClick(
                location = location,
                templateKey = notification.templateKey,
                notificationId = notification.notificationId,
                product = notification.product
        )
    }

    private fun trackProductCheckoutBuyClick(
            location: String,
            templateKey: String,
            notificationId: String,
            product: ProductData?
    ) {
        val eventLabel = getImpressionTrackLabel(
                location,
                templateKey,
                notificationId,
                product?.productId
        )
        val productTrack = arrayListOf<Map<String, Any>>()
        productTrack.add(
                DataLayer.mapOf(
                        "name", product?.name,
                        "id", product?.productId,
                        "price", product?.price,
                        "brand", "",
                        "category", "",
                        "variant", "",
                        "quantity", "1",
                        "shop_id", product?.shop?.id,
                        "shop_type", "",
                        "shop_name", product?.shop?.name
                )
        )
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
    fun trackSingleProductCheckoutCardClick(
            location: String = LABEL_LOCATION,
            notification: NotificationItemViewBean
    ) {
        val eventLabel = getImpressionTrackLabel(
                location,
                notification.templateKey,
                notification.notificationId,
                notification.getAtcProduct()?.productId
        )
        trackProductCheckoutCardClick(
                eventLabel = eventLabel,
                eventAction = ACTION_CLICK_PRODUCT_THUMBNAIL,
                product = notification.getAtcProduct()
        )
    }

    fun trackMultiProductCheckoutCardClick(
            eventLocation: String = "notif_list",
            productNumber: Int = 0,
            notification: NotificationItemViewBean
    ) {
        val eventLabel = getImpressionTrackLabel(
                notificationId = notification.notificationId,
                productNumber = productNumber,
                location = eventLocation
        )
        trackProductCheckoutCardClick(
                eventLabel = eventLabel,
                eventAction = "click on multiple product list",
                product = notification.getAtcProduct()
        )
    }

    fun trackMultiProductCheckoutCardClick(
            eventLocation: String = "notif_list",
            productNumber: Int = 0,
            notification: MultipleProductCardViewBean
    ) {
        val eventLabel = getImpressionTrackLabel(
                notificationId = notification.notificationId,
                productNumber = productNumber,
                location = eventLocation
        )
        trackProductCheckoutCardClick(
                eventLabel = eventLabel,
                eventAction = "click on multiple product list",
                product = notification.product
        )
    }

    private fun trackProductCheckoutCardClick(
            eventLabel: String,
            eventAction: String,
            product: ProductData?
    ) {
        val productListName = "/notifcenter"
        val productTrack = arrayListOf<Map<String, Any>>()
        productTrack.add(
                DataLayer.mapOf(
                        "name", product?.name,
                        "id", product?.productId,
                        "price", product?.price,
                        "brand", "",
                        "category", "",
                        "variant", "",
                        "list", productListName,
                        "position", "0",
                        "attribution", ""
                )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT_NAME, NAME_EVENT_PRODUCT_CLICK,
                        EVENT_CATEGORY, CATEGORY_NOTIF_CENTER,
                        EVENT_ACTION, eventAction,
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
    override fun trackNotificationClick(notification: NotificationItemViewBean) {
        val label = getImpressionWithoutLocationLabel(
                notification.templateKey,
                notification.notificationId,
                notification.getAtcProduct()?.productId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_NAME_CLICK_NOTIF_CENTER,
                CATEGORY_NOTIF_CENTER,
                EVENT_ACTION_CLICK_NOTIF_LIST,
                label
        ))
    }
    // #NC3
    override fun trackClickFilterRequest(filter: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_NAME_CLICK_NOTIF_CENTER,
                CATEGORY_NOTIF_CENTER,
                EVENT_ACTION_CLICK_FILTER_REQ,
                filter
        ))
    }

    // #NC4
    override fun trackScrollBottom(notificationSize: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_NAME_CLICK_NOTIF_CENTER,
                CATEGORY_NOTIF_CENTER,
                EVENT_ACTION_SCROLL_TO_BOTTOM,
                notificationSize
        ))
    }
    // #NC5
    override fun trackMarkAllAsRead(markAllReadCounter: String) {
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

    fun trackAtcOnSingleProductClick(
            eventLocation: String = LABEL_LOCATION,
            notification: NotificationItemViewBean,
            cartId: String
    ) {
        val eventLabel = getImpressionTrackLabel(
                location = eventLocation,
                templateKey = notification.templateKey,
                notificationId = notification.notificationId,
                productId = notification.getAtcProduct()?.productId
        )
        trackAtcOnProductClick(
                ACTION_CLICK_BUY_BUTTON,
                eventLabel,
                notification.getAtcProduct(),
                cartId
        )
    }

    fun trackAtcOnMultiProductClick(
            eventLocation: String = "notif_list",
            productNumber: Int = 0,
            notification: MultipleProductCardViewBean,
            cartId: String
    ) {
        val eventLabel = getImpressionTrackLabel(
                notificationId = notification.notificationId,
                productNumber = productNumber,
                location = eventLocation
        )
        trackAtcOnProductClick(
                ACTION_CLICK_MP_BUY_BUTTON,
                eventLabel,
                notification.product,
                cartId
        )
    }

    // #NC6
    private fun trackAtcOnProductClick(
            eventAction: String,
            eventLabel: String,
            product: ProductData?,
            cartId: String = ""
    ) {
        val products = DataLayer.mapOf(
                "name", product?.name,
                "id", product?.productId,
                "price", product?.price,
                "brand", "",
                "category", "",
                "variant", "",
                "quantity", "1",
                "dimension79", product?.shop?.id.toString(),
                "dimension81", "", //shop type
                "dimension80", product?.shop?.name, //shop name
                "dimension82", "", //category child id
                "dimension45", cartId, //cart_id
                "dimension40", "/notifcenter" //list name
        )

        val ecommerce = DataLayer.mapOf(
                "currencyCode", "IDR",
                "add", DataLayer.mapOf("products", listOf(products))
        )

        val eventsLayer = DataLayer.mapOf(
                EVENT_NAME, NAME_EVENT_ATC,
                EVENT_CATEGORY, CATEGORY_NOTIF_CENTER,
                EVENT_ACTION, eventAction,
                EVENT_LABEL, eventLabel,
                ECOMMERCE, ecommerce
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                eventsLayer
        )
    }

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
                                    "dimension82", atc.cartId,
                                    "dimension45", ""
                                )
                            )
                    )
                )
            )
        )
    }

    fun trackAtcOnClick(
            templateKey: String,
            notificationId: String,
            product: ProductData,
            atc: DataModel
    ) {
        val eventLabel = getImpressionTrackLabel(
                location = LABEL_NOTIF_LIST_LOCATION,
                templateKey = templateKey,
                notificationId = notificationId
        )

        val products = DataLayer.mapOf(
                "products", listOf(DataLayer.mapOf(
                        "name", product.name,
                        "id", product.productId,
                        "price", product.price,
                        "brand", "",
                        "category", "",
                        "variant", "",
                        "quantity", "1",
                        "dimension79", product.shop?.id.toString(),
                        "dimension81", "",
                        "dimension80", product.shop?.name.toString(),
                        "dimension82", "",
                        "dimension45", atc.cartId,
                        "dimension40", "/notifcenter"
                ))
        )

        val ecommerce = DataLayer.mapOf(
                "currencyCode", "IDR",
                "add", products
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                EVENT_NAME, NAME_EVENT_ATC,
                EVENT_CATEGORY, CATEGORY_NOTIF_CENTER,
                EVENT_ACTION, ACTION_CLICK_ATC_BUTTON,
                EVENT_LABEL, eventLabel,
                ECOMMERCE, ecommerce
        ))
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

    override fun saveNotificationImpression(notification: NotificationItemViewBean) {
        val notificationId = notification.notificationId
        val isNotAlreadyTracked = seenNotifications.add(notificationId)
        if (isNotAlreadyTracked) {
            trackNotificationImpression(notification)
        }
    }

    private fun trackNotificationImpression(notification: NotificationItemViewBean) {
        val label = getImpressionWithoutLocationLabel(
                notification.templateKey,
                notification.notificationId,
                notification.getAtcProduct()?.productId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        NAME_EVENT_VIEW_NOTIF,
                        CATEGORY_NOTIF_CENTER,
                        ACTION_VIEW_NOTIF_LIST,
                        label
                )
        )
    }

    fun trackOnClickLongerContentBtn(templateKey: String, notificationId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        EVENT_NAME_CLICK_NOTIF_CENTER,
                        CATEGORY_NOTIF_CENTER,
                        ACTION_CLICK_LONGER_CONTENT_BUTTON,
                        "$templateKey - $notificationId"
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

    override fun sendTrackTransactionTab(parent: String, child: String) {
        //no-op
    }

}