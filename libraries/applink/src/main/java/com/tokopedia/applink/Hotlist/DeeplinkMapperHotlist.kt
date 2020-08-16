package com.tokopedia.applink.Hotlist

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalCategory

object DeeplinkMapperHotlist {


    fun getRegisteredHotlist(deeplink: String): String {

            if (deeplink.startsWith(ApplinkConst.HOME_HOTLIST)) {
                return deeplink.replace(ApplinkConst.HOME_HOTLIST, ApplinkConstInternalCategory.INTERNAL_FIND)
            }
        return deeplink
    }
}