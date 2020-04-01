package com.tokopedia.applink.find

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalCategory

object DeepLinkMapperFind {
    fun getRegisteredFind(deepLink: String): String {
        if (deepLink.startsWith(ApplinkConst.FIND)) {
            return deepLink.replace(ApplinkConst.FIND, ApplinkConstInternalCategory.INTERNAL_FIND)
        }else if(deepLink.startsWith(ApplinkConst.AMP_FIND))
            return deepLink.replace(ApplinkConst.AMP_FIND, ApplinkConstInternalCategory.INTERNAL_FIND)
        return deepLink
    }
}