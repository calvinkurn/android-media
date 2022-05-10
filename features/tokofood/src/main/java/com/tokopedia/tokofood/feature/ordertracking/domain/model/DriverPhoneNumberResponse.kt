package com.tokopedia.tokofood.feature.ordertracking.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DriverPhoneNumberResponse(
    @SerializedName("tokofoodDriverPhoneNumber")
    val tokofoodDriverPhoneNumber: TokofoodDriverPhoneNumber = TokofoodDriverPhoneNumber()
) {
    data class TokofoodDriverPhoneNumber(
        @SerializedName("isCallable")
        @Expose
        val isCallable: Boolean = false,
        @SerializedName("phoneNumber")
        @Expose
        val phoneNumber: String? = ""
    )
}