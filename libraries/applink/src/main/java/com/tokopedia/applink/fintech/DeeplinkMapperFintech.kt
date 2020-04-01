package com.tokopedia.applink.fintech

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.OQR_PIN_URL_ENTRY

object DeeplinkMapperFintech {
    fun getRegisteredNavigationForFintech(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        if (deeplink.startsWith(ApplinkConst.OQR_PIN_URL_ENTRY_LINK)) {
            return "$OQR_PIN_URL_ENTRY${uri.pathSegments[0]}/"
        }
        return deeplink
    }
}