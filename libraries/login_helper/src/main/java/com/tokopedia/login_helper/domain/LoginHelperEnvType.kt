package com.tokopedia.login_helper.domain

sealed class LoginHelperEnvType {
    object STAGING : LoginHelperEnvType()
    object PRODUCTION : LoginHelperEnvType()
}
