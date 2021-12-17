package com.tokopedia.applink.fintech

import android.net.Uri
import com.tokopedia.applink.ApplinkConst.*
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalFintech
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

    // check if pattern is tokopedia://fintech/home-credit/ktp
    // or
    // tokopedia://fintech/home-credit/selfie
    fun isHomeCreditRegister(uri: Uri): Boolean {
        val segments = uri.pathSegments
        if (segments.size < 2 || segments.size > 3) return false
        return uri.host == FINTECH && segments[0].equals(HOME_CREDIT) &&
                (segments[1].equals(SELFIE) || segments[1].equals(KTP))
    }

    fun getRegisteredNavigationForHomeCreditRegister(uri: Uri): String {
        val map =
            mutableMapOf(ApplinkConstInternalFintech.SHOW_KTP to (KTP == uri.pathSegments[1]).toString())
        val type = uri.pathSegments.getOrNull(2)
        if (type != null) {
            map[ApplinkConstInternalFintech.TYPE] = type
        }
        return UriUtil.buildUriAppendParam(ApplinkConstInternalFintech.HOME_CREDIT_REGISTER, map)
    }
}