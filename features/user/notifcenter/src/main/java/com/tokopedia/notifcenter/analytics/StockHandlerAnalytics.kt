package com.tokopedia.notifcenter.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.notifcenter.data.entity.ProductData
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

    fun productStockListCardImpression(
            notificationId: String,
            product: ProductData,
            userId: String,
            shopId: String,
            productIndex: Int
    ) {
        val eventName = "productView"
        val eventCategory = "notif center"
        val eventAction = "view on restock product list"

        val impressions = hashMapOf(
                "name" to product.name,
                "id" to product.productId,
                "price" to product.price,
                "brand" to "",
                "category" to "",
                "variant" to "",
                "list" to "/notifcenter",
                "position" to productIndex.toString(),
                "dimension79" to shopId
        )
        val ecommerce = mapOf(
                KEY_CURRENCY_CODE to VALUE_CURRENCY_CODE,
                KEY_IMPRESSIONS to listOf(impressions)
        )
        val dataTracker = hashMapOf(
                KEY_EVENT_NAME to eventName,
                KEY_EVENT_CATEGORY to eventCategory,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to notificationId,
                KEY_USER_ID to userId,
                KEY_ECOMMERCE to ecommerce
        )
        send(dataTracker)
    }

    fun productStockListCardClicked(
            notificationId: String,
            productData: ProductData,
            userId: String,
            shopId: String,
            index: Int
    ) {
        val eventName = "productClick"
        val eventCategory = "notif center"
        val eventAction = "click on restock product list"

        val product = hashMapOf(
                "name" to productData.name,
                "id" to productData.productId,
                "price" to productData.price,
                "brand" to "",
                "category" to "",
                "variant" to "",
                "list" to "/notifcenter",
                "position" to index,
                "dimension79" to shopId
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
                KEY_EVENT_LABEL to notificationId,
                KEY_USER_ID to userId,
                KEY_ECOMMERCE to mapOf(KEY_CLICK to click)
        )
        send(dataTracker)
    }

    fun addToCardClicked(
            notificationId: String,
            product: ProductData,
            userId: String,
            cartId: String
    ) {
        val eventName = "addToCart"
        val eventCategory = "notif center"
        val eventAction = "click on restock product ATC"
        val eventLabel = "$notificationId - ${product.productId}"

        val items = mapOf(
                "name" to product.name,
                "id" to product.productId,
                "price" to product.price,
                "brand" to "",
                "category" to "none / other",
                "variant" to "",
                "quantity" to "1",
                "dimension79" to product.shop?.id.toString(),
                "dimension81" to "",
                "dimension80" to product.shop?.name.toString(),
                "dimension82" to "",
                "dimension45" to cartId,
                "dimension40" to "/notifcenter"
        )

        val products = mapOf(
                KEY_PRODUCTS to listOf(items)
        )

        val ecommerce = mapOf(
                KEY_CURRENCY_CODE to VALUE_CURRENCY_CODE,
                KEY_ADD to products
        )

        val dataTracker = DataLayer.mapOf(
                KEY_EVENT_NAME, eventName,
                KEY_EVENT_CATEGORY, eventCategory,
                KEY_EVENT_ACTION, eventAction,
                KEY_EVENT_LABEL, eventLabel,
                KEY_USER_ID, userId,
                KEY_ECOMMERCE, ecommerce
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
        private const val KEY_ADD = "add"

        //const value
        private const val VALUE_CURRENCY_CODE = "IDR"
    }

}