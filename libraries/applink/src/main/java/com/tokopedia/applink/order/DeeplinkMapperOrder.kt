package com.tokopedia.applink.order

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.applink.startsWithPattern

/**
 * Created by fwidjaja on 2020-01-26.
 */
object DeeplinkMapperOrder {

    private const val QUERY_TAB_ACTIVE = "tab_active"
    private const val QUERY_TAB_STATUS = "tab_status"
    private const val NEW_ORDER = "new_order"
    private const val CONFIRM_SHIPPING = "confirm_shipping"
    private const val IN_SHIPPING = "in_shipping"
    private const val DELIVERED = "delivered"
    private const val DONE = "done"
    private const val CANCELLED = "order_canceled"
    private const val ALL_ORDER = "all_order"
    private const val FILTER_STATUS_ID = "filter_status_id"
    private const val FILTER_WAITING_PICKUP = "7"
    private const val FILTER_WAITING_AWB = "9"
    private const val FILTER_AWB_INVALID = "10"
    private const val FILTER_AWB_CHANGE = "11"
    private const val FILTER_RETUR = "13"
    private const val FILTER_COMPLAINT = "15"

    fun getRegisteredNavigationOrder(deeplink: String): String {
        return if (deeplink.startsWithPattern(ApplinkConst.SELLER_ORDER_DETAIL)) getRegisteredNavigationOrderInternal(deeplink)
        else deeplink
    }

    /**
     * tokopedia://seller/order/{order_id}
     */
    private fun getRegisteredNavigationOrderInternal(deeplink: String): String {
        return deeplink.replace(DeeplinkConstant.SCHEME_TOKOPEDIA, DeeplinkConstant.SCHEME_INTERNAL)
    }

    fun getRegisteredNavigationMainAppSellerNewOrder(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to NEW_ORDER)
        return UriUtil.buildUriAppendParam(ApplinkConstInternalOrder.NEW_ORDER, param)
    }

    fun getRegisteredNavigationMainAppSellerReadyToShip(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to CONFIRM_SHIPPING)
        return UriUtil.buildUriAppendParam(ApplinkConstInternalOrder.READY_TO_SHIP, param)
    }

    fun getRegisteredNavigationMainAppSellerInShipping(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to IN_SHIPPING)
        return UriUtil.buildUriAppendParam(ApplinkConstInternalOrder.SHIPPED, param)
    }

    fun getRegisteredNavigationMainAppSellerDelivered(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to IN_SHIPPING, QUERY_TAB_STATUS to DELIVERED)
        return UriUtil.buildUriAppendParams(ApplinkConstInternalOrder.DELIVERED, param)
    }

    fun getRegisteredNavigationMainAppSellerFinished(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to DONE)
        return UriUtil.buildUriAppendParam(ApplinkConstInternalOrder.FINISHED, param)
    }

    fun getRegisteredNavigationMainAppSellerCancelled(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to CANCELLED)
        return UriUtil.buildUriAppendParam(ApplinkConstInternalOrder.CANCELLED, param)
    }

    fun getRegisteredNavigationMainAppSellerHistory(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to ALL_ORDER)
        return UriUtil.buildUriAppendParam(ApplinkConstInternalOrder.HISTORY, param)
    }

    fun getRegisteredNavigationMainAppSellerWaitingPickup(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to "", FILTER_STATUS_ID to FILTER_WAITING_PICKUP)
        return UriUtil.buildUriAppendParams(ApplinkConstInternalOrder.WAITING_PICKUP, param)
    }

    fun getRegisteredNavigationMainAppSellerWaitingAwb(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to "", FILTER_STATUS_ID to FILTER_WAITING_AWB)
        return UriUtil.buildUriAppendParams(ApplinkConstInternalOrder.WAITING_AWB, param)
    }

    fun getRegisteredNavigationMainAppSellerAwbInvalid(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to "", FILTER_STATUS_ID to FILTER_AWB_INVALID)
        return UriUtil.buildUriAppendParams(ApplinkConstInternalOrder.AWB_INVALID, param)
    }

    fun getRegisteredNavigationMainAppSellerAwbChange(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to "", FILTER_STATUS_ID to FILTER_AWB_CHANGE)
        return UriUtil.buildUriAppendParams(ApplinkConstInternalOrder.AWB_CHANGE, param)
    }

    fun getRegisteredNavigationMainAppSellerRetur(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to "", FILTER_STATUS_ID to FILTER_RETUR)
        return UriUtil.buildUriAppendParams(ApplinkConstInternalOrder.RETUR, param)
    }

    fun getRegisteredNavigationMainAppSellerComplaint(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to "", FILTER_STATUS_ID to FILTER_COMPLAINT)
        return UriUtil.buildUriAppendParams(ApplinkConstInternalOrder.COMPLAINT, param)
    }
}