package com.tokopedia.applink.marketplace

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.startsWithPattern

object DeeplinkMapperLogistic {

    const val HEADER_TEMPLATE = "tokopedia:/"

    fun getRegisteredNavigationOrder(deepLink: String): String {
        return deepLink.replace(HEADER_TEMPLATE, ApplinkConstInternalLogistic.INTERNAL_LOGISTIC)
    }
}