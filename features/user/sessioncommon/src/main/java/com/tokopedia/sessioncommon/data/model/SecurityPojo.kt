package com.tokopedia.sessioncommon.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Used by WSService.java (InstrumentationAuthHelper)
 */
class SecurityPojo {
    @SerializedName("allow_login")
    @Expose
    var allowLogin = 0

    @SerializedName("user_check_security_2")
    @Expose
    var userCheckSecurity2 = 0

    @SerializedName("user_check_security_1")
    @Expose
    var userCheckSecurity1 = 0
}
