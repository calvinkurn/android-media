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
    private const val CLICK_QUICK_FILTER = "click quick filter"
    private const val CLICK_ORDER_CARD_ORDER_LIST = "click order card order list"
    private const val SUBMIT_SEARCH = "submit search"
    private const val CLICK_CHAT_ICON_ON_HEADER_ORDER_DETAIL = "click chat icon on header order detail"
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

    fun eventClickOrder() {
        sendEventCategoryAction(CLICK_SOM, CATEGORY_SOM, CLICK_ORDER_CARD_ORDER_LIST)
    }

    fun eventSubmitSearch(keyword: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, SUBMIT_SEARCH, keyword)
    }

    fun eventClickChatOnHeaderDetail() {
        sendEventCategoryAction(CLICK_SOM, CATEGORY_SOM, CLICK_CHAT_ICON_ON_HEADER_ORDER_DETAIL)
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

    fun eventClickButtonPeluangInEmptyState() {
        sendEventCategoryAction(CLICK_SOM, CATEGORY_SOM, CLICK_TERAPKAN_ON_FILTER_PAGE)
    }

    fun eventViewTicker() {
        // TODO : put ticker_id to label
        sendEventCategoryActionLabel(VIEW_SOM_IRIS, CATEGORY_SOM, VIEW_TICKER, "")
    }

    fun eventClickSeeMoreOnTicker() {
        // TODO : put ticker_id to label
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_SEE_MORE_ON_TICKER, "")
    }

    fun eventClickXOnTicker() {
        // TODO : put ticker_id to label, ask unify team how to detect close button?
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_X_ON_TICKER, "")
    }

    fun eventClickFilterButtonOnOrderList() {
        sendEventCategoryAction(CLICK_SOM, CATEGORY_SOM, CLICK_FILTER_BUTTON_ON_ORDER_LIST)
    }

    fun eventClickViewInvoice() {
        sendEventCategoryAction(CLICK_SOM, CATEGORY_SOM, CLICK_VIEW_INVOICE)
    }

    fun eventViewEmptyState(statusOrderName: String) {
        sendEventCategoryActionLabel(VIEW_SOM_IRIS, CATEGORY_SOM, VIEW_EMPTY_STATE, statusOrderName)
    }

    fun eventClickBackButtonOnFilterPage() {
        sendEventCategoryAction(CLICK_SOM, CATEGORY_SOM, CLICK_BACK_BUTTON_ON_FILTER_PAGE)
    }

    fun eventClickResetButtonOnFilterPage() {
        sendEventCategoryAction(CLICK_SOM, CATEGORY_SOM, CLICK_RESET_BUTTON_ON_FILTER_PAGE)
    }

    fun eventClickTolakPesanan(statusOrderName: String, reason: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_TOLAK_PESANAN, "$statusOrderName - $reason")
    }

    fun eventClickCekPeluangOnEmptyState(statusOrderName: String) {
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_CEK_PELUANG_ON_EMPTY_STATE, statusOrderName)
    }
}