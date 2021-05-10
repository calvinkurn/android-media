package com.tokopedia.applink.marketplace

import com.tokopedia.applink.internal.ApplinkConstInternalLogistic

object DeeplinkMapperLogistic {

    const val HEADER_TEMPLATE = "tokopedia:/"

    fun getRegisteredNavigationOrder(deepLink: String): String {
        return deepLink.replace(HEADER_TEMPLATE, ApplinkConstInternalLogistic.INTERNAL_LOGISTIC)
    }
}