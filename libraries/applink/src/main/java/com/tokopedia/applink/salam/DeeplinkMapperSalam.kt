package com.tokopedia.applink.salam

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalSalam

object DeeplinkMapperSalam{
    fun getRegisteredNavigationSalamUmrah(deeplink: String): String {
        return deeplink.replace(ApplinkConst.SALAM_UMRAH, ApplinkConstInternalSalam.SALAM_UMRAH_HOME_PAGE)
    }
}