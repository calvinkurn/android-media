package com.tokopedia.applink.contactus

import com.tokopedia.applink.ApplinkConst.CONTACT_US
import com.tokopedia.applink.ApplinkConst.CONTACT_US_NATIVE

object DeeplinkMapperContactUs {

    fun getNavigationContactUs(deeplink: String): String {
        return when {
            deeplink.startsWith(CONTACT_US) -> {
                deeplink.replace(CONTACT_US, CONTACT_US_NATIVE)
            }
            else -> {
                deeplink
            }
        }
    }
}
