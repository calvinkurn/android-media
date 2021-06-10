package com.tokopedia.applink.fintech

import android.net.Uri
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.OQR_PIN_URL_ENTRY

object DeeplinkMapperFintech {
    fun getRegisteredNavigationForFintech(uri: Uri): String {
        return "$OQR_PIN_URL_ENTRY${uri.pathSegments[0]}/"
    }

    fun getRegisteredNavigationForLayanan(deeplink: String): String {
        deeplink.replace(DeeplinkConstant.SCHEME_TOKOPEDIA, DeeplinkConstant.SCHEME_INTERNAL).let {
            if (it[it.length - 1] != '/') {
                return "$it/"
            }
            return it
        }
    }
}