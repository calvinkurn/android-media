package com.tokopedia.buyerorderdetail.analytic.tracker

import android.os.Bundle
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS
import com.tokopedia.buyerorderdetail.presentation.model.OrderInsuranceUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.toZeroStringIfNull
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.Tracker

/**
 * Created by yusuf.hendrawan on 08/06/21.
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/1122
 */

object BuyerOrderDetailTracker {

    object SavingsWidget {
        fun clickSavingsWidget(
            orderId: String,
            isPlus: Boolean
        ) {
            val plus = if (isPlus) "plus" else "non plus"
            val trackerId = if (isPlus) "48649" else "48651"

            mutableMapOf<String, Any>().appendGeneralEventData(
                eventName = BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PG,
                eventCategory = BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
                eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_SAVING_WIDGET +
                        plus,
                eventLabel = orderId
            ).appendBusinessUnit(BUSINESS_UNIT_PHYSICAL_GOODS)
                .appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
                .appendTrackerId(trackerId)
                .sendGeneralEvent()
        }

        fun impressSavingsWidget(
            orderId: String,
            isPlus: Boolean
        ) {
            val plus = if (isPlus) "plus" else "non plus"
            val trackerId = if (isPlus) "48650" else "48652"
            mutableMapOf<String, Any>().appendGeneralEventData(
                eventName = BuyerOrderDetailTrackerConstant.EVENT_NAME_VIEW_PG_IRIS,
                eventCategory = BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
                eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_IMPRESSION_SAVING_WIDGET +
                        plus,
                eventLabel = orderId
            ).appendBusinessUnit(BUSINESS_UNIT_PHYSICAL_GOODS)
                .appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
                .appendTrackerId(trackerId)
                .sendGeneralEvent()
        }
    }

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

    private fun MutableMap<String, Any>.appendTrackerId(trackerId: String): MutableMap<String, Any> {
        if (trackerId.isNotBlank()) {
            put(BuyerOrderDetailTrackerConstant.EVENT_KEY_TRACKER_ID, trackerId)
        }
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
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_DIMENSION_45, atcResult.find { it.productId == product.productId }?.cartId.toZeroStringIfNull())
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_BRAND, "")
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_CATEGORY, product.category)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_PRODUCT_ID, product.productId)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_NAME, product.productName)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_VARIANT, "")
                putDouble(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_PRICE, product.price)
                putInt(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_QUANTITY, product.quantity)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_SHOP_ID, shopId)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_SHOP_NAME, shopName)
                putString(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCT_SHOP_TYPE, shopType)
            }
        }
        putParcelableArrayList(BuyerOrderDetailTrackerConstant.EVENT_KEY_ENHANCED_ECOMMERCE_PRODUCTS_ITEMS, ArrayList(productsPayload))
        return this
    }

    private fun eventGeneralBuyerOrderDetail(
        eventName: String = BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PURCHASE_LIST,
        eventAction: String,
        orderStatusCode: String,
        orderId: String,
        businessUnit: String = BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_MARKETPLACE,
        trackerId: String = ""
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = eventName,
            eventCategory = BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            eventAction = eventAction,
            eventLabel = "$orderStatusCode${BuyerOrderDetailTrackerConstant.SEPARATOR_STRIP}$orderId"
        ).appendBusinessUnit(businessUnit)
            .appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .appendTrackerId(trackerId)
            .sendGeneralEvent()
    }

    fun eventClickSeeOrderHistoryDetail(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_SEE_ORDER_HISTORY_DETAIL,
            orderStatusCode = orderStatusCode,
            orderId = orderId
        )
    }

    fun eventClickSeeOrderInvoice(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_SEE_ORDER_INVOICE,
            orderStatusCode = orderStatusCode,
            orderId = orderId
        )
    }

    fun eventClickSeePodPreview(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_POD_PREVIEW,
            orderStatusCode = orderStatusCode,
            orderId = orderId,
            trackerId = "39813"
        )
    }

    fun eventClickCopyOrderInvoice(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_COPY_ORDER_INVOICE,
            orderStatusCode = orderStatusCode,
            orderId = orderId
        )
    }

    fun eventClickShopName(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_SHOP_NAME,
            orderStatusCode = orderStatusCode,
            orderId = orderId
        )
    }

    fun eventClickProduct(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_PRODUCT,
            orderStatusCode = orderStatusCode,
            orderId = orderId
        )
    }

    fun eventClickSeeShipmentInfoTNC(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_SEE_SHIPMENT_TNC,
            orderStatusCode = orderStatusCode,
            orderId = orderId
        )
    }

    fun eventClickCopyOrderAwb(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_COPY_ORDER_AWB,
            orderStatusCode = orderStatusCode,
            orderId = orderId
        )
    }

    fun eventClickChatIcon(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_CHAT_ICON,
            orderStatusCode = orderStatusCode,
            orderId = orderId
        )
    }

    fun eventClickSeeComplaint(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_SEE_COMPLAINT,
            orderStatusCode = orderStatusCode,
            orderId = orderId
        )
    }

    fun eventClickActionButtonSOM(isPrimaryButton: Boolean, buttonName: String, orderStatusCode: String, orderId: String) {
        val eventAction = StringBuilder().apply {
            if (isPrimaryButton) {
                append(BuyerOrderDetailTrackerConstant.EVENT_ACTION_PARTIAL_CLICK_ON_PRIMARY_BUTTON)
            } else {
                append(BuyerOrderDetailTrackerConstant.EVENT_ACTION_PARTIAL_CLICK_ON_SECONDARY_BUTTON)
            }
            append(BuyerOrderDetailTrackerConstant.SEPARATOR_STRIP)
            append(buttonName)
        }.toString()
        eventGeneralBuyerOrderDetail(eventAction = eventAction, orderStatusCode = orderStatusCode, orderId = orderId)
    }

    fun eventClickActionButtonPG(isPrimaryButton: Boolean, buttonName: String, trackerId: String, orderStatusCode: String, orderId: String) {
        val eventAction = StringBuilder().apply {
            if (isPrimaryButton) {
                append(BuyerOrderDetailTrackerConstant.EVENT_ACTION_PARTIAL_CLICK_ON_PRIMARY_BUTTON)
            } else {
                append(BuyerOrderDetailTrackerConstant.EVENT_ACTION_PARTIAL_CLICK_ON_SECONDARY_BUTTON)
            }
            append(BuyerOrderDetailTrackerConstant.SEPARATOR_STRIP)
            append(buttonName)
        }.toString()
        eventGeneralBuyerOrderDetail(BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PG, eventAction, orderStatusCode, orderId, BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS, trackerId)
    }

    fun eventClickActionButtonFromReceiveConfirmation(buttonName: String, orderStatusCode: String, orderId: String) {
        val eventAction = StringBuilder().apply {
            append(BuyerOrderDetailTrackerConstant.EVENT_ACTION_PARTIAL_CLICK)
            append(BuyerOrderDetailTrackerConstant.SEPARATOR_SPACE)
            append(buttonName)
            append(BuyerOrderDetailTrackerConstant.SEPARATOR_SPACE)
            append(BuyerOrderDetailTrackerConstant.EVENT_ACTION_PARTIAL_CLICK_ON_FINISH_ORDER_CONFIRMATION_DIALOG)
        }.toString()
        eventGeneralBuyerOrderDetail(eventAction = eventAction, orderStatusCode = orderStatusCode, orderId = orderId)
    }

    fun eventClickSimilarProduct(orderStatusCode: String, orderId: String) {
        eventGeneralBuyerOrderDetail(
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_SIMILAR_PRODUCT,
            orderStatusCode = orderStatusCode,
            orderId = orderId
        )
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
            .appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .appendUserId(userId)
            .sendGeneralEvent()
    }

    fun eventClickWarrantyClaim(
        orderId: String
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PG,
            eventCategory = BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_CLAIM_WARRANTY,
            eventLabel = orderId
        ).apply {
            put(BuyerOrderDetailTrackerConstant.EVENT_KEY_TRACKER_ID, BuyerOrderDetailTrackerConstant.TRACKER_ID_47433)
        }.appendBusinessUnit(BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS)
            .appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
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
            .appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .appendBuyAgainProductEE(products, atcResult, shopId, shopName, shopType)
            .appendProductIds(products)
            .appendUserId(userId)
            .sendEnhancedEcommerce(BuyerOrderDetailTrackerConstant.EVENT_NAME_ADD_TO_CART)
    }

    fun eventImpressionInsuranceWidget(
        trackerData: OrderInsuranceUiModel.TrackerData
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = BuyerOrderDetailTrackerConstant.EVENT_NAME_VIEW_PG_IRIS,
            eventCategory = BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_IMPRESSION_INSURANCE_WIDGET,
            eventLabel = "${trackerData.orderStatusCode}${BuyerOrderDetailTrackerConstant.SEPARATOR_STRIP}${trackerData.orderId}"
        ).appendBusinessUnit(BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS)
            .appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .appendTrackerId(BuyerOrderDetailTrackerConstant.TRACKER_ID_IMPRESSION_INSURANCE_WIDGET)
            .sendGeneralEvent()
    }

    fun eventClickInsuranceWidget(
        trackerData: OrderInsuranceUiModel.TrackerData
    ) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PG,
            eventCategory = BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_INSURANCE_WIDGET,
            eventLabel = "${trackerData.orderStatusCode}${BuyerOrderDetailTrackerConstant.SEPARATOR_STRIP}${trackerData.orderId}"
        ).appendBusinessUnit(BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS)
            .appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .appendTrackerId(BuyerOrderDetailTrackerConstant.TRACKER_ID_CLICK_INSURANCE_WIDGET)
            .sendGeneralEvent()
    }

    fun eventClickSeeAllProduct() {
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PG,
            eventCategory = BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_SEE_ALL_PRODUCTS,
            eventLabel = String.EMPTY
        ).appendBusinessUnit(BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS)
            .appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .appendTrackerId(BuyerOrderDetailTrackerConstant.TRACKER_ID_41154)
            .sendGeneralEvent()
    }

    fun eventClickSeeLessProduct() {
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PG,
            eventCategory = BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_SEE_LESS_PRODUCTS,
            eventLabel = String.EMPTY
        ).appendBusinessUnit(BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS)
            .appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .appendTrackerId(BuyerOrderDetailTrackerConstant.TRACKER_ID_41155)
            .sendGeneralEvent()
    }

    fun eventClickEstimateIconInBom() {
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PG,
            eventCategory = BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_ESTIMATE_ICON_POF_BOM_DETAIL,
            eventLabel = String.EMPTY
        ).appendBusinessUnit(BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS)
            .appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .appendTrackerId(BuyerOrderDetailTrackerConstant.TRACKER_ID_41156)
            .sendGeneralEvent()
    }

    fun sendClickOnOrderGroupWidget(orderId: String) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PG,
            eventCategory = BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_ON_ORDER_WIDGET,
            eventLabel = orderId
        ).appendBusinessUnit(BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS)
            .appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .appendTrackerId(BuyerOrderDetailTrackerConstant.TRACKER_ID_44136)
            .sendGeneralEvent()
    }

    fun sendClickViewDetailOnOrderGroupDetail(orderId: String) {
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PG,
            eventCategory = BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_VIEW_DETAIL_ORDER_GROUP,
            eventLabel = orderId
        ).appendBusinessUnit(BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS)
            .appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .appendTrackerId(BuyerOrderDetailTrackerConstant.TRACKER_ID_44137)
            .sendGeneralEvent()
    }

    fun sendClickOnResolutionWidgetEvent(orderStatusCode: String, orderId: String) {
        val eventLabel = "$orderStatusCode - $orderId"
        val trackerId = "38355"
        Tracker.Builder()
            .setEvent(BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_PG)
            .setEventAction(BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_RESOLUTION_WIDGET)
            .setEventCategory(BuyerOrderDetailTrackerConstant.EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP)
            .setEventLabel(eventLabel)
            .setCustomProperty(BuyerOrderDetailTrackerConstant.EVENT_KEY_TRACKER_ID, trackerId)
            .setBusinessUnit(BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_PHYSICAL_GOODS)
            .setCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .build()
            .send()
    }

    fun sendClickOnShareButton(orderId: String, productId: String, orderStatusId: String, userId: String) {
        val eventLabel = "$productId - $orderId - $orderStatusId"
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_COMMUNICATION,
            eventCategory = BuyerOrderDetailTrackerConstant.EVENT_ORDER_DETAIL_HISTORY,
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_SHARE_BUTTON,
            eventLabel = eventLabel
        ).appendBusinessUnit(BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_SHARING_EXPERIENCE)
            .appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .appendTrackerId(BuyerOrderDetailTrackerConstant.TRACKER_ID_45653)
            .appendUserId(userId)
            .sendGeneralEvent()
    }

    fun sendClickOnClosingBottomSheet(orderId: String, productId: String, orderStatusId: String, userId: String) {
        val eventLabel = "$productId - $orderId - $orderStatusId"
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_COMMUNICATION,
            eventCategory = BuyerOrderDetailTrackerConstant.EVENT_ORDER_DETAIL_HISTORY,
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_CLOSE_SHARE_BOTTOM_SHEET,
            eventLabel = eventLabel
        ).appendBusinessUnit(BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_SHARING_EXPERIENCE)
            .appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .appendTrackerId(BuyerOrderDetailTrackerConstant.TRACKER_ID_45654)
            .appendUserId(userId)
            .sendGeneralEvent()
    }

    fun sendClickOnSelectionOfSharingChannels(channel: String, orderId: String, productId: String, orderStatusId: String, userId: String) {
        val eventLabel = "$channel - $productId - $orderId - $orderStatusId"
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = BuyerOrderDetailTrackerConstant.EVENT_NAME_CLICK_COMMUNICATION,
            eventCategory = BuyerOrderDetailTrackerConstant.EVENT_ORDER_DETAIL_HISTORY,
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_CLICK_SHARING_CHANNEL,
            eventLabel = eventLabel
        ).appendBusinessUnit(BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_SHARING_EXPERIENCE)
            .appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .appendTrackerId(BuyerOrderDetailTrackerConstant.TRACKER_ID_45655)
            .appendUserId(userId)
            .sendGeneralEvent()
    }

    fun eventImpressionShareBottomSheet(orderId: String, productId: String, orderStatusId: String, userId: String) {
        val eventLabel = "$productId - $orderId - $orderStatusId"
        mutableMapOf<String, Any>().appendGeneralEventData(
            eventName = BuyerOrderDetailTrackerConstant.EVENT_NAME_VIEW_COMMUNICATION_IRIS,
            eventCategory = BuyerOrderDetailTrackerConstant.EVENT_ORDER_DETAIL_HISTORY,
            eventAction = BuyerOrderDetailTrackerConstant.EVENT_ACTION_IMPRESSION_SHARE_BOTTOM_SHEET,
            eventLabel = eventLabel
        ).appendBusinessUnit(BuyerOrderDetailTrackerConstant.BUSINESS_UNIT_SHARING_EXPERIENCE)
            .appendCurrentSite(BuyerOrderDetailTrackerConstant.CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .appendTrackerId(BuyerOrderDetailTrackerConstant.TRACKER_ID_45656)
            .appendUserId(userId)
            .sendGeneralEvent()
    }
}
