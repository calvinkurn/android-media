package com.tokopedia.applink.marketplace

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.home.DeeplinkMapperHome
import com.tokopedia.applink.internal.ApplinkConsInternalHome
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMechant

object DeeplinkMapperLogistic {

    const val HEADER_TEMPLATE = "tokopedia:/"

    fun getRegisteredNavigationOrder(deepLink: String): String {
        return deepLink.replace(HEADER_TEMPLATE, ApplinkConstInternalLogistic.INTERNAL_LOGISTIC)
    }

    fun getRegisteredNavigationPod(deepLink: String): String {
        return deepLink.replace(HEADER_TEMPLATE, ApplinkConstInternalLogistic.INTERNAL_LOGISTIC)
    }
}