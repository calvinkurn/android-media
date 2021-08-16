package com.tokopedia.applink.order

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst.*
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.DIGITAL_ORDER_LIST_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.MP_INTERNAL_CONFIRMED
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.MP_INTERNAL_DELIVERED
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.MP_INTERNAL_PROCESSED
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.MP_INTERNAL_SHIPPED
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.OMS_INTERNAL_ORDER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.ORDER_LIST_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PESAWAT_INTERNAL_ORDER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.UNIFY_ORDER_ONGOING
import com.tokopedia.remoteconfig.RemoteConfigInstance
import java.util.*

/**
 * Created by fwidjaja on 27/08/20.
 */
object DeeplinkMapperUohOrder {
    const val UOH_AB_TEST_KEY = "uoh_android_v2"
    const val UOH_AB_TEST_VALUE = "uoh_android_v2"

    private const val PATH_ORDER = "order"
    const val PATH_ORDER_ID = "order_id"
    const val PATH_PAYMENT_ID = "payment_id"
    const val PATH_CART_STRING = "cart_string"

    fun isNavigationUohOrder(deeplink: String):Boolean{
        val d = deeplink.toLowerCase(Locale.getDefault())
        val td = trimDeeplink(d)
        return td == BELANJA_ORDER ||
                d.startsWith(MARKETPLACE_ORDER)||
                d.startsWith(MARKETPLACE_ORDER_SUB) ||
                td == ORDER_LIST ||
                td == ORDER_LIST_WEBVIEW ||
                d.startsWith(DIGITAL_ORDER) ||
                td == ORDER_HISTORY ||
                td == EVENTS_ORDER ||
                td == DEALS_ORDER ||
                td == FLIGHT_ORDER ||
                td == TRAIN_ORDER ||
                td == GIFT_CARDS_ORDER ||
                td == INSURANCE_ORDER ||
                td == MODAL_TOKO_ORDER ||
                td == HOTEL_ORDER ||
                td == PURCHASE_ORDER ||
                td == PURCHASE_CONFIRMED ||
                td == PURCHASE_PROCESSED ||
                td == PURCHASE_SHIPPING_CONFIRM ||
                td == PURCHASE_SHIPPED ||
                td == PURCHASE_DELIVERED ||
                d.startsWith(PURCHASE_HISTORY) ||
                td == ORDER_HISTORY ||
                d.startsWith(OMS_ORDER_DETAIL) ||
                td == TRAVEL_AND_ENTERTAINMENT_ORDER ||
                td == PURCHASE_ONGOING
    }

    private fun trimDeeplink(deeplink: String): String {
        val qIndex = deeplink.indexOf('?')
        val deeplinkWithoutQuery = if (qIndex > 0) {
            deeplink.substring(0, qIndex)
        } else deeplink
        return if (deeplinkWithoutQuery.endsWith("/")) {
            deeplinkWithoutQuery.substringBeforeLast("/")
        } else {
            deeplinkWithoutQuery
        }
    }

    fun getRegisteredNavigationUohOrder(context: Context, deeplink: String): String {
        var returnedDeeplink = ""
        if (deeplink.equals(ORDER_LIST, true) || deeplink.equals(ORDER_LIST_WEBVIEW, true)
                || deeplink.equals(PURCHASE_ORDER, true) || deeplink.equals(PURCHASE_HISTORY, true) ) {
            returnedDeeplink = ApplinkConstInternalOrder.UNIFY_ORDER

        } else if (deeplink.equals(PURCHASE_CONFIRMED, true)
                || deeplink.equals(MARKETPLACE_WAITING_CONFIRMATION, true)) {
            returnedDeeplink = ApplinkConstInternalOrder.UNIFY_ORDER_WAITING_CONFIRMATION

        } else if (deeplink.equals(PURCHASE_PROCESSED, true)
                || deeplink.equals(MARKETPLACE_ORDER_PROCESSED, true)) {
            returnedDeeplink = ApplinkConstInternalOrder.UNIFY_ORDER_PROCESSED

        } else if (deeplink.equals(PURCHASE_SHIPPED, true)
                || deeplink.equals(MARKETPLACE_SENT, true)) {
            returnedDeeplink = ApplinkConstInternalOrder.UNIFY_ORDER_SENT

        } else if (deeplink.equals(PURCHASE_DELIVERED, true)
                || deeplink.equals(MARKETPLACE_DELIVERED, true)) {
            returnedDeeplink = ApplinkConstInternalOrder.UNIFY_ORDER_DELIVERED

        } else if (deeplink.startsWith(MARKETPLACE_ORDER_SUB) || deeplink.equals(PURCHASE_CONFIRMED, true)
                || deeplink.startsWith(PURCHASE_HISTORY) || deeplink.equals(PURCHASE_PROCESSED, true)
                || deeplink.equals(PURCHASE_SHIPPING_CONFIRM, true) || deeplink.equals(PURCHASE_SHIPPED, true)
                || deeplink.equals(PURCHASE_DELIVERED, true)) {
            returnedDeeplink = ApplinkConstInternalOrder.UNIFY_ORDER_MARKETPLACE_IN_PROCESS

        } else if (deeplink.equals(BELANJA_ORDER, true) || deeplink.equals(MARKETPLACE_ORDER, true)) {
            returnedDeeplink = ApplinkConstInternalOrder.UNIFY_ORDER_MARKETPLACE

        } else if (deeplink.equals(DIGITAL_ORDER, true) || deeplink.equals(Transaction.ORDER_HISTORY, true)) {
            returnedDeeplink = ApplinkConstInternalOrder.UNIFY_ORDER_DIGITAL

        } else if (deeplink.equals(EVENTS_ORDER, true)) {
            returnedDeeplink = ApplinkConstInternalOrder.UNIFY_ORDER_EVENTS

        } else if (deeplink.equals(DEALS_ORDER, true)) {
            returnedDeeplink = ApplinkConstInternalOrder.UNIFY_ORDER_DEALS

        } else if (deeplink.equals(FLIGHT_ORDER, true)) {
            returnedDeeplink = ApplinkConstInternalOrder.UNIFY_ORDER_PESAWAT

        } else if (deeplink.equals(TRAIN_ORDER, true)) {
            returnedDeeplink = ApplinkConstInternalOrder.UNIFY_ORDER_TRAIN

        } else if (deeplink.equals(GIFT_CARDS_ORDER, true)) {
            returnedDeeplink = ApplinkConstInternalOrder.UNIFY_ORDER_GIFTCARDS

        } else if (deeplink.equals(INSURANCE_ORDER, true)) {
            returnedDeeplink = ApplinkConstInternalOrder.UNIFY_ORDER_INSURANCE

        } else if (deeplink.equals(MODAL_TOKO_ORDER, true)) {
            returnedDeeplink = ApplinkConstInternalOrder.UNIFY_ORDER_MODALTOKO

        } else if (deeplink.equals(HOTEL_ORDER, true)) {
            returnedDeeplink = ApplinkConstInternalOrder.UNIFY_ORDER_HOTEL

        } else if (deeplink.startsWith(MARKETPLACE_ORDER) || deeplink.startsWith(DIGITAL_ORDER)
                || deeplink.startsWith(FLIGHT_ORDER)
                || deeplink.startsWith(HOTEL_ORDER) || deeplink.startsWith(OMS_ORDER_DETAIL)) {
            returnedDeeplink = getInternalDeeplink(context, deeplink)

        } else if (deeplink.equals(TRAVEL_AND_ENTERTAINMENT_ORDER, true)) {
            returnedDeeplink = ApplinkConstInternalOrder.UNIFY_ORDER_TRAVEL_ENTERTAINMENT

        } else if (deeplink.equals(PURCHASE_ONGOING, true)) {
            returnedDeeplink = getInternalDeeplink(context, deeplink)
        }

        return returnedDeeplink
    }

    private fun getInternalDeeplink(context: Context, deeplink: String): String {
        when {
            deeplink.equals(PURCHASE_CONFIRMED, true)
                    || deeplink.equals(MARKETPLACE_WAITING_CONFIRMATION, true) -> {
                return MP_INTERNAL_CONFIRMED

            }
            deeplink.equals(PURCHASE_PROCESSED, true)
                    || deeplink.equals(MARKETPLACE_ORDER_PROCESSED, true) -> {
                return MP_INTERNAL_PROCESSED

            }
            deeplink.equals(PURCHASE_SHIPPED, true)
                    || deeplink.equals(MARKETPLACE_SENT, true) -> {
                return MP_INTERNAL_SHIPPED

            }
            deeplink.equals(PURCHASE_DELIVERED, true)
                    || deeplink.equals(MARKETPLACE_DELIVERED, true) -> {
                return MP_INTERNAL_DELIVERED
            }

            deeplink.equals(PURCHASE_SHIPPING_CONFIRM, true) -> {
                return MP_INTERNAL_SHIPPED
            }
            deeplink.equals(ORDER_LIST, true) -> {
                return ORDER_LIST_INTERNAL
            }
            deeplink.equals(DIGITAL_ORDER, true) || deeplink.equals(Transaction.ORDER_HISTORY, true) -> {
                return DIGITAL_ORDER_LIST_INTERNAL
            }
            deeplink.equals(MARKETPLACE_ORDER, true) -> {
                return ORDER_LIST_INTERNAL
            }
            deeplink.startsWith(MARKETPLACE_ORDER) || (deeplink.startsWith(DIGITAL_ORDER) && !deeplink.equals(DIGITAL_ORDER, true)) -> {
                return getMarketplaceDigitalOrderDetailInternalAppLink(context, deeplink)
            }
            deeplink.startsWith(OMS_ORDER_DETAIL) -> {
                return getOmsOrderDetailInternalAppLink(deeplink)
            }
            deeplink.equals(TRAVEL_AND_ENTERTAINMENT_ORDER, true) -> {
                return PESAWAT_INTERNAL_ORDER
            }
            deeplink.equals(PURCHASE_ONGOING, true) -> {
                return UNIFY_ORDER_ONGOING
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
    private fun getMarketplaceDigitalOrderDetailInternalAppLink(context: Context, deepLink: String): String {
        val uri = Uri.parse(deepLink)
        return when {
            uri.pathSegments.size == 2 && uri.pathSegments[0] == PATH_ORDER -> {
                val orderId: String = if (!uri.pathSegments[1].isNullOrBlank()) {
                    uri.pathSegments[1]
                } else {
                    "0"
                }

                var category = ""
                if (deepLink.startsWith(MARKETPLACE_ORDER)) {
                    category = ApplinkConstInternalOrder.MARKETPLACE_INTERNAL_BUYER_ORDER_DETAIL
                } else if (deepLink.startsWith(DIGITAL_ORDER)) {
                    category = ApplinkConstInternalOrder.DIGITAL_ORDER
                }

                Uri.parse(category)
                        .buildUpon()
                        .appendQueryParameter(PATH_ORDER_ID, orderId)
                        .build()
                        .toString()
            }
            uri.pathSegments.size == 1 && uri.pathSegments[0] == PATH_ORDER && !uri.getQueryParameter(PATH_PAYMENT_ID).isNullOrEmpty() && !uri.getQueryParameter(PATH_CART_STRING).isNullOrEmpty() -> {
                val paymentId = uri.getQueryParameter(PATH_PAYMENT_ID)
                val cartString = uri.getQueryParameter(PATH_CART_STRING)

                var category = ""
                if (deepLink.startsWith(MARKETPLACE_ORDER)) {
                    category = ApplinkConstInternalOrder.MARKETPLACE_INTERNAL_BUYER_ORDER_DETAIL
                } else if (deepLink.startsWith(DIGITAL_ORDER)) {
                    category = ApplinkConstInternalOrder.DIGITAL_ORDER
                }

                Uri.parse(category)
                        .buildUpon()
                        .appendQueryParameter(PATH_PAYMENT_ID, paymentId)
                        .appendQueryParameter(PATH_CART_STRING, cartString)
                        .build()
                        .toString()
            }
            else -> ""
        }
    }

    private fun getOmsOrderDetailInternalAppLink(deepLink: String): String {
        val uri = Uri.parse(deepLink)
        return when {
            uri.pathSegments.size == 1 -> {
                val orderId: String = if (!uri.pathSegments[0].isNullOrBlank()) {
                    uri.pathSegments[0]
                } else {
                    "0"
                }

                Uri.parse(OMS_INTERNAL_ORDER)
                        .buildUpon()
                        .appendQueryParameter(PATH_ORDER_ID, orderId)
                        .build()
                        .toString()
            }
            else -> ""
        }
    }
}