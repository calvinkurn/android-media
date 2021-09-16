package com.tokopedia.buyerorderdetail.analytic.tracker

import android.os.Bundle
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * Created by yusuf.hendrawan on 08/06/21.
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/1122
 */

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

    private fun MutableMap<String, Any>.appendUserId(userId: String): MutableMap<String, Any> {
        put(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_USER_ID, userId)
        return this
    }

    private fun Bundle.appendUserId(userId: String): Bundle {
        putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_USER_ID, userId)
        return this
    }

    private fun Bundle.appendProductIds(products: List<ProductListUiModel.ProductUiModel>): Bundle {
        putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_ID, products.joinToString(separator = BuyerOrderDetailTrackerConstant.SEPARATOR_COMMA) { it.productId })
        return this
    }

    private fun MutableMap<String, Any>.appendOrderListDetailMarker(): MutableMap<String, Any> {
        put(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_DIMENSION_40, BuyerOrderDetailTrackerConstant.MARKER_ORDER_LIST_DETAIL_MARKETPLACE)
        return this
    }

    private fun Map<String, Any>.sendGeneralEvent() {
        TrackApp.getInstance().gtm.sendGeneralEvent(this)
    }

    private fun Bundle.sendEnhancedEcommerce(eventName: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, this)
    }

    private fun Bundle.appendBuyAgainProductEE(
        products: List<ProductListUiModel.ProductUiModel>,
        atcResult: List<AtcMultiData.AtcMulti.BuyAgainData.AtcProduct>,
        shopId: String,
        shopName: String,
        shopType: String
    ): Bundle {
        val productsPayload = products.map { product ->
            Bundle().apply {
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_CATEGORY_ID, product.categoryId)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_DIMENSION_40, BuyerOrderDetailTrackerConstant.MARKER_ORDER_LIST_DETAIL_MARKETPLACE)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_DIMENSION_45, atcResult.find { it.productId.toString() == product.productId }?.cartId.orZero().toString())
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_BRAND, "")
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_CATEGORY, product.category)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_PRODUCT_ID, product.productId)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_NAME, product.productName)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_VARIANT, "")
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_PRICE, product.price.toString())
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_QUANTITY, product.quantity.toString())
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_SHOP_ID, shopId)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_SHOP_NAME, shopName)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_SHOP_TYPE, shopType)
            }
        }
        putParcelableArrayList(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCTS_ITEMS, ArrayList(productsPayload))
        return this
    }

    private fun eventGeneralBuyerOrderDetail(
        eventAction: String,
        orderStatusCode: String,
        orderId: String
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PURCHASE_LIST,
            eventCategory = BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            eventAction = eventAction,
            eventLabel = "$orderStatusCode${BuyerOrderDetailTrackerConstant.SEPARATOR_STRIP}$orderId"
        ).appendBusinessUnit(BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_MARKETPLACE)
            .appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .sendGeneralEvent()
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
            append(BuyerOrderDetailTrackerConstant.EVENT_ACTION_PARTIAL_CLICK)
            append(BuyerOrderDetailTrackerConstant.SEPARATOR_SPACE)
            append(buttonName)
            append(BuyerOrderDetailTrackerConstant.SEPARATOR_SPACE)
            append(BuyerOrderDetailTrackerConstant.EVENT_ACTION_PARTIAL_CLICK_ON_FINISH_ORDER_CONFIRMATION_DIALOG)
        }.toString()
        eventGeneralBuyerOrderDetail(eventAction, orderStatusCode, orderId)
    }

    fun eventClickSimilarProduct(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_SIMILAR_PRODUCT, orderStatusCode, orderId)
    }

    fun eventClickBuyAgain(
        orderId: String,
        userId: String
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PURCHASE_LIST,
            eventCategory = BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_BUY_AGAIN,
            eventLabel = "${BuyerOrderDetailTrackerConstant.EVENT_LABEL_ATTEMPT_BUY_AGAIN} $orderId"
        ).appendBusinessUnit(BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS)
            .appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_MARKETPLACE)
            .appendUserId(userId)
            .sendGeneralEvent()
    }

    fun eventSuccessATC(
        products: List<ProductListUiModel.ProductUiModel>,
        atcResult: List<AtcMultiData.AtcMulti.BuyAgainData.AtcProduct>,
        orderId: String,
        shopId: String,
        shopName: String,
        shopType: String,
        userId: String
    ) {
        Bundle().appendGeneralEventData(
            eventName = BuyerOrderDetailTrackerConstant.EVENT_NAME_ADD_TO_CART,
            eventCategory = BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_BUY_AGAIN_SUCCESS,
            eventLabel = "${BuyerOrderDetailTrackerConstant.EVENT_LABEL_BUY_AGAIN_SUCCESS} $orderId"
        ).appendBusinessUnit(BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS)
            .appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_MARKETPLACE)
            .appendBuyAgainProductEE(products, atcResult, shopId, shopName, shopType)
            .appendProductIds(products)
            .appendUserId(userId)
            .sendEnhancedEcommerce(BuyerOrderDetailTrackerConstant.EVENT_NAME_ADD_TO_CART)
    }
}