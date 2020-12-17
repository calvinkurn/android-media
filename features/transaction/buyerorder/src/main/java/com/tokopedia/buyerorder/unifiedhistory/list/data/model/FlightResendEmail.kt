package com.tokopedia.buyerorder.unifiedhistory.list.data.model


import com.google.gson.annotations.SerializedName

data class FlightResendEmail(
    @SerializedName("data")
    val data: Data = Data()
) {
    data class Data(
        @SerializedName("flightResendEmailV2")
        val flightResendEmailV2: FlightResendEmailV2? = FlightResendEmailV2()
    ) {
        data class FlightResendEmailV2(
            @SerializedName("meta")
            val meta: Meta = Meta()
        ) {
            data class Meta(
                @SerializedName("status")
                val status: String = ""
            )
        }
    }
}