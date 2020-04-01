package com.tokopedia.loginregister.loginthirdparty.facebook.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-11-25.
 * ade.hadian@tokopedia.com
 */

data class FacebookRequestData(
        @SerializedName("email") @Expose var email: String = "",
        @SerializedName("verified_mobile_phone") @Expose var phone: String = ""
)