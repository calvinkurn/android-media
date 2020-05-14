package com.tokopedia.notifcenter.analytics

import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.track.TrackApp

class StockHandlerAnalytics {

    fun productCardImpression(element: NotificationItemViewBean, userId: String) {
        val eventName = "productView"
        val eventCategory = "notif center"
        val eventAction = "impression on restock handler product card"
        val eventLabel = "${element.notificationId} - ${element.getAtcProduct()?.productId}"

        val impressions = hashMapOf(
                "name" to element.getAtcProduct()?.name.toString(),
                "id" to element.getAtcProduct()?.productId.toString(),
                "price" to element.getAtcProduct()?.price.toString(),
                "brand" to "",
                "category" to "",
                "variant" to "",
                "list" to "/notifcenter",
                "position" to "0",
                "dimension79" to element.getAtcProduct()?.shop?.id.toString()
        )
        val ecommerce = mapOf(
                KEY_CURRENCY_CODE to VALUE_CURRENCY_CODE,
                KEY_IMPRESSIONS to listOf(impressions)
        )
        val dataTracker = hashMapOf(
                KEY_EVENT_NAME to eventName,
                KEY_EVENT_CATEGORY to eventCategory,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_USER_ID to userId,
                KEY_ECOMMERCE to ecommerce
        )
        send(dataTracker)
    }

    fun productCardClicked(element: NotificationItemViewBean, userId: String) {
        val eventName = "productClick"
        val eventCategory = "notif center"
        val eventAction = "click on restock product card"
        val eventLabel = "${element.notificationId} - ${element.getAtcProduct()?.productId}"

        val product = hashMapOf(
                "name" to element.getAtcProduct()?.name.toString(),
                "id" to element.getAtcProduct()?.productId.toString(),
                "price" to element.getAtcProduct()?.price.toString(),
                "brand" to "",
                "category" to "",
                "variant" to "",
                "list" to "/notifcenter",
                "position" to "0",
                "dimension79" to element.getAtcProduct()?.shop?.id.toString()
        )
        val click = mapOf(
                KEY_ACTION_FIELD to mapOf(
                        KEY_LIST to "/notifcenter"
                ),
                KEY_PRODUCTS to listOf(product)
        )
        val dataTracker = hashMapOf(
                KEY_EVENT_NAME to eventName,
                KEY_EVENT_CATEGORY to eventCategory,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_USER_ID to userId,
                KEY_ECOMMERCE to mapOf(KEY_CLICK to click)
        )
        send(dataTracker)
    }

    fun stockReminderClicked(element: NotificationItemViewBean, userId: String) {
        val eventName = "clickNotifCenter"
        val eventCategory = "notif center"
        val eventAction = "click on reminder button"
        val eventLabel = "${element.notificationId} - ${element.getAtcProduct()?.productId}"

        val dataTracker = hashMapOf(
                KEY_EVENT_NAME to eventName,
                KEY_EVENT_CATEGORY to eventCategory,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_USER_ID to userId,
                KEY_SHOP_ID to element.getAtcProduct()?.shop?.id.toString()
        )
        send(dataTracker)
    }

    fun addToCardClicked(element: NotificationItemViewBean, userId: String, cartId: String) {
        val eventName = "add_to_cart"
        val eventCategory = "notif center"
        val eventAction = "click on restock product ATC"
        val eventLabel = "${element.notificationId} - ${element.getAtcProduct()?.productId}"

        val items = mapOf(
                "item_name" to element.getAtcProduct()?.name.toString(),
                "item_id" to element.getAtcProduct()?.productId.toString(),
                "price" to element.getAtcProduct()?.price.toString(),
                "item_brand" to "",
                "item_category" to "none / other",
                "item_variant" to "",
                "list" to "/notifcenter",
                "quantity" to "1",
                "dimension69" to element.getAtcProduct()?.shop?.id.toString(),
                "dimension71" to "",
                "dimension70" to element.getAtcProduct()?.shop?.name.toString(),
                "category_id" to "",
                "dimension42" to cartId,
                "dimension39" to "/notifcenter"
        )

        val dataTracker = mapOf(
                KEY_EVENT_NAME to eventName,
                KEY_EVENT_CATEGORY to eventCategory,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_USER_ID to userId,
                KEY_EVENT_ITEMS to listOf(items)
        )
        send(dataTracker)
    }

    fun swipeRestockProductList(
            notificationId: String,
            productId: String,
            userId: String,
            shopId: String
    ) {
        val eventName = "clickNotifCenter"
        val eventCategory = "notif center"
        val eventAction = "swipe on restock product list"
        val eventLabel = "$notificationId - $productId"

        val dataTracker = hashMapOf(
                KEY_EVENT_NAME to eventName,
                KEY_EVENT_CATEGORY to eventCategory,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_USER_ID to userId,
                KEY_SHOP_ID to shopId
        )
        send(dataTracker)
    }

    private fun send(data: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(data)
    }

    companion object {
        //common key event
        private const val KEY_EVENT_NAME = "event"
        private const val KEY_EVENT_CATEGORY = "eventCategory"
        private const val KEY_EVENT_ACTION = "eventAction"
        private const val KEY_EVENT_LABEL = "eventLabel"
        private const val KEY_EVENT_ITEMS = "items"

        //key event
        private const val KEY_USER_ID = "userId"
        private const val KEY_ECOMMERCE = "ecommerce"
        private const val KEY_CURRENCY_CODE = "currencyCode"
        private const val KEY_IMPRESSIONS = "impressions"
        private const val KEY_CLICK = "click"
        private const val KEY_ACTION_FIELD = "actionField"
        private const val KEY_LIST = "list"
        private const val KEY_PRODUCTS = "products"
        private const val KEY_SHOP_ID = "shopId"

        //const value
        private const val VALUE_CURRENCY_CODE = "IDR"
    }

}