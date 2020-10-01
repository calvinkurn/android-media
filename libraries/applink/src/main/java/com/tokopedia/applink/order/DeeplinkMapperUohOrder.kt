package com.tokopedia.applink.order

import android.net.Uri
import com.tokopedia.applink.ApplinkConst.*
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.remoteconfig.RemoteConfigInstance

/**
 * Created by fwidjaja on 27/08/20.
 */
object DeeplinkMapperUohOrder {
    const val UOH_AB_TEST_KEY = "uoh_android"
    const val UOH_AB_TEST_VALUE = "uoh_android"

    private const val PATH_ORDER = "order"
    const val PATH_ORDER_ID = "order_id"

    fun getRegisteredNavigationUohOrder(deeplink: String): String {
        var returnedDeeplink = ""
        if (deeplink.equals(ORDER_LIST, true) || deeplink.equals(ORDER_LIST_WEBVIEW, true)
                || deeplink.equals(PURCHASE_ORDER, true) || deeplink.equals(PURCHASE_HISTORY, true) ) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER
            else getInternalDeeplink(deeplink)

        } else if (deeplink.startsWith(MARKETPLACE_ORDER_SUB) || deeplink.equals(PURCHASE_CONFIRMED, true)
                || deeplink.startsWith(PURCHASE_HISTORY) || deeplink.equals(PURCHASE_PROCESSED, true)
                || deeplink.equals(PURCHASE_SHIPPING_CONFIRM, true) || deeplink.equals(PURCHASE_SHIPPED, true)
                || deeplink.equals(PURCHASE_DELIVERED, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_MARKETPLACE_IN_PROCESS
            else getInternalDeeplink(deeplink)

        } else if (deeplink.equals(BELANJA_ORDER, true) || deeplink.equals(MARKETPLACE_ORDER, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_MARKETPLACE
            else getInternalDeeplink(deeplink)

        } else if (deeplink.equals(DIGITAL_ORDER, true) || deeplink.equals(Transaction.ORDER_HISTORY, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_DIGITAL
            else getInternalDeeplink(deeplink)

        } else if (deeplink.equals(EVENTS_ORDER, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_EVENTS
            else getInternalDeeplink(deeplink)

        } else if (deeplink.equals(DEALS_ORDER, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_DEALS
            else getInternalDeeplink(deeplink)

        } else if (deeplink.equals(FLIGHT_ORDER, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_PESAWAT
            else getInternalDeeplink(deeplink)

        } else if (deeplink.equals(GIFT_CARDS_ORDER, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_GIFTCARDS
            else getInternalDeeplink(deeplink)

        } else if (deeplink.equals(INSURANCE_ORDER, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_INSURANCE
            else getInternalDeeplink(deeplink)

        } else if (deeplink.equals(MODAL_TOKO_ORDER, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_MODALTOKO
            else getInternalDeeplink(deeplink)

        } else if (deeplink.equals(HOTEL_ORDER, true)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_HOTEL
            else getInternalDeeplink(deeplink)

        } else if (deeplink.startsWith(MARKETPLACE_ORDER) || deeplink.startsWith(DIGITAL_ORDER)
                || deeplink.startsWith(FLIGHT_ORDER) || deeplink.startsWith(HOTEL_ORDER)
                || deeplink.startsWith(OMS_DETAIL)) {
            returnedDeeplink = getInternalDeeplink(deeplink)
        }
        return returnedDeeplink
    }

    private fun useUoh(): Boolean {
        val remoteConfigValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(UOH_AB_TEST_KEY, "")
        return remoteConfigValue == UOH_AB_TEST_VALUE
    }

    private fun getInternalDeeplink(deeplink: String): String {
        when {
            deeplink.equals(PURCHASE_CONFIRMED, true) -> {
                return deeplink.replace(PURCHASE_CONFIRMED, MP_INTERNAL_CONFIRMED)

            }
            deeplink.equals(PURCHASE_PROCESSED, true) -> {
                return deeplink.replace(PURCHASE_PROCESSED, MP_INTERNAL_PROCESSED)

            }
            deeplink.equals(PURCHASE_SHIPPED, true) -> {
                return deeplink.replace(PURCHASE_SHIPPED, MP_INTERNAL_SHIPPED)

            }
            deeplink.equals(PURCHASE_DELIVERED, true) -> {
                return deeplink.replace(PURCHASE_DELIVERED, MP_INTERNAL_DELIVERED)

            }
            deeplink.equals(PURCHASE_SHIPPING_CONFIRM, true) -> {
                return deeplink.replace(PURCHASE_SHIPPING_CONFIRM, MP_INTERNAL_SHIPPED)

            }
            deeplink.startsWith(MARKETPLACE_ORDER) -> {
                return getMarketplaceOrderDetailInternalAppLink(deeplink)
            }
            else -> {
                return deeplink.replace(DeeplinkConstant.SCHEME_TOKOPEDIA, DeeplinkConstant.SCHEME_INTERNAL)
            }
        }
    }

    /**
     * @param deepLink tokopedia://marketplace/order/599769548
     * @return tokopedia-android-internal://marketplace/order?order_id=12345
     * or will return empty string if given invalid deep link
     * */
    private fun getMarketplaceOrderDetailInternalAppLink(deepLink: String): String {
        val uri = Uri.parse(deepLink)
        return when {
            uri.pathSegments.size == 2 && uri.pathSegments[0] == PATH_ORDER -> {
                val orderId: String = if (!uri.pathSegments[1].isNullOrBlank()) {
                    uri.pathSegments[1]
                } else {
                    "0"
                }
                Uri.parse(ApplinkConstInternalOrder.MARKETPLACE_ORDER)
                        .buildUpon()
                        .appendQueryParameter(PATH_ORDER_ID, orderId)
                        .build()
                        .toString()
            }
            else -> ""
        }
    }
}