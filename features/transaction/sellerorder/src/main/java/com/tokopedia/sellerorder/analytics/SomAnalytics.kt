package com.tokopedia.sellerorder.analytics

import com.tokopedia.config.GlobalConfig
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.Tracker

/**
 * Created by fwidjaja on 2019-11-29.
 */
object SomAnalytics {

    private const val CATEGORY_SOM = "som"
    private const val CLICK_SOM = "clickSOM"
    private const val CLICK_PG = "clickPG"
    private const val CLICK_PRODUCT_NAME = "click product name"
    private const val CLICK_QUICK_FILTER = "click quick filter"
    private const val CLICK_ORDER_CARD_ON_ORDER_LIST = "click order card on order list"
    private const val SUBMIT_SEARCH = "submit search"
    private const val CLICK_CHAT_ICON_ON_HEADER_ORDER_DETAIL =
        "click chat icon on header order detail"
    private const val CLICK_MAIN_CTA_IN_ORDER_DETAIL = "click main CTA"
    private const val CLICK_SECONDARY_ACTION_IN_ORDER_DETAIL =
        "click secondary action in order detail"
    private const val CLICK_TERAPKAN_ON_FILTER_PAGE = "click terapkan on filter page"
    private const val CLICK_FILTER_BUTTON_ON_ORDER_LIST = "click filter button on order list"
    private const val CLICK_VIEW_INVOICE = "click view invoice"
    private const val CLICK_RESET_BUTTON_ON_FILTER_PAGE = "click reset button on filter page"
    private const val CLICK_TOLAK_PESANAN = "click tolak pesanan"
    private const val CLICK_ACCEPT_ORDER_POPUP = "click accept order popup"
    private const val CLICK_BUTTON_TOLAK_PESANAN_POPUP = "click button tolak pesanan - popup"
    private const val CLICK_BUTTON_DOWNLOAD_INVOICE = "click button download invoice"
    private const val CLICK_START_ADVERTISE = "click start advertise"
    private const val CLICK_WAITING_FOR_PAYMENT = "click waiting for payment"
    private const val CLICK_CHECK_MANAGE_STOCK = "click check and manage stock"
    private const val CLICK_BULK_PRINT_ACTION = "click print labels at once"
    private const val CLICK_BULK_PRINT_ACTION_YES = "click print labels - yes"
    private const val CLICK_BULK_PRINT_ACTION_CANCEL = "click print labels - cancel"
    private const val CLICK_BULK_ACCEPT_ALL = "click accept all"
    private const val BULK_ACCEPT_ORDER = "bulk accept order"
    private const val CLICK_REQUEST_PICKUP_ALL = "click request pickup all"
    private const val BULK_REQUEST_PICKUP = "bulk request pickup"
    private const val CUSTOM_DIMENSION_USER_ID = "userId"
    private const val CUSTOM_DIMENSION_SHOP_ID = "shopId"
    private const val CUSTOM_DIMENSION_BUSINESS_UNIT = "businessUnit"
    private const val CUSTOM_DIMENSION_CURRENT_SITE = "currentSite"
    private const val CUSTOM_DIMENSION_SHOP_TYPE = "shopType"
    private const val CUSTOM_DIMENSION_TRACKER_ID = "trackerId"
    private const val AWAITING_PAYMENT = "awaiting payment"
    private const val WAITING_FOR_PAYMENT = "waiting for payment"
    private const val BUSINESS_UNIT_PHYSICAL_GOODS = "physicalgoods"
    private const val BUSINESS_UNIT_PHYSICAL_GOODS_CAPITALIZE = "Physical Goods"
    private const val CURRENT_SITE_TOKOPEDIA_SELLER = "tokopediaseller"
    private const val BUSINESS_UNIT_SOM = "Seller Order Management"
    private const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
    private const val EVENT_NAME_VIEW_SHIPPING_IRIS = "viewShippingIris"
    private const val EVENT_ACTION_REQUEST_ORDER_EXTENSION = "request order extension status"
    private const val EVENT_ACTION_CLICK_ONBOARD_BACK = "click onboard info - balik"
    private const val EVENT_ACTION_CLICK_ONBOARD_CONTINUE = "click onboard info - lanjut"
    private const val EVENT_ACTION_CLICK_ONBOARD_OK = "click onboard info - oke"
    private const val EVENT_ACTION_CLICK_RESOLUTION_WIDGET = "click on resolution widget"

    private const val TRACKER_ID_CLICK_ONBOARD_CONTINUE_SA = "33451"
    private const val TRACKER_ID_CLICK_ONBOARD_BACK_SA = "33452"
    private const val TRACKER_ID_CLICK_ONBOARD_OK_SA = "33453"
    private const val TRACKER_ID_CLICK_ONBOARD_CONTINUE_MA = "33446"
    private const val TRACKER_ID_CLICK_ONBOARD_BACK_MA = "33447"
    private const val TRACKER_ID_CLICK_ONBOARD_OK_MA = "33448"

    @JvmStatic
    fun sendScreenName(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    private fun sendEventCategoryAction(
        event: String,
        eventCategory: String,
        eventAction: String
    ) {
        sendEventCategoryActionLabel(event, eventCategory, eventAction, "")
    }

    private fun sendEventCategoryActionLabel(
        event: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                event,
                eventCategory,
                eventAction,
                eventLabel
            )
        )
    }

    fun eventSubmitSearch(keyword: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, SUBMIT_SEARCH, keyword)
    }

    fun clickProductNameToSnapshot(statusOrderName: String, userId: String) {
        val event = mapOf(
            TrackAppUtils.EVENT to CLICK_SOM,
            TrackAppUtils.EVENT_CATEGORY to CATEGORY_SOM,
            TrackAppUtils.EVENT_ACTION to CLICK_PRODUCT_NAME,
            TrackAppUtils.EVENT_LABEL to statusOrderName,
            CUSTOM_DIMENSION_BUSINESS_UNIT to BUSINESS_UNIT_SOM,
            CUSTOM_DIMENSION_CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            CUSTOM_DIMENSION_USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickChatOnHeaderDetail(statusOrderCode: String, statusOrderName: String) {
        sendEventCategoryActionLabel(
            CLICK_SOM,
            CATEGORY_SOM,
            CLICK_CHAT_ICON_ON_HEADER_ORDER_DETAIL,
            "$statusOrderCode - $statusOrderName"
        )
    }

    fun eventClickCtaActionInOrderDetail(labelBtn: String, statusOrderCode: String) {
        sendEventCategoryActionLabel(
            CLICK_SOM,
            CATEGORY_SOM,
            "$CLICK_MAIN_CTA_IN_ORDER_DETAIL - $statusOrderCode",
            "$statusOrderCode - $labelBtn"
        )
    }

    fun eventClickSecondaryActionInOrderDetail(
        labelBtn: String,
        statusOrderCode: String,
        orderStatusName: String,
        shopId: String
    ) {
        val data = mapOf(
            TrackAppUtils.EVENT to CLICK_SOM,
            TrackAppUtils.EVENT_CATEGORY to CATEGORY_SOM,
            TrackAppUtils.EVENT_ACTION to CLICK_SECONDARY_ACTION_IN_ORDER_DETAIL,
            TrackAppUtils.EVENT_LABEL to "$statusOrderCode - $orderStatusName - $labelBtn",
            CUSTOM_DIMENSION_BUSINESS_UNIT to BUSINESS_UNIT_PHYSICAL_GOODS_CAPITALIZE,
            CUSTOM_DIMENSION_CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            CUSTOM_DIMENSION_SHOP_TYPE to shopId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun eventClickTerapkanOnFilterPage(filterValue: String) {
        sendEventCategoryActionLabel(
            CLICK_SOM,
            CATEGORY_SOM,
            CLICK_TERAPKAN_ON_FILTER_PAGE,
            filterValue
        )
    }

    fun eventClickViewInvoice(statusOrderCode: String, orderStatusName: String) {
        sendEventCategoryActionLabel(
            CLICK_SOM,
            CATEGORY_SOM,
            CLICK_VIEW_INVOICE,
            "$statusOrderCode - $orderStatusName"
        )
    }

    fun eventClickResetButtonOnFilterPage() {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_RESET_BUTTON_ON_FILTER_PAGE, "")
    }

    fun eventClickTolakPesanan(statusOrderName: String, reason: String) {
        sendEventCategoryActionLabel(
            CLICK_SOM,
            CATEGORY_SOM,
            CLICK_TOLAK_PESANAN,
            "$statusOrderName - $reason"
        )
    }

    fun eventClickAcceptOrderPopup(isSuccess: Boolean) {
        var success = "success"
        if (!isSuccess) success = "failed"
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_ACCEPT_ORDER_POPUP, success)
    }

    fun eventClickButtonTolakPesananPopup(statusOrder: String, statusOrderName: String) {
        sendEventCategoryActionLabel(
            CLICK_SOM,
            CATEGORY_SOM,
            CLICK_BUTTON_TOLAK_PESANAN_POPUP,
            "$statusOrder - $statusOrderName"
        )
    }

    fun eventClickButtonDownloadInvoice(orderCode: String) {
        sendEventCategoryActionLabel(
            CLICK_SOM,
            CATEGORY_SOM,
            CLICK_BUTTON_DOWNLOAD_INVOICE,
            orderCode
        )
    }

    fun eventClickWaitingPaymentOrderCard(
        statusOrder: String,
        counter: Int,
        userId: String,
        shopId: String
    ) {
        val data = mapOf(
            TrackAppUtils.EVENT to CLICK_SOM,
            TrackAppUtils.EVENT_CATEGORY to CATEGORY_SOM,
            TrackAppUtils.EVENT_ACTION to CLICK_WAITING_FOR_PAYMENT,
            TrackAppUtils.EVENT_LABEL to "$statusOrder - $AWAITING_PAYMENT:$counter",
            CUSTOM_DIMENSION_USER_ID to userId,
            CUSTOM_DIMENSION_SHOP_ID to shopId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun eventClickCheckAndSetStockButton(counter: Int, userId: String, shopId: String) {
        val data = mapOf(
            TrackAppUtils.EVENT to CLICK_SOM,
            TrackAppUtils.EVENT_CATEGORY to CATEGORY_SOM,
            TrackAppUtils.EVENT_ACTION to CLICK_CHECK_MANAGE_STOCK,
            TrackAppUtils.EVENT_LABEL to "$WAITING_FOR_PAYMENT - $AWAITING_PAYMENT:$counter",
            CUSTOM_DIMENSION_USER_ID to userId,
            CUSTOM_DIMENSION_SHOP_ID to shopId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    // SOM Revamp
    fun eventClickOrderCard(orderStatus: Int, orderStatusName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            CLICK_SOM,
            CATEGORY_SOM,
            CLICK_ORDER_CARD_ON_ORDER_LIST,
            "$orderStatus - $orderStatusName"
        )
    }

    fun eventClickFilter(orderStatus: List<String>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            CLICK_SOM,
            CATEGORY_SOM,
            CLICK_FILTER_BUTTON_ON_ORDER_LIST,
            orderStatus.joinToString(",")
        )
    }

    fun eventClickStatusFilter(orderStatus: List<String>, orderStatusName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            CLICK_SOM,
            CATEGORY_SOM,
            CLICK_QUICK_FILTER,
            "${orderStatus.joinToString(",")} - $orderStatusName"
        )
    }

    fun eventClickStartAdvertise(orderStatus: String, orderStatusName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            CLICK_SOM,
            CATEGORY_SOM,
            CLICK_START_ADVERTISE,
            "$orderStatus - $orderStatusName"
        )
    }

    fun eventBulkAcceptOrder(
        orderStatus: String,
        orderStatusName: String,
        acceptedOrderCount: Int,
        userId: String,
        shopId: String
    ) {
        val data = mapOf(
            TrackAppUtils.EVENT to CLICK_SOM,
            TrackAppUtils.EVENT_CATEGORY to CATEGORY_SOM,
            TrackAppUtils.EVENT_ACTION to "click accept all",
            TrackAppUtils.EVENT_LABEL to "$orderStatus - $orderStatusName - $acceptedOrderCount",
            CUSTOM_DIMENSION_USER_ID to userId,
            CUSTOM_DIMENSION_SHOP_ID to shopId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun eventClickBulkPrintAwb(userId: String) {
        val data = mapOf(
            TrackAppUtils.EVENT to CLICK_SOM,
            TrackAppUtils.EVENT_CATEGORY to CATEGORY_SOM,
            TrackAppUtils.EVENT_ACTION to CLICK_BULK_PRINT_ACTION,
            TrackAppUtils.EVENT_LABEL to "",
            CUSTOM_DIMENSION_BUSINESS_UNIT to BUSINESS_UNIT_PHYSICAL_GOODS,
            CUSTOM_DIMENSION_CURRENT_SITE to CURRENT_SITE_TOKOPEDIA_SELLER,
            CUSTOM_DIMENSION_USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun eventClickYesOnBulkPrintAwb(userId: String) {
        val data = mapOf(
            TrackAppUtils.EVENT to CLICK_SOM,
            TrackAppUtils.EVENT_CATEGORY to CATEGORY_SOM,
            TrackAppUtils.EVENT_ACTION to CLICK_BULK_PRINT_ACTION_YES,
            TrackAppUtils.EVENT_LABEL to "",
            CUSTOM_DIMENSION_BUSINESS_UNIT to BUSINESS_UNIT_PHYSICAL_GOODS,
            CUSTOM_DIMENSION_CURRENT_SITE to CURRENT_SITE_TOKOPEDIA_SELLER,
            CUSTOM_DIMENSION_USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun eventClickCancelOnBulkPrintAwb(userId: String) {
        val data = mapOf(
            TrackAppUtils.EVENT to CLICK_SOM,
            TrackAppUtils.EVENT_CATEGORY to CATEGORY_SOM,
            TrackAppUtils.EVENT_ACTION to CLICK_BULK_PRINT_ACTION_CANCEL,
            TrackAppUtils.EVENT_LABEL to "",
            CUSTOM_DIMENSION_BUSINESS_UNIT to BUSINESS_UNIT_PHYSICAL_GOODS,
            CUSTOM_DIMENSION_CURRENT_SITE to CURRENT_SITE_TOKOPEDIA_SELLER,
            CUSTOM_DIMENSION_USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun eventClickBulkAcceptOrder(userId: String, shopId: String, orderIdList: List<String>) {
        val orderIds = orderIdList.joinToString(separator = ",") { it }
        val data = mapOf(
            TrackAppUtils.EVENT to CLICK_SOM,
            TrackAppUtils.EVENT_ACTION to CLICK_BULK_ACCEPT_ALL,
            TrackAppUtils.EVENT_CATEGORY to CATEGORY_SOM,
            TrackAppUtils.EVENT_LABEL to "$BULK_ACCEPT_ORDER - $orderIds",
            CUSTOM_DIMENSION_BUSINESS_UNIT to BUSINESS_UNIT_PHYSICAL_GOODS,
            CUSTOM_DIMENSION_CURRENT_SITE to CURRENT_SITE_TOKOPEDIA_SELLER,
            CUSTOM_DIMENSION_USER_ID to userId,
            CUSTOM_DIMENSION_SHOP_ID to shopId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun eventClickBulkRequestPickup(userId: String, shopId: String, orderIdList: List<String>) {
        val orderIds = orderIdList.joinToString(separator = ",") { it }
        val data = mapOf(
            TrackAppUtils.EVENT to CLICK_SOM,
            TrackAppUtils.EVENT_ACTION to CLICK_REQUEST_PICKUP_ALL,
            TrackAppUtils.EVENT_CATEGORY to CATEGORY_SOM,
            TrackAppUtils.EVENT_LABEL to "$BULK_REQUEST_PICKUP - $orderIds",
            CUSTOM_DIMENSION_BUSINESS_UNIT to BUSINESS_UNIT_PHYSICAL_GOODS,
            CUSTOM_DIMENSION_CURRENT_SITE to CURRENT_SITE_TOKOPEDIA_SELLER,
            CUSTOM_DIMENSION_USER_ID to userId,
            CUSTOM_DIMENSION_SHOP_ID to shopId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun eventFinishSendOrderExtensionRequest(shopId: String, orderId: String, success: Boolean) {
        val data = mapOf(
            TrackAppUtils.EVENT to EVENT_NAME_VIEW_SHIPPING_IRIS,
            TrackAppUtils.EVENT_ACTION to EVENT_ACTION_REQUEST_ORDER_EXTENSION,
            TrackAppUtils.EVENT_CATEGORY to CATEGORY_SOM,
            TrackAppUtils.EVENT_LABEL to "${if (success) "success" else "failed"} - $orderId",
            CUSTOM_DIMENSION_BUSINESS_UNIT to BUSINESS_UNIT_PHYSICAL_GOODS_CAPITALIZE,
            CUSTOM_DIMENSION_CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            CUSTOM_DIMENSION_SHOP_ID to shopId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun eventClickOnboardInfoContinue(userId: String) {
        val data = mapOf(
            TrackAppUtils.EVENT to CLICK_PG,
            TrackAppUtils.EVENT_ACTION to EVENT_ACTION_CLICK_ONBOARD_CONTINUE,
            TrackAppUtils.EVENT_CATEGORY to CATEGORY_SOM,
            TrackAppUtils.EVENT_LABEL to userId,
            CUSTOM_DIMENSION_BUSINESS_UNIT to BUSINESS_UNIT_PHYSICAL_GOODS_CAPITALIZE,
            CUSTOM_DIMENSION_CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            CUSTOM_DIMENSION_TRACKER_ID to if (GlobalConfig.isSellerApp()) {
                TRACKER_ID_CLICK_ONBOARD_CONTINUE_SA
            } else {
                TRACKER_ID_CLICK_ONBOARD_CONTINUE_MA
            }
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun eventClickOnboardInfoBack(userId: String) {
        val data = mapOf(
            TrackAppUtils.EVENT to CLICK_PG,
            TrackAppUtils.EVENT_ACTION to EVENT_ACTION_CLICK_ONBOARD_BACK,
            TrackAppUtils.EVENT_CATEGORY to CATEGORY_SOM,
            TrackAppUtils.EVENT_LABEL to userId,
            CUSTOM_DIMENSION_BUSINESS_UNIT to BUSINESS_UNIT_PHYSICAL_GOODS_CAPITALIZE,
            CUSTOM_DIMENSION_CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            CUSTOM_DIMENSION_TRACKER_ID to if (GlobalConfig.isSellerApp()) {
                TRACKER_ID_CLICK_ONBOARD_BACK_SA
            } else {
                TRACKER_ID_CLICK_ONBOARD_BACK_MA
            }
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun eventClickOnboardInfoOk(userId: String) {
        val data = mapOf(
            TrackAppUtils.EVENT to CLICK_PG,
            TrackAppUtils.EVENT_ACTION to EVENT_ACTION_CLICK_ONBOARD_OK,
            TrackAppUtils.EVENT_CATEGORY to CATEGORY_SOM,
            TrackAppUtils.EVENT_LABEL to userId,
            CUSTOM_DIMENSION_BUSINESS_UNIT to BUSINESS_UNIT_PHYSICAL_GOODS_CAPITALIZE,
            CUSTOM_DIMENSION_CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            CUSTOM_DIMENSION_TRACKER_ID to if (GlobalConfig.isSellerApp()) {
                TRACKER_ID_CLICK_ONBOARD_OK_SA
            } else {
                TRACKER_ID_CLICK_ONBOARD_OK_MA
            }
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendClickOnResolutionWidgetEvent(userId: String) {
        val trackerId = if (GlobalConfig.isSellerApp()) {
            "38438"
        } else {
            "38435"
        }
        Tracker.Builder()
            .setEvent(CLICK_PG)
            .setEventAction(EVENT_ACTION_CLICK_RESOLUTION_WIDGET)
            .setEventCategory(CATEGORY_SOM)
            .setEventLabel(userId)
            .setCustomProperty(CUSTOM_DIMENSION_TRACKER_ID, trackerId)
            .setBusinessUnit(BUSINESS_UNIT_PHYSICAL_GOODS_CAPITALIZE)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .build()
            .send()
    }
}
