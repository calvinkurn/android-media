package com.tokopedia.applink.Hotlist

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery

object DeeplinkMapperHotlist {


    fun getRegisteredHotlist(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        if (deeplink.startsWith(ApplinkConst.HOME_HOTLIST)) {
            return ApplinkConstInternalDiscovery.SEARCH_RESULT + "?q=" + uri.pathSegments[0]
        }
        return deeplink
    }
}