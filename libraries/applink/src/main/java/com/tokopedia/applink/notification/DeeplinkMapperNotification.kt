package com.tokopedia.applink.notification

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.startsWithPattern

object DeeplinkMapperNotification {

    fun getRegisteredNotification(deeplink: String): String {
        return if (deeplink.startsWithPattern(ApplinkConst.BUYER_INFO_WITH_ID)) getRegisteredNavigation(deeplink)
        else deeplink
    }

    private fun getRegisteredNavigation(deeplink: String): String {
        return deeplink.replace(DeeplinkConstant.SCHEME_TOKOPEDIA, DeeplinkConstant.SCHEME_INTERNAL)
    }

}