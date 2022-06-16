package com.tokopedia.applink.marketplace

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic

object DeeplinkMapperLogistic {

    const val HEADER_TEMPLATE = "tokopedia:/"
    private const val QUERY_PARAM_ORDER_ID = "order_id"

    fun getRegisteredNavigationOrder(deepLink: String): String {
        return deepLink.replace(HEADER_TEMPLATE, ApplinkConstInternalLogistic.INTERNAL_LOGISTIC)
    }

    fun getReschedulePickupDeeplink(context: Context, uri: Uri, deeplink: String): String {
        val orderId = uri.getQueryParameter(QUERY_PARAM_ORDER_ID) ?: uri.pathSegments.last()
        return ApplinkConstInternalLogistic.RESCHEDULE_PICKUP.replace("{order_id}", orderId)
    }

    fun getRegisteredNavigationPod(deepLink: String): String {
        return deepLink.replace(HEADER_TEMPLATE, ApplinkConstInternalLogistic.INTERNAL_LOGISTIC)
    }
}