package com.tokopedia.otp.notif.data

/**
 * Created by Ade Fulki on 01/10/20.
 */

data class SignResult(
        var publicKey: String = "",
        var signature: String = "",
        var datetime: String = ""
)