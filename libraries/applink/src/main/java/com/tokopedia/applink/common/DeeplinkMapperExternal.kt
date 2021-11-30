package com.tokopedia.applink.common

import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal

object DeeplinkMapperExternal {
    fun getRegisteredNavigation(deeplink: String): String {
        return deeplink.replace(DeeplinkConstant.SCHEME_TOKOPEDIA_SLASH,
                ApplinkConstInternalGlobal.INTERNAL_GLOBAL+"/")
    }
}