package com.tokopedia.loginHelper.domain

sealed class LoginHelperEnvType {
    object STAGING : LoginHelperEnvType()
    object PRODUCTION : LoginHelperEnvType()
}
