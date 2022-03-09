package com.tokopedia.applink.internal

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalUserPlatform {

    private const val INTERNAL_USER = "${DeeplinkConstant.SCHEME_INTERNAL}://${ApplinkConstInternalGlobal.HOST_GLOBAL}"

    const val METHOD_LOGIN_EMAIL = "email"
    const val METHOD_LOGIN_PHONE = "phone"
    const val METHOD_LOGIN_GOOGLE = "google"
    const val METHOD_LOGIN_FACEBOOK = "facebook"
    const val METHOD_LOGIN_FINGERPRINT = "fingerprint"

    //LoginActivity
    const val LOGIN = "$INTERNAL_USER/login"
    const val LOGIN_EMAIL = "${LOGIN}?method=$METHOD_LOGIN_EMAIL&e={email}&source={source}"
    const val LOGIN_PHONE = "${LOGIN}?method=$METHOD_LOGIN_PHONE&p={phone}&source={source}"
    const val LOGIN_THIRD_PARTY = "${LOGIN}?method={method}&source={source}"

    fun getInactivePhoneInternalDeepLink(deepLink: String): String {
        return deepLink.replace(ApplinkConst.CHANGE_INACTIVE_PHONE, ApplinkConstInternalGlobal.CHANGE_INACTIVE_PHONE)
    }
}