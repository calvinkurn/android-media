package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalUserPlatform {

    private const val HOST_USER = "user"
    private const val INTERNAL_USER = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_USER}"

    const val METHOD_LOGIN_EMAIL = "email"
    const val METHOD_LOGIN_PHONE = "phone"
    const val METHOD_LOGIN_GOOGLE = "google"
    const val METHOD_LOGIN_FACEBOOK = "facebook"
    const val METHOD_LOGIN_WEBVIEW = "webview"

    //LoginActivity
    const val LOGIN = "$INTERNAL_USER/login"
    const val LOGIN_EMAIL = "${LOGIN}?method=$METHOD_LOGIN_EMAIL&email={email}&password={password}&source={source}"
    const val LOGIN_PHONE = "${LOGIN}?method=$METHOD_LOGIN_PHONE&phone={phone}&source={source}"
    const val LOGIN_WITH_PARAM = "${LOGIN}?method={method}&phone={phone}&email={email}&password={password}&source={source}"
    const val LOGIN_THIRD_PARTY = "${LOGIN}?method={method}&source={source}"
    const val LOGIN_WEBVIEW = "${LOGIN}?method=$METHOD_LOGIN_WEBVIEW&name={name}&url={url}"

}