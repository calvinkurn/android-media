package com.tokopedia.applink.order

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst.*
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.DIGITAL_ORDER_LIST_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.MP_INTERNAL_CONFIRMED
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.MP_INTERNAL_DELIVERED
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.MP_INTERNAL_PROCESSED
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.MP_INTERNAL_SHIPPED
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.OMS_INTERNAL_ORDER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.ORDER_LIST_INTERNAL
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey

/**
 * Created by fwidjaja on 27/08/20.
 */
object DeeplinkMapperUohOrder {
    const val UOH_AB_TEST_KEY = "uoh_android_v2"
    const val UOH_AB_TEST_VALUE = "uoh_android_v2"

    private const val PATH_ORDER = "order"
    const val PATH_ORDER_ID = "order_id"

    fun getRegisteredNavigationUohOrder(context: Context, deeplink: String): String {
        var returnedDeeplink = ""
        if (deeplink.equals(ORDER_LIST, true) || deeplink.equals(ORDER_LIST_WEBVIEW, true)
                || deeplink.equals(PURCHASE_ORDER, true) || deeplink.equals(PURCHASE_HISTORY, true) ) {
            returnedDeeplink = if (useUoh(context)) ApplinkConstInternalOrder.UNIFY_ORDER
            else getInternalDeeplink(deeplink)

        } else if (deeplink.startsWith(MARKETPLACE_ORDER_SUB) || deeplink.equals(PURCHASE_CONFIRMED, true)
                || deeplink.startsWith(PURCHASE_HISTORY) || deeplink.equals(PURCHASE_PROCESSED, true)
                || deeplink.equals(PURCHASE_SHIPPING_CONFIRM, true) || deeplink.equals(PURCHASE_SHIPPED, true)
                || deeplink.equals(PURCHASE_DELIVERED, true)) {
            returnedDeeplink = if (useUoh(context)) ApplinkConstInternalOrder.UNIFY_ORDER_MARKETPLACE_IN_PROCESS
            else getInternalDeeplink(deeplink)

        } else if (deeplink.equals(BELANJA_ORDER, true) || deeplink.equals(MARKETPLACE_ORDER, true)) {
            returnedDeeplink = if (useUoh(context)) ApplinkConstInternalOrder.UNIFY_ORDER_MARKETPLACE
            else getInternalDeeplink(deeplink)

        } else if (deeplink.equals(DIGITAL_ORDER, true) || deeplink.equals(Transaction.ORDER_HISTORY, true)) {
            returnedDeeplink = if (useUoh(context)) ApplinkConstInternalOrder.UNIFY_ORDER_DIGITAL
            else getInternalDeeplink(deeplink)

        } else if (deeplink.equals(EVENTS_ORDER, true)) {
            returnedDeeplink = if (useUoh(context)) ApplinkConstInternalOrder.UNIFY_ORDER_EVENTS
            else getInternalDeeplink(deeplink)

        } else if (deeplink.equals(DEALS_ORDER, true)) {
            returnedDeeplink = if (useUoh(context)) ApplinkConstInternalOrder.UNIFY_ORDER_DEALS
            else getInternalDeeplink(deeplink)

        } else if (deeplink.equals(FLIGHT_ORDER, true)) {
            returnedDeeplink = if (useUoh(context)) ApplinkConstInternalOrder.UNIFY_ORDER_PESAWAT
            else getInternalDeeplink(deeplink)

        } else if (deeplink.equals(GIFT_CARDS_ORDER, true)) {
            returnedDeeplink = if (useUoh(context)) ApplinkConstInternalOrder.UNIFY_ORDER_GIFTCARDS
            else getInternalDeeplink(deeplink)

        } else if (deeplink.equals(INSURANCE_ORDER, true)) {
            returnedDeeplink = if (useUoh(context)) ApplinkConstInternalOrder.UNIFY_ORDER_INSURANCE
            else getInternalDeeplink(deeplink)

        } else if (deeplink.equals(MODAL_TOKO_ORDER, true)) {
            returnedDeeplink = if (useUoh(context)) ApplinkConstInternalOrder.UNIFY_ORDER_MODALTOKO
            else getInternalDeeplink(deeplink)

        } else if (deeplink.equals(HOTEL_ORDER, true)) {
            returnedDeeplink = if (useUoh(context)) ApplinkConstInternalOrder.UNIFY_ORDER_HOTEL
            else getInternalDeeplink(deeplink)

        } else if (deeplink.startsWith(MARKETPLACE_ORDER) || deeplink.startsWith(DIGITAL_ORDER)
                || deeplink.startsWith(FLIGHT_ORDER) || deeplink.startsWith(HOTEL_ORDER)
                || deeplink.startsWith(OMS_ORDER_DETAIL)) {
            returnedDeeplink = getInternalDeeplink(deeplink)
        }
        return returnedDeeplink
    }

    fun useUoh(context: Context): Boolean {
        return try {
            val remoteConfigRollenceValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(UOH_AB_TEST_KEY, "")
            val rollence = remoteConfigRollenceValue.equals(UOH_AB_TEST_VALUE, ignoreCase = true)

            val remoteConfig = FirebaseRemoteConfigImpl(context)
            val remoteConfigFirebase: Boolean = remoteConfig.getBoolean(RemoteConfigKey.ENABLE_UOH)
            return (rollence && remoteConfigFirebase)

        } catch (e: Exception) {
            false
        }
    }

    private fun getInternalDeeplink(deeplink: String): String {
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
                return getMarketplaceDigitalOrderDetailInternalAppLink(deeplink)
            }
            deeplink.startsWith(OMS_ORDER_DETAIL) -> {
                return getOmsOrderDetailInternalAppLink(deeplink)
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
    private fun getMarketplaceDigitalOrderDetailInternalAppLink(deepLink: String): String {
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
                    category = ApplinkConstInternalOrder.MARKETPLACE_ORDER
                } else if (deepLink.startsWith(DIGITAL_ORDER)) {
                    category = ApplinkConstInternalOrder.DIGITAL_ORDER
                }

                Uri.parse(category)
                        .buildUpon()
                        .appendQueryParameter(PATH_ORDER_ID, orderId)
                        .build()
                        .toString()
            }
            else -> {
                if (deepLink.startsWith(MARKETPLACE_ORDER)) {
                    MARKETPLACE_ORDER
                } else ""
            }
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