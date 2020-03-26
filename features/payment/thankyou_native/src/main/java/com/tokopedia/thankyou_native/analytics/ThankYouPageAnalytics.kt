package com.tokopedia.thankyou_native.analytics

import com.tokopedia.thankyou_native.domain.model.PurchaseItem
import com.tokopedia.thankyou_native.domain.model.ShopOrder
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

class ThankYouPageAnalytics {

    private val IDR = "IDR"

    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm

    fun sendThankYouPageData(thanksPageData: ThanksPageData) {
        thanksPageData.shopOrder.forEach { shopOrder ->
            val data = getParentTrackingNode(thanksPageData)
            data[ParentTrackingKey.KEY_SHOP_ID] = shopOrder.storeId
            data[ParentTrackingKey.KEY_SHOP_TYPE] = shopOrder.storeType
            data[ParentTrackingKey.KEY_LOGISTIC_TYPE] = shopOrder.logisticType
            data[ParentTrackingKey.KEY_ECOMMERCE] = getEnhancedECommerceNode(shopOrder)
            analyticTracker.sendEnhanceEcommerceEvent(data)
        }

    }

    private fun getParentTrackingNode(thanksPageData: ThanksPageData): MutableMap<String, Any> {
        return mutableMapOf(
                ParentTrackingKey.KEY_EVENT to ParentTrackingKeyValue.KEY_EVENT_VALUE,
                ParentTrackingKey.KEY_EVENT_CATEGORY to ParentTrackingKeyValue.KEY_EVENT_CATEGORY_VALUE,
                ParentTrackingKey.KEY_EVENT_ACTION to "",
                ParentTrackingKey.KEY_EVENT_LABEL to "",
                ParentTrackingKey.KEY_PAYMENT_ID to thanksPageData.paymentID,
                ParentTrackingKey.KEY_PAYMENT_STATUS to thanksPageData.paymentStatus,
                ParentTrackingKey.KEY_PAYMENT_TYPE to thanksPageData.paymentType
        )
    }

    private fun getEnhancedECommerceNode(orderedItem: ShopOrder): Map<String, Any> {
        return mapOf(ECommerceNodeTrackingKey.KEY_CURRENCY_CODE to IDR,
                ECommerceNodeTrackingKey.KEY_PURCHASE to getPurchasedEventNodeByShop(orderedItem))
    }

    private fun getPurchasedEventNodeByShop(orderedItem: ShopOrder): Map<String, Any> {
        return mapOf(PurchaseNodeTrackingKey.KEY_ACTION_FIELD to getActionFieldNode(orderedItem),
                PurchaseNodeTrackingKey.KEY_PRODUCTS to getECommerceProductNodeList(orderedItem.purchaseItemList))
    }

    private fun getActionFieldNode(orderedItem: ShopOrder): Map<String, Any?> {
        return mapOf(
                ActionFieldNodeTrackingKey.KEY_ID to orderedItem.orderId,
                ActionFieldNodeTrackingKey.KEY_AFFILIATION to orderedItem.storeName,
                ActionFieldNodeTrackingKey.KEY_REVENUE to null,//todo not clear...
                ActionFieldNodeTrackingKey.KEY_TAX to orderedItem.tax,
                ActionFieldNodeTrackingKey.KEY_SHIPPING to orderedItem.shippingAmount,
                ActionFieldNodeTrackingKey.KEY_COUPON to orderedItem.coupon
        )
    }

    private fun getECommerceProductNodeList(purchaseItemList: ArrayList<PurchaseItem>): ArrayList<Map<String, Any?>> {
        val productNodeList: ArrayList<Map<String, Any?>> = arrayListOf()
        purchaseItemList.forEach { item ->
            val productNodeMap = HashMap<String, Any?>()
            productNodeMap[ProductNodeTrackingKey.KEY_NAME] = item.productName
            productNodeMap[ProductNodeTrackingKey.KEY_ID] = item.productId
            productNodeMap[ProductNodeTrackingKey.KEY_PRICE] = item.price
            productNodeMap[ProductNodeTrackingKey.KEY_BRAND] = item.productBrand
            productNodeMap[ProductNodeTrackingKey.KEY_CATEGORY] = item.category
            productNodeMap[ProductNodeTrackingKey.KEY_VARIANT] = item.variant
            productNodeMap[ProductNodeTrackingKey.KEY_QUANTITY] = item.quantity
            productNodeList.add(productNodeMap)
        }
        return productNodeList
    }

}

object ParentTrackingKeyValue {
    val KEY_EVENT_VALUE = "transaction"
    val KEY_EVENT_CATEGORY_VALUE = "order complete"
}

object ParentTrackingKey {
    val KEY_EVENT = "event"
    val KEY_EVENT_CATEGORY = "eventCategory"
    val KEY_EVENT_ACTION = "eventAction"
    val KEY_EVENT_LABEL = "eventLabel"
    val KEY_PAYMENT_ID = "payment_id"
    val KEY_PAYMENT_STATUS = "payment_status"
    val KEY_PAYMENT_TYPE = "payment_type"

    val KEY_SHOP_ID = "shopId"
    val KEY_SHOP_TYPE = "shopType"
    val KEY_LOGISTIC_TYPE = "logistic_type"
    val KEY_ECOMMERCE = "ecommerce"
}

object ECommerceNodeTrackingKey {
    val KEY_CURRENCY_CODE = "currencyCode"
    val KEY_PURCHASE = "purchase"
}

object PurchaseNodeTrackingKey {
    val KEY_ACTION_FIELD = "actionField"
    val KEY_PRODUCTS = "products"
}

object ActionFieldNodeTrackingKey {
    val KEY_ID = "id"
    val KEY_AFFILIATION = "affiliation"
    val KEY_REVENUE = "revenue"
    val KEY_TAX = "tax"
    val KEY_SHIPPING = "shipping"
    val KEY_COUPON = "coupon"
}

object ProductNodeTrackingKey {
    val KEY_NAME = "name"
    val KEY_ID = "id"
    val KEY_PRICE = "price"
    val KEY_BRAND = "brand"
    val KEY_CATEGORY = "category"
    val KEY_VARIANT = "variant"
    val KEY_QUANTITY = "quantity"
}