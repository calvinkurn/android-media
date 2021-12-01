package com.tokopedia.applink.find

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery

object DeepLinkMapperFind {
    fun getRegisteredFind(deepLink: String): String {
        if (deepLink.startsWith(ApplinkConst.FIND)) {
            return deepLink.replace(ApplinkConst.FIND, ApplinkConstInternalDiscovery.SEARCH_RESULT)
        }else if(deepLink.startsWith(ApplinkConst.AMP_FIND))
            return deepLink.replace(ApplinkConst.AMP_FIND, ApplinkConstInternalDiscovery.SEARCH_RESULT)
        return deepLink
    }
}