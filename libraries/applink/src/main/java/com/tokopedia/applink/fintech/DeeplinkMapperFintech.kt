package com.tokopedia.applink.fintech

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalFintech
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.OQR_PIN_URL_ENTRY

object DeeplinkMapperFintech {
    fun getRegisteredNavigationForFintech(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        if (deeplink.startsWith(ApplinkConst.OQR_PIN_URL_ENTRY_LINK)) {
            return "$OQR_PIN_URL_ENTRY${uri.pathSegments[0]}/"
        }
        return deeplink
    }

    fun getRegisteredNavigationForLayanan(deeplink: String): String {
        deeplink.replace(DeeplinkConstant.SCHEME_TOKOPEDIA, DeeplinkConstant.SCHEME_INTERNAL).let {
            if (!it[it.length - 1].equals("/")) {
                return "$it/"
            }
            return it
        }
    }

    fun getRegisteredNavigationForPayLater(deeplink: String): String {
        val query = Uri.parse(deeplink).query
        val path = Uri.parse(deeplink).path
        return if (query?.isNotEmpty() == true || path?.isNotEmpty() == true) {
            "${ApplinkConstInternalFintech.PAYLATER}?$query"
        } else ApplinkConstInternalFintech.PAYLATER
    }
}