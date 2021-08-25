package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName

class ProfileResponse(
        @SerializedName("address")
        val address: Address = Address(),
        @SerializedName("payment")
        val payment: Payment = Payment(),
        @SerializedName("shipment")
        val shipment: Shipment = Shipment()
)