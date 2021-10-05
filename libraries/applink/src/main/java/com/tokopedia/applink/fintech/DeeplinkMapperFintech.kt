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
        if (segments.size < 3 || segments.size > 4) return false
        return segments[0].equals(FINTECH) && segments[1].equals(HOME_CREDIT) &&
                (segments[2].equals(SELFIE) || segments[2].equals(KTP))
    }

    fun getRegisteredNavigationForHomeCreditRegister(uri: Uri): String {
        return UriUtil.buildUriAppendParam(
            ApplinkConstInternalFintech.HOME_CREDIT_REGISTER,
            mapOf(ApplinkConstInternalFintech.SHOW_KTP to (KTP == uri.pathSegments[2]).toString()))
    }
}