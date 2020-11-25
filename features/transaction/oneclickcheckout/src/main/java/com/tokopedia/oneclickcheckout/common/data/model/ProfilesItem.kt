package com.tokopedia.oneclickcheckout.common.data.model

import com.google.gson.annotations.SerializedName

data class ProfilesItem(
        @SerializedName("profile_id")
        val profileId: Int = 0,
        @SerializedName("enable")
        val enable: Boolean = false,
        @SerializedName("status")
        val status: Int = 1,
        @SerializedName("address")
        val address: Address = Address(),
        @SerializedName("payment")
        val payment: Payment = Payment(),
        @SerializedName("shipment")
        val shipment: Shipment = Shipment()
)
