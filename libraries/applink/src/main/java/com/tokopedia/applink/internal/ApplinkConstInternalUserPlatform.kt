package com.tokopedia.applink.internal

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalUserPlatform {

    private const val HOST_USER = "user"

    private const val INTERNAL_USER = "${DeeplinkConstant.SCHEME_INTERNAL}://${ApplinkConstInternalGlobal.HOST_GLOBAL}"

    const val NEW_INTERNAL_HOME = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_USER}"

    const val METHOD_LOGIN_EMAIL = "email"
    const val METHOD_LOGIN_PHONE = "phone"
    const val METHOD_LOGIN_GOOGLE = "google"
    const val METHOD_LOGIN_FACEBOOK = "facebook"
    const val METHOD_LOGIN_FINGERPRINT = "fingerprint"
    const val PAGE_EDIT_INFO_PROFILE_BIO = "bio"
    const val PAGE_EDIT_INFO_PROFILE_USERNAME = "username"
    const val PAGE_EDIT_INFO_PARAM = "page"

    //LoginActivity
    const val LOGIN = "$INTERNAL_USER/login"
    const val LOGIN_EMAIL = "${LOGIN}?method=$METHOD_LOGIN_EMAIL&e={email}&source={source}"
    const val LOGIN_PHONE = "${LOGIN}?method=$METHOD_LOGIN_PHONE&p={phone}&source={source}"
    const val LOGIN_THIRD_PARTY = "${LOGIN}?method={method}&source={source}"

    const val NEW_PROFILE_INFO = "${NEW_INTERNAL_HOME}/profile-info"
    const val EDIT_PROFILE_INFO = "${NEW_INTERNAL_HOME}/edit-profile-info"

    fun getInactivePhoneInternalDeepLink(deepLink: String): String {
        return deepLink.replace(ApplinkConst.CHANGE_INACTIVE_PHONE, ApplinkConstInternalGlobal.CHANGE_INACTIVE_PHONE)
    }
}