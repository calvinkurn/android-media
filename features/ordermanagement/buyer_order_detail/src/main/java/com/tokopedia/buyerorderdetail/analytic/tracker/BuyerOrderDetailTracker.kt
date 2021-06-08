package com.tokopedia.buyerorderdetail.analytic.tracker

import android.os.Bundle
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

object BuyerOrderDetailTracker {
    private fun MutableMap<String, Any>.appendGeneralEventData(
            eventName: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String
    ): MutableMap<String, Any> {
        put(TrackAppUtils.EVENT, eventName)
        put(TrackAppUtils.EVENT_CATEGORY, eventCategory)
        put(TrackAppUtils.EVENT_ACTION, eventAction)
        put(TrackAppUtils.EVENT_LABEL, eventLabel)
        return this
    }

    private fun Bundle.appendGeneralEventData(
            eventName: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String
    ): Bundle {
        putString(TrackAppUtils.EVENT, eventName)
        putString(TrackAppUtils.EVENT_CATEGORY, eventCategory)
        putString(TrackAppUtils.EVENT_ACTION, eventAction)
        putString(TrackAppUtils.EVENT_LABEL, eventLabel)
        return this
    }

    private fun MutableMap<String, Any>.appendBusinessUnit(businessUnit: String): MutableMap<String, Any> {
        put(BuyerOrderDetailTrackerConstant.EVENT_KEY_BUSINESS_UNIT, businessUnit)
        return this
    }

    private fun Bundle.appendBusinessUnit(businessUnit: String): Bundle {
        putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_BUSINESS_UNIT, businessUnit)
        return this
    }

    private fun MutableMap<String, Any>.appendCurrentSite(currentSite: String): MutableMap<String, Any> {
        put(BuyerOrderDetailTrackerConstant.EVENT_KEY_CURRENT_SITE, currentSite)
        return this
    }

    private fun Bundle.appendCurrentSite(currentSite: String): Bundle {
        putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_CURRENT_SITE, currentSite)
        return this
    }

    private fun Bundle.appendBuyAgainProductEE(
            products: List<ProductListUiModel.ProductUiModel>,
            cartId: String,
            shopId: String,
            shopName: String,
            shopType: String,
            currencyCode: String
    ): Bundle {
        val productsPayload = products.map {
            Bundle().apply {
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_CATEGORY, it.category)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_CATEGORY_ID, it.categoryId)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_DIMENSION_45, cartId)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_PRODUCT_ID, it.productId)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_NAME, it.productName)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_PRICE, it.price)
                putInt(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_QUANTITY, it.quantity)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_SHOP_ID, shopId)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_SHOP_NAME, shopName)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_SHOP_TYPE, shopType)
            }
        }
        val addProductsPayload = Bundle().apply {
            putBundle(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_ADD, Bundle().apply {
                putParcelableArrayList(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCTS, ArrayList(productsPayload))
            })
            putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_CURRENCY_CODE, currencyCode)
        }
        putBundle(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE, addProductsPayload)
        return this
    }

    private fun eventGeneralBuyerOrderDetail(eventAction: String, orderStatusCode: String, orderId: String) {
        val payload = mutableMapOf<String, Any>().appendGeneralEventData(
                eventName = BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PURCHASE_LIST,
                eventCategory = BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
                eventAction = eventAction,
                eventLabel = "$orderStatusCode${BuyerOrderDetailTrackerConstant.SEPARATOR_STRIP}$orderId"
        ).appendBusinessUnit(BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_MARKETPLACE).appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_MARKETPLACE)
        TrackApp.getInstance().gtm.sendGeneralEvent(payload)
    }

    fun eventClickSeeOrderHistoryDetail(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_SEE_ORDER_HISTORY_DETAIL, orderStatusCode, orderId)
    }

    fun eventClickSeeOrderInvoice(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_SEE_ORDER_INVOICE, orderStatusCode, orderId)
    }

    fun eventClickCopyOrderInvoice(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_COPY_ORDER_INVOICE, orderStatusCode, orderId)
    }

    fun eventClickShopName(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_SHOP_NAME, orderStatusCode, orderId)
    }

    fun eventClickProduct(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_PRODUCT, orderStatusCode, orderId)
    }

    // TODO: ask backend to put ticker action_key so that we can trigger this tracker
    fun eventClickSeeShipmentInfoTNC(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_SEE_SHIPMENT_TNC, orderStatusCode, orderId)
    }

    fun eventClickCopyOrderAwb(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_COPY_ORDER_AWB, orderStatusCode, orderId)
    }

    fun eventClickChatIcon(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_CHAT_ICON, orderStatusCode, orderId)
    }

    fun eventClickSeeComplaint(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_SEE_COMPLAINT, orderStatusCode, orderId)
    }

    fun eventClickActionButton(isPrimaryButton: Boolean, buttonName: String, orderStatusCode: String, orderId: String) {
        val eventAction = StringBuilder().apply {
            if (isPrimaryButton) append(BuyerOrderDetailTrackerConstant.EVENT_ACTION_PARTIAL_CLICK_ON_PRIMARY_BUTTON)
            else append(BuyerOrderDetailTrackerConstant.EVENT_ACTION_PARTIAL_CLICK_ON_SECONDARY_BUTTON)
            append(BuyerOrderDetailTrackerConstant.SEPARATOR_STRIP)
            append(buttonName)
        }.toString()
        eventGeneralBuyerOrderDetail(eventAction, orderStatusCode, orderId)
    }

    fun eventClickActionButtonFromReceiveConfirmation(buttonName: String, orderStatusCode: String, orderId: String) {
        val eventAction = StringBuilder().apply {
            append(BuyerOrderDetailTrackerConstant.EVENT_ACTION_PARTIAL_CLICK_ON_FINISH_ORDER_CONFIRMATION_DIALOG)
            append(BuyerOrderDetailTrackerConstant.SEPARATOR_STRIP)
            append(buttonName)
        }.toString()
        eventGeneralBuyerOrderDetail(eventAction, orderStatusCode, orderId)
    }

    fun eventClickSimilarProduct(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_SIMILAR_PRODUCT, orderStatusCode, orderId)
    }

    fun eventClickBuyAgain(
            products: List<ProductListUiModel.ProductUiModel>,
            orderId: String,
            cartId: String,
            shopId: String,
            shopName: String,
            shopType: String,
            currencyCode: String,
            userId: String
    ) {
        val payload = Bundle().apply {
            appendGeneralEventData(
                    eventName = BuyerOrderDetailTrackerConstant.EVENT_NAME_ADD_TO_CART,
                    eventCategory = BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
                    eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_BUY_AGAIN,
                    eventLabel = orderId)
            appendBusinessUnit(BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_MARKETPLACE)
            appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_MARKETPLACE)
            appendBuyAgainProductEE(products, cartId, shopId, shopName, shopType, currencyCode)
            putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_ID, products.joinToString(separator = BuyerOrderDetailTrackerConstant.SEPARATOR_COMMA) { it.productId })
            putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_USER_ID, userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(BuyerOrderDetailTrackerConstant.EVENT_NAME_ADD_TO_CART, payload)
    }
}