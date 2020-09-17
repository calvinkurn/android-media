package com.tokopedia.applink.order

import com.tokopedia.applink.ApplinkConst.*
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.remoteconfig.RemoteConfigInstance

/**
 * Created by fwidjaja on 27/08/20.
 */
object DeeplinkMapperUohOrder {
    private var UOH_AB_TEST_KEY = "uoh_android"
    private var UOH_AB_TEST_VALUE = "uoh_android"

    fun getRegisteredNavigationUohOrder(deeplink: String): String {
        var returnedDeeplink = ""
        if (deeplink.startsWith(DIGITAL_ORDER)
                || deeplink.startsWith(EVENTS_ORDER) || deeplink.startsWith(DEALS_ORDER)
                || deeplink.startsWith(FLIGHT_ORDER) || deeplink.startsWith(GIFT_CARDS_ORDER)
                || deeplink.startsWith(INSURANCE_ORDER) || deeplink.startsWith(MODAL_TOKO_ORDER)
                || deeplink.startsWith(HOTEL_ORDER) || deeplink.equals(ORDER_LIST, true)
                || deeplink.equals(ORDER_LIST_WEBVIEW, true) || deeplink.equals(PURCHASE_ORDER, true)
                || deeplink.equals(PURCHASE_CONFIRMED, true) || deeplink.equals(PURCHASE_PROCESSED, true)
                || deeplink.equals(PURCHASE_SHIPPING_CONFIRM, true) || deeplink.equals(PURCHASE_SHIPPED, true)
                || deeplink.equals(PURCHASE_DELIVERED, true) || deeplink.equals(PURCHASE_HISTORY, true)
                || deeplink.equals(ORDER_HISTORY, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER
            else deeplink

        } else if (deeplink.startsWith(MARKETPLACE_ORDER_SUB) || deeplink.equals(PURCHASE_CONFIRMED, true)
                || deeplink.equals(PURCHASE_PROCESSED, true) || deeplink.equals(PURCHASE_SHIPPING_CONFIRM, true)
                || deeplink.equals(PURCHASE_SHIPPED, true) || deeplink.equals(PURCHASE_DELIVERED, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_MARKETPLACE_IN_PROCESS
            else deeplink

        } else if (deeplink.equals(BELANJA_ORDER, true) || deeplink.equals(MARKETPLACE_ORDER, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_MARKETPLACE
            else deeplink

        } else if (deeplink.equals(DIGITAL_ORDER, true) || deeplink.equals(Transaction.ORDER_HISTORY, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_DIGITAL
            else deeplink

        } else if (deeplink.equals(EVENTS_ORDER, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_EVENTS
            else deeplink

        } else if (deeplink.equals(DEALS_ORDER, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_DEALS
            else deeplink

        } else if (deeplink.equals(FLIGHT_ORDER, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_PESAWAT
            else deeplink

        } else if (deeplink.equals(GIFT_CARDS_ORDER, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_GIFTCARDS
            else deeplink

        } else if (deeplink.equals(INSURANCE_ORDER, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_INSURANCE
            else deeplink

        } else if (deeplink.equals(MODAL_TOKO_ORDER, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_MODALTOKO
            else deeplink

        } else if (deeplink.equals(HOTEL_ORDER, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_HOTEL
            else deeplink
        }
        return returnedDeeplink
    }

    private fun useUoh(): Boolean {
        val remoteConfigValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(UOH_AB_TEST_KEY, "")
        return remoteConfigValue == UOH_AB_TEST_VALUE
    }
}