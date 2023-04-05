package com.tokopedia.logisticCommon.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class PinpointValidationResponse(

    @SerializedName("pinpoint_validation")
    val pinpointValidations: PinpointValidations = PinpointValidations()
) {

    data class PinpointValidations(

        @SerializedName("data")
        val data: PinpointValidationResponseData = PinpointValidationResponseData()
    ) {
        data class PinpointValidationResponseData(

            @SerializedName("result")
            val result: Boolean = false,

            @SerializedName("result_text")
            val resultText: String = "",

            @SerializedName("latitude")
            val latitude: Double = 0.0,

            @SerializedName("checksum")
            val checksum: String = "",

            @SuppressLint("Invalid Data Type")
            @SerializedName("district_id")
            val districtId: Int = 0,

            @SerializedName("longitude")
            val longitude: Double = 0.0
        )
    }
}
