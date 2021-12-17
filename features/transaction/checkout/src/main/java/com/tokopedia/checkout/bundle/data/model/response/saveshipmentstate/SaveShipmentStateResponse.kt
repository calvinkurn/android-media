package com.tokopedia.checkout.bundle.data.model.response.saveshipmentstate

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SaveShipmentStateResponse(
        @SerializedName("success")
        val success: Int = 0,

        @SerializedName("error")
        val error: String = "",

        @SerializedName("message")
        val message: String = "",
)