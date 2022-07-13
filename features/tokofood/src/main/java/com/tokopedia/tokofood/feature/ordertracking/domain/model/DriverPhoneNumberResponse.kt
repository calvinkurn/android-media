package com.tokopedia.tokofood.feature.ordertracking.domain.model

import com.google.gson.annotations.SerializedName

data class DriverPhoneNumberResponse(
    @SerializedName("tokofoodDriverPhoneNumber")
    val tokofoodDriverPhoneNumber: TokofoodDriverPhoneNumber = TokofoodDriverPhoneNumber()
) {
    data class TokofoodDriverPhoneNumber(
        @SerializedName("isCallable")
        val isCallable: Boolean = false,
        @SerializedName("phoneNumber")
        val phoneNumber: String? = ""
    )
}