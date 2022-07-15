package com.tokopedia.profilecompletion.addpin.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-09-04.
 * ade.hadian@tokopedia.com
 */

data class StatusPinPojo(
    @SerializedName("status_pin")
    var data: StatusPinData = StatusPinData()
)

data class StatusPinData(
    @SerializedName("is_registered")
    var isRegistered: Boolean = false,
    @SerializedName("is_blocked")
    var isBlocked: Boolean = false,
    @SerializedName("is_phone_number_exist")
    var isPhoneNumberExist: Boolean = false,
    @SerializedName("is_phone_number_verified")
    var isPhoneNumberVerified: Boolean = false,
    @SerializedName("msisdn")
    var msisdn: String = "",
    @SerializedName("error_message")
    var errorMessage: String = ""
)