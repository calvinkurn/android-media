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
}