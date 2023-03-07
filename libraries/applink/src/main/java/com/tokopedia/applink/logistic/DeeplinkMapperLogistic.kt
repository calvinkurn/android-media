package com.tokopedia.applink.logistic

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic

object DeeplinkMapperLogistic {

    const val HEADER_TEMPLATE = "tokopedia:/"
    private const val QUERY_PARAM_ORDER_ID = "order_id"

    fun getRegisteredNavigationOrder(deepLink: String): String {
        return deepLink.replace(HEADER_TEMPLATE, ApplinkConstInternalLogistic.INTERNAL_LOGISTIC)
    }

    fun getReschedulePickupDeeplink(uri: Uri): String {
        val orderId = uri.getQueryParameter(QUERY_PARAM_ORDER_ID) ?: uri.pathSegments.last()
        return ApplinkConstInternalLogistic.RESCHEDULE_PICKUP.replace("{order_id}", orderId)
    }

    fun getEditAddressDeeplink(deeplink: String): String {
        return deeplink.replace(ApplinkConst.SETTING_EDIT_ADDRESS, ApplinkConstInternalLogistic.EDIT_ADDRESS_REVAMP)
    }

    fun getRegisteredNavigationPod(deepLink: String): String {
        return deepLink.replace(HEADER_TEMPLATE, ApplinkConstInternalLogistic.INTERNAL_LOGISTIC)
    }

    fun getRegisteredNavigationShareAddress(deepLink: String): String {
        return deepLink.replace(ApplinkConst.SHARE_ADDRESS, ApplinkConstInternalLogistic.MANAGE_ADDRESS)
    }
}
