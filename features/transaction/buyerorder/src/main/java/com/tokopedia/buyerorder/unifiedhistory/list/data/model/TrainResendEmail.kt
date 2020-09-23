package com.tokopedia.buyerorder.unifiedhistory.list.data.model


import com.google.gson.annotations.SerializedName

data class TrainResendEmail(
    @SerializedName("data")
    val data: Data = Data()
) {
    data class Data(
        @SerializedName("trainResendBookingEmail")
        val trainResendBookingEmail: TrainResendBookingEmail? = TrainResendBookingEmail()
    ) {
        data class TrainResendBookingEmail(
            @SerializedName("Success")
            val success: Boolean = false
        )
    }
}