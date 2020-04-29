package com.tokopedia.notifcenter.analytics

import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.track.TrackApp
import kotlin.collections.mapOf
import com.tokopedia.analyticconstant.DataLayer.mapOf as layerMapOf

class StockHandlerAnalytics {

    fun productCardImpression(element: NotificationItemViewBean, userId: String) {
        val eventName = "productView"
        val eventCategory = "notif center"
        val eventAction = "impression on restock handler product card"
        val eventLabel = "${element.notificationId} - ${element.getAtcProduct()?.productId}"

        val impressions = mapOf(
                "name" to element.getAtcProduct()?.name,
                "id" to element.getAtcProduct()?.productId,
                "price" to element.getAtcProduct()?.price,
                "brand" to "",
                "category" to "",
                "variant" to "",
                "list" to "/notifcenter",
                "position" to "0",
                "dimension79" to element.getAtcProduct()?.shop?.id
        )
        val ecommerce = mapOf(
                KEY_CURRENCY_CODE to VALUE_CURRENCY_CODE,
                KEY_IMPRESSIONS to listOf(impressions)
        )
        val dataTracker = layerMapOf(
                KEY_EVENT_NAME, eventName,
                KEY_EVENT_CATEGORY, eventCategory,
                KEY_EVENT_ACTION, eventAction,
                KEY_EVENT_LABEL, eventLabel,
                KEY_USER_ID, userId,
                KEY_ECOMMERCE, ecommerce
        )
        send(dataTracker)
    }

    fun productCardClicked(element: NotificationItemViewBean, userId: String) {
        val eventName = "productClick"
        val eventCategory = "notif center"
        val eventAction = "click on restock product card"
        val eventLabel = "${element.notificationId} - ${element.getAtcProduct()?.productId}"

        val product = layerMapOf(
                "name", element.getAtcProduct()?.name,
                "id", element.getAtcProduct()?.productId,
                "price", element.getAtcProduct()?.price,
                "brand", "",
                "category", "",
                "variant", "",
                "list", "/notifcenter",
                "position", "0",
                "dimension79", element.getAtcProduct()?.shop?.id?: ""
        )
        val click = mapOf(
                KEY_ACTION_FIELD to mapOf(
                        KEY_LIST to "/notifcenter"
                ),
                KEY_PRODUCTS to listOf(product)
        )
        val dataTracker = layerMapOf(
                KEY_EVENT_NAME, eventName,
                KEY_EVENT_CATEGORY, eventCategory,
                KEY_EVENT_ACTION, eventAction,
                KEY_EVENT_LABEL, eventLabel,
                KEY_USER_ID, userId,
                KEY_ECOMMERCE, mapOf(KEY_CLICK to click)
        )
        send(dataTracker)
    }

    fun stockReminderClicked(element: NotificationItemViewBean, userId: String) {
        val eventName = "clickNotifCenter"
        val eventCategory = "notif center"
        val eventAction = "click on reminder button"
        val eventLabel = "${element.notificationId} - ${element.getAtcProduct()?.productId}"

        val dataTracker = layerMapOf(
                KEY_EVENT_NAME, eventName,
                KEY_EVENT_CATEGORY, eventCategory,
                KEY_EVENT_ACTION, eventAction,
                KEY_EVENT_LABEL, eventLabel,
                KEY_USER_ID, userId,
                KEY_SHOP_ID, element.getAtcProduct()?.shop?.id?: ""
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