package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PassengerForm (
    @SerializedName("passenger_informations")
    @Expose
    val passengerInformations: List<PassengerInformation> = emptyList()
)