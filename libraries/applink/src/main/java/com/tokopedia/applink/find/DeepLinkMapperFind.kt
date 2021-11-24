package com.tokopedia.applink.find

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery

object DeepLinkMapperFind {
    fun getRegisteredFind(deepLink: String): String {
        var uri = Uri.parse(deepLink)

        var query = uri.lastPathSegment?.replace("-", "%20")
        if (deepLink.startsWith(ApplinkConst.FIND) || deepLink.startsWith(ApplinkConst.AMP_FIND)) {
            return ApplinkConstInternalDiscovery.SEARCH_RESULT + "?q=" + query
        }
        return deepLink
    }
}