package com.tokopedia.applink.communication

import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication

object DeeplinkMapperCommunication {
    fun getRegisteredNavigationTokoChat(deeplink: String): String {
        return deeplink.replace(
            DeeplinkConstant.SCHEME_TOKOPEDIA_SLASH,
            ApplinkConstInternalCommunication.INTERNAL_COMMUNICATION + "/"
        )
    }
}
