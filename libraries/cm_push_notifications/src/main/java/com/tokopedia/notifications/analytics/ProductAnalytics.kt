package com.tokopedia.notifications.analytics

import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.ProductInfo
import com.tokopedia.track.TrackApp

object ProductAnalytics {

    private const val KEY_EVENT = "event"
    private const val KEY_EVENT_CATEGORY = "eventCategory"
    private const val KEY_EVENT_ACTION = "eventAction"
    private const val KEY_EVENT_LABEL = "eventLabel"
    private const val KEY_USER_ID = "userId"
    private const val KEY_SHOP_ID = "shopId"
    private const val KEY_ECOMMERCE = "ecommerce"
    private const val KEY_CURRENCY_CODE = "currencyCode"
    private const val KEY_IMPRESSIONS = "impressions"
    private const val KEY_CLICK = "click"
    private const val KEY_ADD = "add"
    private const val KEY_PRODUCTS = "products"
    private const val KEY_ACTION_FIELD = "actionField"

    private const val KEY_ID = "id"
    private const val KEY_NAME = "name"
    private const val KEY_PRICE = "price"
    private const val KEY_BRAND = "brand"
    private const val KEY_VARIANT = "variant"
    private const val KEY_LIST = "list"
    private const val KEY_POSITION = "position"
    private const val KEY_CATEGORY = "category"
    private const val KEY_QUANTITY = "quantity"
    private const val KEY_DIMENSION79 = "dimension79"
    private const val KEY_DIMENSION81 = "dimension81"
    private const val KEY_DIMENSION80 = "dimension80"
    private const val KEY_DIMENSION82 = "dimension82"
    private const val KEY_DIMENSION45 = "dimension45"
    private const val KEY_DIMENSION40 = "dimension40"
    private const val KEY_DIMENSION_79 = "dimension79"

    private const val EVENT_ATC = "addToCart"
    private const val EVENT_VIEW = "viewPushNotifIris"
    private const val EVENT_CLICK = "clickPushNotif"
    private const val EVENT_PRODUCT_VIEW = "productView"
    private const val EVENT_PRODUCT_CLICK = "productClick"
    private const val EVENT_CHECKOUT = "checkout"
    private const val CATEGORY = "push notif"
    private const val ACTION_VIEW = "view on push notif"
    private const val ACTION_VIEW_EXPAND = "view on expanded push"
    private const val ACTION_CLICK = "click on push body"
    private const val ACTION_CLICK_EXPAND = "click on expanded push body"
    private const val ACTION_CLICK_EXPAND_OCC = "click on expanded push occ"
    private const val ACTION_CLICK_EXPAND_ATC = "click on expanded push atc"
    private const val ACTION_CLICK_EXPAND_PDP = "click on expanded push product card"
    private const val IDR = "IDR"
    private const val LIST = "/pushnotif"
    private const val POSITION = "1"

    fun impression(
            element: BaseNotificationModel
    ) {
        sendTracker(mapOf(
                KEY_EVENT to EVENT_VIEW,
                KEY_EVENT_CATEGORY to CATEGORY,
                KEY_EVENT_ACTION to ACTION_VIEW,
                KEY_EVENT_LABEL to element.transactionId.toString(),
                KEY_USER_ID to element.userId.toString(),
                KEY_SHOP_ID to element.shopId.toString()
        ))
    }

    fun clickBody(
            element: BaseNotificationModel?
    ) {
        sendTracker(mapOf(
                KEY_EVENT to EVENT_CLICK,
                KEY_EVENT_CATEGORY to CATEGORY,
                KEY_EVENT_ACTION to ACTION_CLICK_EXPAND,
                KEY_EVENT_LABEL to element?.transactionId.toString(),
                KEY_USER_ID to element?.userId.toString(),
                KEY_SHOP_ID to element?.shopId.toString()
        ))
    }

    fun impressionExpanded(
            element: BaseNotificationModel,
            product: ProductInfo
    ) {
        val impressions = mapOf(
                KEY_ID to product.element_id.toString(),
                KEY_NAME to product.productTitle,
                KEY_PRICE to product.productCurrentPrice,
                KEY_BRAND to "",
                KEY_VARIANT to "",
                KEY_LIST to LIST,
                KEY_POSITION to POSITION,
                KEY_CATEGORY to "",
                KEY_DIMENSION_79 to element.shopId.toString()
        )

        val ecommerce = mapOf(
                KEY_CURRENCY_CODE to IDR,
                KEY_IMPRESSIONS to listOf(impressions)
        )

        sendTracker(mapOf(
                KEY_EVENT to EVENT_PRODUCT_VIEW,
                KEY_EVENT_CATEGORY to CATEGORY,
                KEY_EVENT_ACTION to ACTION_VIEW_EXPAND,
                KEY_EVENT_LABEL to element.transactionId.toString(),
                KEY_USER_ID to element.userId.toString(),
                KEY_ECOMMERCE to ecommerce
        ))
    }

    fun clickProductCard(
            element: BaseNotificationModel,
            product: ProductInfo
    ) {
        val productElement = mapOf(
                KEY_ID to product.element_id.toString(),
                KEY_NAME to product.productTitle,
                KEY_PRICE to product.productCurrentPrice,
                KEY_BRAND to "",
                KEY_VARIANT to "",
                KEY_LIST to LIST,
                KEY_POSITION to POSITION,
                KEY_CATEGORY to "",
                KEY_DIMENSION_79 to element.shopId.toString()
        )

        val ecommerce = mapOf(
                KEY_CLICK to mapOf(
                        KEY_ACTION_FIELD to mapOf(
                                KEY_LIST to LIST,
                                KEY_PRODUCTS to listOf(productElement)
                        )
                )
        )

        sendTracker(mapOf(
                KEY_EVENT to EVENT_PRODUCT_CLICK,
                KEY_EVENT_CATEGORY to CATEGORY,
                KEY_EVENT_ACTION to ACTION_CLICK_EXPAND_PDP,
                KEY_EVENT_LABEL to element.transactionId.toString(),
                KEY_USER_ID to element.userId.toString(),
                KEY_ECOMMERCE to ecommerce
        ))
    }

    fun occCLickButton(
            element: BaseNotificationModel,
            productElement: List<ProductInfo>
    ) {
        val products = mutableListOf<Map<String, String>>()
        productElement.forEach {
            products.add(mapOf(
                    KEY_ID to it.element_id.toString(),
                    KEY_NAME to it.productTitle,
                    KEY_PRICE to it.productCurrentPrice,
                    KEY_BRAND to "",
                    KEY_VARIANT to "",
                    KEY_LIST to LIST,
                    KEY_CATEGORY to "",
                    KEY_QUANTITY to POSITION,
                    KEY_DIMENSION79 to element.shopId.toString(),
                    KEY_DIMENSION81 to "",
                    KEY_DIMENSION80 to "",
                    KEY_DIMENSION82 to "",
                    KEY_DIMENSION45 to "",
                    KEY_DIMENSION40 to LIST
            ))
        }

        val ecommerce = mapOf(
                EVENT_CHECKOUT to mapOf(
                        KEY_ACTION_FIELD to mapOf(
                                KEY_LIST to LIST,
                                KEY_PRODUCTS to products
                        )
                )
        )

        sendTracker(mapOf(
                KEY_EVENT to EVENT_CHECKOUT,
                KEY_EVENT_CATEGORY to CATEGORY,
                KEY_EVENT_ACTION to ACTION_CLICK_EXPAND_OCC,
                KEY_EVENT_LABEL to element.transactionId.toString(),
                KEY_USER_ID to element.userId.toString(),
                KEY_ECOMMERCE to ecommerce
        ))
    }

    fun atcCLickButton(
            element: BaseNotificationModel,
            productElement: List<ProductInfo>
    ) {
        val products = mutableListOf<Map<String, String>>()
        productElement.forEach {
            products.add(mapOf(
                    KEY_ID to it.element_id.toString(),
                    KEY_NAME to it.productTitle,
                    KEY_PRICE to it.productCurrentPrice,
                    KEY_BRAND to "",
                    KEY_VARIANT to "",
                    KEY_LIST to LIST,
                    KEY_CATEGORY to "",
                    KEY_QUANTITY to POSITION,
                    KEY_DIMENSION79 to element.shopId.toString(),
                    KEY_DIMENSION81 to "",
                    KEY_DIMENSION80 to "",
                    KEY_DIMENSION82 to "",
                    KEY_DIMENSION45 to "",
                    KEY_DIMENSION40 to LIST
            ))
        }

        val ecommerce = mapOf(
                KEY_CURRENCY_CODE to IDR,
                KEY_ADD to mapOf(
                        KEY_PRODUCTS to products
                )
        )

        sendTracker(mapOf(
                KEY_EVENT to EVENT_ATC,
                KEY_EVENT_CATEGORY to CATEGORY,
                KEY_EVENT_ACTION to ACTION_CLICK_EXPAND_ATC,
                KEY_EVENT_LABEL to element.transactionId.toString(),
                KEY_ECOMMERCE to ecommerce
        ))
    }

    private fun sendTracker(dataMap: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(dataMap)
    }

}