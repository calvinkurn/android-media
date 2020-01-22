package com.tokopedia.loginregister.loginthirdparty.facebook.data

import com.facebook.AccessToken

/**
 * Created by Ade Fulki on 2019-10-15.
 * ade.hadian@tokopedia.com
 */

data class FacebookCredentialData(
        var accessToken: AccessToken,
        var email: String,
        var phone: String
)