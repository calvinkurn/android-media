package com.tokopedia.notifications.analytics

import com.tokopedia.notifications.model.AddToCart
import com.tokopedia.track.TrackApp

object NotificationAnalytics {

    private const val KEY_EVENT = "event"
    private const val KEY_EVENT_CATEGORY = "eventCategory"
    private const val KEY_EVENT_ACTION = "eventAction"
    private const val KEY_EVENT_LABEL = "eventLabel"
    private const val KEY_USER_ID = "userId"
    private const val KEY_ECOMMERCE = "ecommerce"
    private const val KEY_CURRENCY_CODE = "currencyCode"
    private const val KEY_ADD = "add"
    private const val KEY_PRODUCTS = "products"

    fun addToCartClicked(
            campaignId: String,
            userId: String,
            cartId: String,
            addToCart: AddToCart
    ) {
        val product = mapOf(
                "name" to addToCart.productName,
                "id" to addToCart.productId,
                "price" to addToCart.productPrice.toString(),
                "brand" to addToCart.productBrand,
                "category" to "",
                "variant" to addToCart.productVariant,
                "quantity" to addToCart.productQuantity,
                "dimension79" to addToCart.shopId,
                "dimension81" to addToCart.shopType,
                "dimension80" to addToCart.shopName,
                "dimension82" to "",
                "dimension45" to cartId,
                "dimension40" to "/pushnotif"
        )

        sendTracker(mapOf(
                KEY_EVENT to "addToCart",
                KEY_EVENT_CATEGORY to "push notif",
                KEY_EVENT_ACTION to "click on atc",
                KEY_EVENT_LABEL to campaignId,
                KEY_USER_ID to userId,
                KEY_ECOMMERCE to mapOf(
                        KEY_CURRENCY_CODE to "IDR",
                        KEY_ADD to mapOf(
                                KEY_PRODUCTS to listOf(product)
                        )
                )
        ))
    }

    private fun sendTracker(dataMap: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(dataMap)
    }

}