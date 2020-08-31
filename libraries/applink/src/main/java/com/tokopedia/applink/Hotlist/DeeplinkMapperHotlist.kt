package com.tokopedia.applink.Hotlist

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery

object DeeplinkMapperHotlist {

    fun getRegisteredHotlist(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        val query = uri.pathSegments[0].replace("-","+")
        if (deeplink.startsWith(ApplinkConst.HOME_HOTLIST)) {
            return ApplinkConstInternalDiscovery.SEARCH_RESULT + "?q=" + query
        }
        return deeplink
    }
}