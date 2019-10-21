package com.tokopedia.promocheckout.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FlightCancelVoucher (

        @SerializedName("type")
        @Expose
        var type: String = "",
        @SerializedName("attributes")
        @Expose
        var attributes: Attributes = Attributes()

) {
    class Response(
            @SerializedName("flightCancelVoucher")
            @Expose
            var response: FlightCancelVoucher = FlightCancelVoucher()
    )

        class Attributes(
                @SerializedName("success")
                @Expose
                var success: Boolean = true
        )
}