package com.tokopedia.unifyorderhistory.data.model

import com.google.gson.annotations.SerializedName

data class TrainResendEmail(
    @SerializedName("trainResendBookingEmail")
    val trainResendBookingEmail: TrainResendBookingEmail? = TrainResendBookingEmail()
) {
    data class TrainResendBookingEmail(
        @SerializedName("Success")
        val success: Boolean = false
    )
}
