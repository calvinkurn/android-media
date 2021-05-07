package com.tokopedia.logintest.login.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-10-08.
 * ade.hadian@tokopedia.com
 */

data class StatusPinPojo(
        @SerializedName("status_pin")
        @Expose
        var data: StatusPinData = StatusPinData()
)

data class StatusPinData(
        @SerializedName("is_registered")
        @Expose
        var isRegistered: Boolean = false,
        @SerializedName("is_blocked")
        @Expose
        var isBlocked: Boolean = false,
        @SerializedName("is_phone_number_exist")
        @Expose
        var isPhoneNumberExist: Boolean = false,
        @SerializedName("is_phone_number_verified")
        @Expose
        var isPhoneNumberVerified: Boolean = false,
        @SerializedName("msisdn")
        @Expose
        var msisdn: String = "",
        @SerializedName("error_message")
        @Expose
        var errorMessage: String = ""
)