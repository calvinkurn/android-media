package com.tokopedia.sellerorder.analytics

import android.app.Activity
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * Created by fwidjaja on 2019-11-29.
 */
object SomAnalytics {

    private const val CATEGORY_SOM = "som"
    private const val CLICK_SOM = "clickSOM"
    private const val CLICK = "Click"
    private const val CLICK_QUICK_FILTER = "click quick filter"
    private const val CLICK_ORDER_CARD_ORDER_LIST = "click order card order list"
    private const val SUBMIT_SEARCH = "submit search"
    private const val CLICK_CHAT_ICON_ON_HEADER_ORDER_DETAIL = "click chat icon on header order detail"
    private const val CLICK_CHAT_ICON_ON_HEADER_ORDER_LIST = "click chat icon on header order list"
    private const val CLICK_MAIN_ACTION_IN_ORDER_DETAIL = "click main action in order detail"
    private const val CLICK_SECONDARY_ACTION_IN_ORDER_DETAIL = "click secondary action in order detail"
    private const val CLICK_BUTTON_PELUANG_IN_EMPTY_STATE = "click button peluang in empty state"
    private const val CLICK_TERAPKAN_ON_FILTER_PAGE = "click terapkan on filter page"
    private const val VIEW_SOM_IRIS = "viewSOMIris"
    private const val VIEW_TICKER = "view ticker"
    private const val CLICK_SEE_MORE_ON_TICKER = "click see more on ticker"
    private const val CLICK_X_ON_TICKER = "click x on ticker"
    private const val CLICK_FILTER_BUTTON_ON_ORDER_LIST = "click filter button on order list"
    private const val CLICK_VIEW_INVOICE = "click view invoice"
    private const val VIEW_EMPTY_STATE = "view empty state"
    private const val CLICK_BACK_BUTTON_ON_FILTER_PAGE = "click back button on filter page"
    private const val CLICK_RESET_BUTTON_ON_FILTER_PAGE = "click reset button on filter page"
    private const val CLICK_TOLAK_PESANAN = "click tolak pesanan"
    private const val CLICK_CEK_PELUANG_ON_EMPTY_STATE = "click cek peluang on empty state"
    private const val CLICK_ACCEPT_ORDER_POPUP = "click accept order popup"
    private const val CLICK_KONFIRMASI = "click konfirmasi"
    private const val CLICK_REQUEST_PICKUP_POPUP = "click request pickup popup"
    private const val CLICK_BUTTON_TOLAK_PESANAN_POPUP = "click button tolak pesanan - popup"
    private const val CLICK_BUTTON_CHAT_PEMBELI_POPUP = "click button chat pembeli - popup"
    private const val CLICK_SEARCH_RECENT_SEARCH = "top nav - click search - search box"
    private const val CLICK_BUTTON_DOWNLOAD_INVOICE = "click button download invoice"
    private const val CLICK_START_ADVERTISE = "click start advertise"
    private const val CLICK_WAITING_FOR_PAYMENT = "click waiting for payment"
    private const val CLICK_CHECK_MANAGE_STOCK = "click check and manage stock"
    private const val TO_APP_ORDER = "To App - Order"
    private const val SELLER_WIDGET = "sellerWidget"
    private const val SELLER_APP_WIDGET = "Seller App Widget"
    private const val CUSTOM_DIMENSION_USER_ID = "userId"
    private const val CUSTOM_DIMENSION_SHOP_ID = "shopId"
    private const val AWAITING_PAYMENT = "awaiting payment"
    private const val WAITING_FOR_PAYMENT = "awaiting payment"

    @JvmStatic
    fun sendScreenName(activity: Activity, screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    private fun sendEventCategoryAction(event: String, eventCategory: String,
                                        eventAction: String) {
        sendEventCategoryActionLabel(event, eventCategory, eventAction, "")
    }

    private fun sendEventCategoryActionLabel(event: String, eventCategory: String,
                                             eventAction: String, eventLabel: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                event, eventCategory, eventAction, eventLabel))
    }

    fun eventClickQuickFilter(orderLabel: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_QUICK_FILTER, orderLabel)
    }

    fun eventClickOrder(orderLabel: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_ORDER_CARD_ORDER_LIST, orderLabel)
    }

    fun eventSubmitSearch(keyword: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, SUBMIT_SEARCH, keyword)
    }

    fun eventClickChatOnHeaderDetail(statusOrder: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_CHAT_ICON_ON_HEADER_ORDER_DETAIL, statusOrder)
    }

    fun eventClickMainActionInOrderDetail(labelBtn: String, statusOrder: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, "$CLICK_MAIN_ACTION_IN_ORDER_DETAIL $labelBtn", statusOrder)
    }

    fun eventClickSecondaryActionInOrderDetail(labelBtn: String, statusOrder: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, "$CLICK_SECONDARY_ACTION_IN_ORDER_DETAIL $labelBtn", statusOrder)
    }

    fun eventClickButtonPeluangInEmptyState(statusOrder: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_BUTTON_PELUANG_IN_EMPTY_STATE, statusOrder)
    }

    fun eventClickTerapkanOnFilterPage(orderCode: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_TERAPKAN_ON_FILTER_PAGE, orderCode)
    }

    fun eventViewTicker(tickerId: String) {
        sendEventCategoryActionLabel(VIEW_SOM_IRIS, CATEGORY_SOM, VIEW_TICKER, tickerId)
    }

    fun eventClickSeeMoreOnTicker(tickerId: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_SEE_MORE_ON_TICKER, tickerId)
    }

    fun eventClickXOnTicker(tickerId: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_X_ON_TICKER, tickerId)
    }

    fun eventClickFilterButtonOnOrderList(orderCode: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_FILTER_BUTTON_ON_ORDER_LIST, orderCode)
    }

    fun eventClickViewInvoice(statusOrder: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_VIEW_INVOICE, statusOrder)
    }

    fun eventViewEmptyState(statusOrderName: String) {
        sendEventCategoryActionLabel(VIEW_SOM_IRIS, CATEGORY_SOM, VIEW_EMPTY_STATE, statusOrderName)
    }

    fun eventClickBackButtonOnFilterPage(orderCode: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_BACK_BUTTON_ON_FILTER_PAGE, orderCode)
    }

    fun eventClickResetButtonOnFilterPage(orderCode: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_RESET_BUTTON_ON_FILTER_PAGE, orderCode)
    }

    fun eventClickTolakPesanan(statusOrderName: String, reason: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_TOLAK_PESANAN, "$statusOrderName - $reason")
    }

    fun eventClickCekPeluangOnEmptyState(statusOrderName: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_CEK_PELUANG_ON_EMPTY_STATE, statusOrderName)
    }

    fun eventClickAcceptOrderPopup(isSuccess: Boolean) {
        var success = "success"
        if (!isSuccess) success = "failed"
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_ACCEPT_ORDER_POPUP, success)
    }

    fun eventClickKonfirmasi(isSuccess: Boolean) {
        var success = "success"
        if (!isSuccess) success = "failed"
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_KONFIRMASI, success)
    }

    fun eventClickRequestPickupPopup() {
        sendEventCategoryAction(CLICK_SOM, CATEGORY_SOM, CLICK_REQUEST_PICKUP_POPUP)
    }

    fun eventClickButtonTolakPesananPopup(statusOrder: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_BUTTON_TOLAK_PESANAN_POPUP, statusOrder)
    }

    fun eventClickButtonChatPembeliPopup(statusOrder: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_BUTTON_CHAT_PEMBELI_POPUP, statusOrder)
    }

    fun eventClickSearchBar() {
        sendEventCategoryAction(CLICK_SOM, CATEGORY_SOM, CLICK_SEARCH_RECENT_SEARCH)
    }

    fun eventClickButtonDownloadInvoice(orderCode: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_BUTTON_DOWNLOAD_INVOICE, orderCode)
    }

    fun eventClickChatIconOnOrderList(orderName: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_CHAT_ICON_ON_HEADER_ORDER_LIST, orderName)
    }

    fun eventClickWidgetNewOrder() {
        sendEventCategoryActionLabel(SELLER_WIDGET, SELLER_APP_WIDGET, CLICK, TO_APP_ORDER)
    }

    fun eventClickStartAds(orderName: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_START_ADVERTISE, orderName)
    }

    fun eventClickWaitingPaymentOrderCard(statusOrder: String, counter: Int, userId: String, shopId: String) {
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
}