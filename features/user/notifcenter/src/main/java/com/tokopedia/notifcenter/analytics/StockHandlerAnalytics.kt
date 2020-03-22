package com.tokopedia.notifcenter.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.track.TrackApp

class StockHandlerAnalytics {

    fun productCardImpression(element: NotificationItemViewBean, userId: String) {
        val eventName = "productView"
        val eventCategory = "notif center"
        val eventAction = "impression on restock handler product card"
        val eventLabel = "${element.notificationId} - ${element.getAtcProduct()?.productId}"

        val impressions = DataLayer.mapOf(
                "name", element.getAtcProduct()?.name,
                "id", element.getAtcProduct()?.productId,
                "price", element.getAtcProduct()?.price,
                "brand", "",
                "category", "",
                "variant", element.getAtcProduct()?.variant,
                "list", "/notifcenter",
                "position", "0",
                "dimension79", element.userInfo.shopId
        )

        val ecommerce = DataLayer.mapOf(
                KEY_CURRENCY_CODE, VALUE_CURRENCY_CODE,
                KEY_IMPRESSIONS, arrayOf(impressions)
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        KEY_EVENT_NAME, eventName,
                        KEY_EVENT_CATEGORY, eventCategory,
                        KEY_EVENT_ACTION, eventAction,
                        KEY_EVENT_LABEL, eventLabel,
                        KEY_USER_ID, userId,
                        KEY_ECOMMERCE, ecommerce
                )
        )
    }

    fun productCardClicked(element: NotificationItemViewBean, userId: String) {
        val eventName = "productClick"
        val eventCategory = "notif center"
        val eventAction = "click on restock product card"
        val eventLabel = "${element.notificationId} - ${element.getAtcProduct()?.productId}"

        val product = DataLayer.mapOf(
                "name", element.getAtcProduct()?.name,
                "id", element.getAtcProduct()?.productId,
                "price", element.getAtcProduct()?.price,
                "brand", "",
                "category", "",
                "variant", element.getAtcProduct()?.variant,
                "list", "/notifcenter",
                "position", "0",
                "dimension79", element.userInfo.shopId
        )

        val click = DataLayer.mapOf(
                KEY_ACTION_FIELD, DataLayer.mapOf(KEY_LIST, "/notifcenter"),
                KEY_PRODUCTS, listOf(product)
        )

        val ecommerce = DataLayer.mapOf(
                KEY_CLICK, click
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        KEY_EVENT_NAME, eventName,
                        KEY_EVENT_CATEGORY, eventCategory,
                        KEY_EVENT_ACTION, eventAction,
                        KEY_EVENT_LABEL, eventLabel,
                        KEY_USER_ID, userId,
                        KEY_ECOMMERCE, ecommerce
                )
        )
    }

    fun stockReminderClicked(element: NotificationItemViewBean, userId: String) {
        val eventName = "clickNotifCenter"
        val eventCategory = "notif center"
        val eventAction = "click on reminder button"
        val eventLabel = "${element.notificationId} - ${element.getAtcProduct()?.productId}"

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        KEY_EVENT_NAME, eventName,
                        KEY_EVENT_CATEGORY, eventCategory,
                        KEY_EVENT_ACTION, eventAction,
                        KEY_EVENT_LABEL, eventLabel,
                        KEY_USER_ID, userId,
                        KEY_SHOP_ID, element.userInfo.shopId
                )
        )
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